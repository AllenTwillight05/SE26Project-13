package com.englishlearningcopilot.backend.service.impl;

import com.englishlearningcopilot.backend.dto.PetChatRequest;
import com.englishlearningcopilot.backend.dto.PetChatResponse;
import com.englishlearningcopilot.backend.entity.SpeakingScenario;
import com.englishlearningcopilot.backend.entity.Vocabulary;
import com.englishlearningcopilot.backend.repository.SpeakingScenarioRepository;
import com.englishlearningcopilot.backend.repository.VocabularyRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriUtils;

@Service
public class PetCompanionServiceImpl {

    private static final String INDEX_VERSION = "pet-vector-index-v3";
    private static final Pattern LATIN_TOKEN_PATTERN = Pattern.compile("[a-zA-Z0-9]+");
    private static final Pattern CJK_PATTERN = Pattern.compile("\\p{IsHan}+");
    private static final Set<String> STOP_TOKENS = Set.of(
            "the", "a", "an", "and", "or", "to", "of", "in", "on", "for", "with", "is", "are", "am",
            "我", "你", "他", "她", "它", "们", "的", "了", "和", "想", "要", "在", "有", "但", "现在", "一个"
    );

    private static final List<VocabularyLevelProfile> VOCABULARY_LEVELS = List.of(
            new VocabularyLevelProfile(
                    "starter",
                    "入门",
                    Set.of("zk", "gk"),
                    """
                    入门 零基础 初学者 英语不好 刚开始 学基础词 高频简单词 校园基础 日常简单表达
                    beginner starter elementary basic words simple words high frequency vocabulary
                    """
            ),
            new VocabularyLevelProfile(
                    "basic",
                    "基础",
                    Set.of("cet4"),
                    """
                    基础 有一点基础 日常交流 旅行生活 常用表达 四级 普通词汇 机场 酒店 餐厅 购物 问路
                    basic daily conversation travel life common words cet4 airport hotel restaurant shopping directions
                    """
            ),
            new VocabularyLevelProfile(
                    "intermediate",
                    "中级",
                    Set.of("cet6"),
                    """
                    中级 英语还可以 工作沟通 职场表达 商务入门 会议 面试 电话沟通 项目交流 六级
                    intermediate work communication business meeting interview phone call project discussion cet6
                    """
            ),
            new VocabularyLevelProfile(
                    "advanced",
                    "进阶",
                    Set.of("ky", "toefl", "gre", "ielts"),
                    """
                    进阶 高级 英语很好 英语较好 口语很好 缺少专业词汇 专业词汇积累 商务对接 国外用户 客户沟通
                    学术表达 留学 雅思 托福 考研 GRE IELTS TOEFL advanced professional vocabulary academic vocabulary
                    fluent high level business client communication study abroad lexical resource
                    """
            )
    );

    private final SpeakingScenarioRepository speakingScenarioRepository;
    private final VocabularyRepository vocabularyRepository;
    private final ObjectMapper objectMapper;
    private final Path indexPath;
    private final Map<String, String> speakingAliases;

    private volatile PetVectorIndex cachedIndex;

    public PetCompanionServiceImpl(
            SpeakingScenarioRepository speakingScenarioRepository,
            VocabularyRepository vocabularyRepository,
            ObjectMapper objectMapper,
            @Value("${app.pet.vector-index-file:var/pet-vector-index.json}") String indexFile
    ) {
        this.speakingScenarioRepository = speakingScenarioRepository;
        this.vocabularyRepository = vocabularyRepository;
        this.objectMapper = objectMapper;
        this.indexPath = Path.of(indexFile);
        this.speakingAliases = loadSpeakingAliases();
    }

    @Transactional(readOnly = true)
    public PetChatResponse chat(PetChatRequest request) {
        PetVectorIndex index = getOrCreateIndex();
        TextVector queryVector = vectorize(request.message(), index.idf());

        VectorSearchResult speaking = bestMatch(queryVector, index.speaking());
        VectorSearchResult vocabulary = bestMatch(queryVector, index.vocabulary());

        PetVectorEntry speakingEntry = speaking == null ? null : speaking.entry();
        PetVectorEntry vocabularyEntry = vocabulary == null
                ? fallbackVocabularyEntry(index)
                : vocabulary.entry();

        return new PetChatResponse(
                "我已从本地向量索引中匹配你的需求：" + request.message().trim(),
                speakingEntry == null
                        ? null
                        : new PetChatResponse.SpeakingRecommendation(
                                speakingEntry.key(),
                                speakingEntry.title(),
                                reasonWithScore("口语场景", speaking),
                                speakingEntry.route()
                        ),
                new PetChatResponse.VocabularyRecommendation(
                        vocabularyEntry.key(),
                        vocabularyEntry.title(),
                        reasonWithScore("词汇等级", vocabulary),
                        vocabularyEntry.route()
                ),
                contexts(index, speaking, vocabulary)
        );
    }

    @Transactional(readOnly = true)
    public PetVectorIndex rebuildIndex() {
        PetVectorIndex rebuilt = buildIndexFromDatabase();
        writeIndex(rebuilt);
        cachedIndex = rebuilt;
        return rebuilt;
    }

    private PetVectorIndex getOrCreateIndex() {
        PetVectorIndex current = cachedIndex;
        if (current != null) {
            return current;
        }

        synchronized (this) {
            if (cachedIndex != null) {
                return cachedIndex;
            }
            cachedIndex = Files.exists(indexPath) ? readIndexOrRebuild() : rebuildIndex();
            return cachedIndex;
        }
    }

    private PetVectorIndex buildIndexFromDatabase() {
        List<RawDocument> rawDocuments = new ArrayList<>();
        rawDocuments.addAll(loadSpeakingDocuments());
        rawDocuments.addAll(loadVocabularyDocuments());

        Map<String, Double> idf = calculateIdf(rawDocuments);
        List<PetVectorEntry> entries = rawDocuments.stream()
                .map(document -> toVectorEntry(document, idf))
                .toList();

        return new PetVectorIndex(
                INDEX_VERSION,
                Instant.now().toString(),
                indexPath.toAbsolutePath().toString(),
                entries.stream().filter(entry -> "speaking".equals(entry.type())).toList(),
                entries.stream().filter(entry -> "vocabulary".equals(entry.type())).toList(),
                idf
        );
    }

    private List<RawDocument> loadSpeakingDocuments() {
        return speakingScenarioRepository.findByActiveTrueOrderByTitleAsc().stream()
                .map(scenario -> new RawDocument(
                        "speaking",
                        scenario.getId(),
                        scenario.getTitle(),
                        "/speaking/" + encodePath(scenario.getId()),
                        String.join(" ",
                                nullToEmpty(scenario.getId()),
                                nullToEmpty(scenario.getTitle()),
                                nullToEmpty(scenario.getDescription()),
                        nullToEmpty(scenario.getSummary()),
                        repeat(nullToEmpty(scenario.getSummary()), 2),
                        nullToEmpty(scenario.getGoal()),
                        repeat(nullToEmpty(scenario.getGoal()), 2),
                        nullToEmpty(scenario.getKeywords()),
                        repeat(nullToEmpty(scenario.getKeywords()), 3),
                        nullToEmpty(speakingAliases.get(scenario.getId())),
                        repeat(nullToEmpty(speakingAliases.get(scenario.getId())), 3),
                        nullToEmpty(scenario.getSampleDialogue())
                        )
                ))
                .toList();
    }

    private List<RawDocument> loadVocabularyDocuments() {
        Map<String, StringBuilder> levelTexts = new LinkedHashMap<>();
        for (VocabularyLevelProfile level : VOCABULARY_LEVELS) {
            levelTexts.put(level.level(), new StringBuilder(level.level())
                    .append(' ')
                    .append(level.title())
                    .append(' ')
                    .append(String.join(" ", level.tags()))
                    .append(' ')
                    .append(repeat(level.profileText(), 6)));
        }

        for (Vocabulary vocabulary : vocabularyRepository.findAll()) {
            VocabularyLevelProfile level = levelFromTags(vocabulary.getTag());
            if (level == null) {
                continue;
            }

            levelTexts.get(level.level())
                    .append(' ')
                    .append(nullToEmpty(vocabulary.getTag()))
                    .append(' ')
                    .append(nullToEmpty(vocabulary.getWord()))
                    .append(' ')
                    .append(nullToEmpty(vocabulary.getTranslation()))
                    .append(' ')
                    .append(nullToEmpty(vocabulary.getBriefTranslation()))
                    .append(' ')
                    .append(nullToEmpty(vocabulary.getDefinition()));
        }

        return VOCABULARY_LEVELS.stream()
                .map(level -> new RawDocument(
                        "vocabulary",
                        level.level(),
                        level.title(),
                        "/vocabulary/practice/" + level.level(),
                        levelTexts.get(level.level()).toString()
                ))
                .toList();
    }

    private Map<String, Double> calculateIdf(List<RawDocument> documents) {
        Map<String, Integer> documentFrequency = new HashMap<>();
        for (RawDocument document : documents) {
            tokenize(document.text()).stream()
                    .collect(Collectors.toSet())
                    .forEach(token -> documentFrequency.merge(token, 1, Integer::sum));
        }

        int documentCount = Math.max(documents.size(), 1);
        Map<String, Double> idf = new HashMap<>();
        for (Map.Entry<String, Integer> entry : documentFrequency.entrySet()) {
            idf.put(entry.getKey(), Math.log((documentCount + 1.0) / (entry.getValue() + 1.0)) + 1.0);
        }
        return idf;
    }

    private PetVectorEntry toVectorEntry(RawDocument document, Map<String, Double> idf) {
        TextVector vector = vectorize(document.text(), idf);
        return new PetVectorEntry(
                document.type(),
                document.key(),
                document.title(),
                document.route(),
                preview(document.text()),
                vector.weights(),
                vector.norm()
        );
    }

    private TextVector vectorize(String text, Map<String, Double> idf) {
        Map<String, Long> termFrequency = tokenize(text).stream()
                .collect(Collectors.groupingBy(token -> token, Collectors.counting()));
        Map<String, Double> weights = new HashMap<>();
        for (Map.Entry<String, Long> entry : termFrequency.entrySet()) {
            double tf = 1.0 + Math.log(entry.getValue());
            double inverseDocumentFrequency = idf.getOrDefault(entry.getKey(), 1.0);
            weights.put(entry.getKey(), tf * inverseDocumentFrequency);
        }

        double norm = Math.sqrt(weights.values().stream()
                .mapToDouble(weight -> weight * weight)
                .sum());
        return new TextVector(weights, norm);
    }

    private VectorSearchResult bestMatch(TextVector queryVector, List<PetVectorEntry> entries) {
        return entries.stream()
                .map(entry -> new VectorSearchResult(entry, cosineSimilarity(queryVector, entry)))
                .filter(result -> result.score() > 0)
                .max(Comparator.comparingDouble(VectorSearchResult::score))
                .orElse(null);
    }

    private double cosineSimilarity(TextVector queryVector, PetVectorEntry entry) {
        if (queryVector.norm() == 0 || entry.norm() == 0) {
            return 0;
        }

        double dotProduct = queryVector.weights().entrySet().stream()
                .mapToDouble(term -> term.getValue() * entry.weights().getOrDefault(term.getKey(), 0.0))
                .sum();
        return dotProduct / (queryVector.norm() * entry.norm());
    }

    private List<String> contexts(
            PetVectorIndex index,
            VectorSearchResult speaking,
            VectorSearchResult vocabulary
    ) {
        List<String> result = new ArrayList<>();
        result.add("索引文件：" + index.indexPath());
        result.add("索引生成时间：" + index.generatedAt());
        if (speaking != null) {
            result.add("口语 Top1：" + speaking.entry().title() + "，相似度 " + formatScore(speaking.score()));
        }
        if (vocabulary != null) {
            result.add("词汇 Top1：" + vocabulary.entry().title() + "，相似度 " + formatScore(vocabulary.score()));
        }
        return result;
    }

    private String reasonWithScore(String label, VectorSearchResult result) {
        if (result == null) {
            return label + "采用本地向量索引兜底推荐。";
        }
        return label + "来自本地向量索引，相似度 " + formatScore(result.score()) + "。";
    }

    private PetVectorEntry fallbackVocabularyEntry(PetVectorIndex index) {
        return index.vocabulary().stream()
                .filter(entry -> "advanced".equals(entry.key()))
                .findFirst()
                .orElseGet(() -> index.vocabulary().isEmpty()
                        ? new PetVectorEntry("vocabulary", "advanced", "进阶", "/vocabulary/practice/advanced",
                                "", Map.of(), 0)
                        : index.vocabulary().get(index.vocabulary().size() - 1));
    }

    private VocabularyLevelProfile levelFromTags(String rawTags) {
        if (rawTags == null || rawTags.isBlank()) {
            return null;
        }

        String normalizedTags = rawTags.toLowerCase(Locale.ROOT);
        for (VocabularyLevelProfile level : VOCABULARY_LEVELS) {
            for (String tag : level.tags()) {
                if (normalizedTags.contains(tag)) {
                    return level;
                }
            }
        }
        return null;
    }

    private List<String> tokenize(String text) {
        String normalized = nullToEmpty(text).toLowerCase(Locale.ROOT);
        List<String> tokens = new ArrayList<>();

        Matcher latinMatcher = LATIN_TOKEN_PATTERN.matcher(normalized);
        while (latinMatcher.find()) {
            addToken(tokens, latinMatcher.group());
        }

        Matcher cjkMatcher = CJK_PATTERN.matcher(normalized);
        while (cjkMatcher.find()) {
            String chunk = cjkMatcher.group();
            for (int size = 2; size <= 4; size++) {
                for (int index = 0; index + size <= chunk.length(); index++) {
                    addToken(tokens, chunk.substring(index, index + size));
                }
            }
        }

        return tokens;
    }

    private void addToken(List<String> tokens, String token) {
        if (!token.isBlank() && !STOP_TOKENS.contains(token)) {
            tokens.add(token);
        }
    }

    private Map<String, String> loadSpeakingAliases() {
        try (var inputStream = getClass().getResourceAsStream("/pet-speaking-aliases.json")) {
            if (inputStream == null) {
                return Map.of();
            }
            return objectMapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read pet speaking aliases.", exception);
        }
    }

    private PetVectorIndex readIndex() {
        try {
            return objectMapper.readValue(indexPath.toFile(), PetVectorIndex.class);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read pet vector index: " + indexPath, exception);
        }
    }

    private PetVectorIndex readIndexOrRebuild() {
        try {
            PetVectorIndex index = readIndex();
            return INDEX_VERSION.equals(index.version()) ? index : rebuildIndex();
        } catch (IllegalStateException exception) {
            return rebuildIndex();
        }
    }

    private void writeIndex(PetVectorIndex index) {
        try {
            Path parent = indexPath.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(indexPath.toFile(), index);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to write pet vector index: " + indexPath, exception);
        }
    }

    private String preview(String text) {
        String normalized = nullToEmpty(text).replaceAll("\\s+", " ").trim();
        return normalized.length() <= 500 ? normalized : normalized.substring(0, 500);
    }

    private String repeat(String text, int times) {
        if (text.isBlank() || times <= 0) {
            return "";
        }
        return (text + " ").repeat(times);
    }

    private String formatScore(double score) {
        return String.format(Locale.ROOT, "%.3f", score);
    }

    private String encodePath(String value) {
        return UriUtils.encodePathSegment(value, StandardCharsets.UTF_8);
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private record VocabularyLevelProfile(String level, String title, Set<String> tags, String profileText) {
    }

    private record RawDocument(String type, String key, String title, String route, String text) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PetVectorIndex(
            String version,
            String generatedAt,
            String indexPath,
            List<PetVectorEntry> speaking,
            List<PetVectorEntry> vocabulary,
            Map<String, Double> idf
    ) {
    }

    public record PetVectorEntry(
            String type,
            String key,
            String title,
            String route,
            String sourcePreview,
            Map<String, Double> weights,
            double norm
    ) {
    }

    private record TextVector(Map<String, Double> weights, double norm) {
    }

    private record VectorSearchResult(PetVectorEntry entry, double score) {
    }
}
