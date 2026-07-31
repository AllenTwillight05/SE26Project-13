# JaCoCo 覆盖率报告汇总

## 总体覆盖率

- 指令覆盖率：97.20%（未覆盖 264 / 总计 9425）
- 分支覆盖率：93.04%（未覆盖 38 / 总计 546）
- 圈复杂度覆盖率：92.69%（未覆盖 64 / 总计 876）
- 行覆盖率：97.56%（未覆盖 49 / 总计 2011）
- 方法覆盖率：95.48%（未覆盖 27 / 总计 598）
- 类覆盖率：99.17%（未覆盖 1 / 总计 120）

## 分包覆盖率

| 包 | 指令覆盖率 | 分支覆盖率 | 行覆盖率 | 方法覆盖率 | 类覆盖率 |
| --- | ---: | ---: | ---: | ---: | ---: |
| com.englishlearningcopilot.backend.controller | 92.50% | 100.00% | 91.67% | 94.55% | 100.00% |
| com.englishlearningcopilot.backend.dto | 100.00% | 95.65% | 100.00% | 100.00% | 100.00% |
| com.englishlearningcopilot.backend.entity | 97.06% | 100.00% | 97.34% | 96.00% | 100.00% |
| com.englishlearningcopilot.backend.exception | 100.00% | n/a | 100.00% | 100.00% | 100.00% |
| com.englishlearningcopilot.backend.fsrs | 100.00% | 94.44% | 100.00% | 100.00% | 100.00% |
| com.englishlearningcopilot.backend.security | 100.00% | 93.75% | 100.00% | 100.00% | 100.00% |
| com.englishlearningcopilot.backend.service.impl | 96.06% | 91.76% | 97.12% | 92.20% | 94.12% |
| com.englishlearningcopilot.backend.service.speech | 100.00% | 96.30% | 100.00% | 100.00% | 100.00% |

## 结论

本次 JaCoCo 报告显示整体指令覆盖率为 97.20%，整体分支覆盖率为 93.04%。核心业务包 service.impl 的指令覆盖率和分支覆盖率均已超过 90%，覆盖情况满足当前测试目标。
