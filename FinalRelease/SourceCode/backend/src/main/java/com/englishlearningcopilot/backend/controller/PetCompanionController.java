package com.englishlearningcopilot.backend.controller;

import com.englishlearningcopilot.backend.dto.PetChatRequest;
import com.englishlearningcopilot.backend.dto.PetChatResponse;
import com.englishlearningcopilot.backend.service.impl.PetCompanionServiceImpl;
import com.englishlearningcopilot.backend.service.impl.PetCompanionServiceImpl.PetVectorIndex;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pet")
public class PetCompanionController {

    private final PetCompanionServiceImpl petCompanionService;

    public PetCompanionController(PetCompanionServiceImpl petCompanionService) {
        this.petCompanionService = petCompanionService;
    }

    @PostMapping("/chat")
    public PetChatResponse chat(@Valid @RequestBody PetChatRequest request) {
        return petCompanionService.chat(request);
    }

    @PostMapping("/vector-index/rebuild")
    public Map<String, Object> rebuildVectorIndex() {
        PetVectorIndex index = petCompanionService.rebuildIndex();
        return Map.of(
                "indexPath", index.indexPath(),
                "generatedAt", index.generatedAt(),
                "speakingCount", index.speaking().size(),
                "vocabularyCount", index.vocabulary().size()
        );
    }
}
