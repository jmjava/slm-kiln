# Halo / GEEKOM 395 — run trained models in Kiln

Paper host: `host_class=halo-395` (Ryzen AI Max+ 395 / Radeon 8060S). Kiln’s own benches use this class with **Vulkan**.

## Once per machine

```bash
# JDK 25+, CMake, Ninja, Vulkan headers (see kiln4j.org)
./scripts/fetch-kiln.sh
(cd vendor/kiln && native/build.sh vulkan)
cp config/models.example.json config/models.json
# place GGUFs under models/ — see docs/from-training.md
```

## Smoke

```bash
./gradlew printKilnPath
./gradlew run --args='embabel-base "Reply in one sentence."'
```

Record: Kiln backend (`vulkan` / later `hip`), GGUF id, tok/s if measured, and that the weights came from which training repo + pin.

## vs Ollama

| | Ollama (`slm-setup`) | Kiln (`slm-kiln`) |
| --- | --- | --- |
| Process | Separate daemon + HTTP | In-JVM FFM call |
| Ideal for | IDE MCP delegation | Embabel / JVM apps using **your** GGUFs |
| Same weights? | Yes — export once, point both at the GGUF family | Yes |

Do not average Halo Kiln timings with downstairs NVIDIA Ollama rows.

## Smoke 2026-11-25

Kiln `26015ae`, backend `vulkan` (llama engine only). Model id `embabel-base`.

| | |
| --- | --- |
| Ollama tag | `qwen3.5:9b` |
| Ollama digest | `56671c2ab9385f9cfcb404638e32cd62d88e3501d44822208363c010179a3c90` |
| Quant | `Q4_K_M` (architecture `qwen35`, 9.0B) |
| Weights | Unmodified official Ollama blob (baseline, untrained). Pinned llama.cpp accepted `qwen35`; no fallback blob. |
| Kiln | **30.3 tok/s** — 373 generated tokens in 12.318 s. Wall clock of `LlamaModel.chat` (includes prompt eval of 20 tokens). Vulkan, 33/33 layers on Radeon 8060S (RADV). |
| Ollama | **19.1 tok/s** — 96 eval tokens in 5.033 s (`/api/generate` eval rate, decode only). The daemon scheduled the CPU backend (`100% CPU`), not Vulkan. |

The system Ollama model store is not readable by the Kiln process (permission denied). This run used a byte-identical official pull of the same tag.

Contention: during the Kiln sample, another live session also had `qwen3.5:9b` and `devstral-small-2` resident on CPU. Do not treat these rates as an idle-GPU comparison.
