# Surefire 测试报告汇总

## 总览

- 测试套件数：52
- 测试总数：315
- 通过数：315
- 失败数：0
- 错误数：0
- 跳过数：0
- 通过率：100%
- 总耗时：22.406 秒

## 各测试类结果

| 测试类 | Tests | Failures | Errors | Skipped | Time(s) |
| --- | ---: | ---: | ---: | ---: | ---: |
| AuthAndAdminIntegrationTest | 6 | 0 | 0 | 0 | 14.796 |
| controller.AdminPlaceholderControllerTest | 3 | 0 | 0 | 0 | 0.045 |
| controller.AdminUserControllerTest | 5 | 0 | 0 | 0 | 0.224 |
| controller.AuthControllerTest | 6 | 0 | 0 | 0 | 0.142 |
| controller.DashboardControllerTest | 3 | 0 | 0 | 0 | 0.051 |
| controller.GrammarControllerTest | 15 | 0 | 0 | 0 | 0.315 |
| controller.ProfileControllerTest | 5 | 0 | 0 | 0 | 0.095 |
| controller.ReviewControllerTest | 3 | 0 | 0 | 0 | 0.079 |
| controller.SpeakingControllerTest | 8 | 0 | 0 | 0 | 0.161 |
| controller.VocabularyControllerTest | 10 | 0 | 0 | 0 | 0.163 |
| DashboardProfileIntegrationTest | 1 | 0 | 0 | 0 | 0.356 |
| dto.DtoCoverageTest | 12 | 0 | 0 | 0 | 0.021 |
| entity.EntityCoverageTest | 9 | 0 | 0 | 0 | 0.009 |
| exception.GlobalExceptionHandlerTest | 4 | 0 | 0 | 0 | 0.051 |
| fsrs.FsrsRetentionTest | 2 | 0 | 0 | 0 | 0.006 |
| fsrs.FSRSTest | 23 | 0 | 0 | 0 | 0.073 |
| GrammarNotebookIntegrationTest | 5 | 0 | 0 | 0 | 1.476 |
| GrammarPracticeIntegrationTest | 1 | 0 | 0 | 0 | 0.267 |
| LearningPlanInitializationIntegrationTest | 3 | 0 | 0 | 0 | 0.524 |
| LearningProgressOutboxIntegrationTest | 1 | 0 | 0 | 0 | 0.077 |
| repository.GrammarRepositoryTest | 3 | 0 | 0 | 0 | 0.746 |
| repository.SpeakingRepositoryTest | 3 | 0 | 0 | 0 | 0.058 |
| repository.UserPracticeUpsertRepositoryTest | 1 | 0 | 0 | 0 | 0.024 |
| repository.UserRepositoryTest | 2 | 0 | 0 | 0 | 0.038 |
| repository.VocabularyRepositoryTest | 3 | 0 | 0 | 0 | 0.071 |
| security.SecurityCoverageTest | 9 | 0 | 0 | 0 | 0.103 |
| service.agent.SjtuDeepSeekSpeakingAgentClientTest | 3 | 0 | 0 | 0 | 0.035 |
| service.agent.SjtuGrammarTutorAgentClientTest | 1 | 0 | 0 | 0 | 0.011 |
| service.AuthServiceImplTest | 6 | 0 | 0 | 0 | 0.039 |
| service.DashboardServiceImplTest | 12 | 0 | 0 | 0 | 0.257 |
| service.GrammarServiceImplTest | 28 | 0 | 0 | 0 | 0.129 |
| service.LearningPlanServiceImplTest | 15 | 0 | 0 | 0 | 0.366 |
| service.LearningProgressOutboxProcessorTest | 3 | 0 | 0 | 0 | 0.008 |
| service.LearningProgressOutboxServiceTest | 11 | 0 | 0 | 0 | 0.045 |
| service.PetCompanionServiceImplTest | 9 | 0 | 0 | 0 | 0.237 |
| service.ReviewServiceTest | 14 | 0 | 0 | 0 | 0.031 |
| service.SpeakingAgentAudioSynthesisServiceTest | 1 | 0 | 0 | 0 | 0.020 |
| service.SpeakingAudioStorageServiceTest | 2 | 0 | 0 | 0 | 0.038 |
| service.SpeakingServiceImplTest | 15 | 0 | 0 | 0 | 0.076 |
| service.speech.EnglishSpeechTextTest | 6 | 0 | 0 | 0 | 0.002 |
| service.speech.OpenSpeakingMetricsTest | 3 | 0 | 0 | 0 | 0.001 |
| service.speech.xfyun.XfyunFileAsrServiceTest | 1 | 0 | 0 | 0 | 0.003 |
| service.speech.xfyun.XfyunIseClientTest | 3 | 0 | 0 | 0 | 0.009 |
| service.speech.xfyun.XfyunIseResultParserTest | 2 | 0 | 0 | 0 | 0.048 |
| service.speech.xfyun.XfyunOnlineTtsClientTest | 2 | 0 | 0 | 0 | 0.015 |
| service.speech.xfyun.XfyunSignatureServiceTest | 2 | 0 | 0 | 0 | 0.002 |
| service.speech.xfyun.XfyunTranscriptionParserTest | 1 | 0 | 0 | 0 | 0.003 |
| service.VocabularyServiceImplTest | 20 | 0 | 0 | 0 | 0.045 |
| SpeakingIntegrationTest | 4 | 0 | 0 | 0 | 0.643 |
| UserGrammarbookSchemaIntegrationTest | 1 | 0 | 0 | 0 | 0.030 |
| VocabularyPracticeDifficultyIntegrationTest | 3 | 0 | 0 | 0 | 0.112 |
| VocabularyPracticeIntegrationTest | 1 | 0 | 0 | 0 | 0.230 |

## 结论

本次 Surefire 共汇总 52 个测试套件，运行 315 个测试用例，失败数、错误数和跳过数均为 0，整体通过率为 100%。
