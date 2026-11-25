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
