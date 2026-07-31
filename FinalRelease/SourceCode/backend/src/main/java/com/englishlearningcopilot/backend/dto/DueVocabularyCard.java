package com.englishlearningcopilot.backend.dto;

import com.englishlearningcopilot.backend.entity.Vocabulary;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;

public record DueVocabularyCard(
        Long id,
        String word,
        String phonetic,
        String definition,
        String briefTranslation,
        String translation,
        String collins,
        String oxford,
        String tag,
        String bnc,
        String frq,
        String exchange,
        @JsonProperty("uk_audio") String ukAudio,
        @JsonProperty("us_audio") String usAudio,
        List<String> chineseOptions,
        List<String> englishOptions
) {

    public static DueVocabularyCard from(Vocabulary vocabulary) {
        return new DueVocabularyCard(
                vocabulary.getId(),
                vocabulary.getWord(),
                vocabulary.getPhonetic(),
                vocabulary.getDefinition(),
                nullToEmpty(vocabulary.getBriefTranslation()),
                vocabulary.getTranslation(),
                vocabulary.getCollins(),
                vocabulary.getOxford(),
                vocabulary.getTag(),
                vocabulary.getBnc(),
                vocabulary.getFrq(),
                vocabulary.getExchange(),
                vocabulary.getUkAudio(),
                vocabulary.getUsAudio(),
                nullToEmptyList(vocabulary.getChineseOptions()),
                nullToEmptyList(vocabulary.getEnglishOptions())
        );
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private static List<String> nullToEmptyList(List<String> value) {
        return value == null ? Collections.emptyList() : value;
    }
}
