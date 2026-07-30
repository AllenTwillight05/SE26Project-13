package com.englishlearningcopilot.backend.repository;

import com.englishlearningcopilot.backend.entity.SpeakingScenario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpeakingScenarioRepository extends JpaRepository<SpeakingScenario, String> {

    List<SpeakingScenario> findByActiveTrueOrderByTitleAsc();

    List<SpeakingScenario> findByActiveTrueAndIdStartingWithOrderByIdAsc(String idPrefix);
}
