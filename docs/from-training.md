# From SLM training → Kiln GGUF

`slm-kiln` does **not** train. It runs artifacts you already produced.

## Pipeline

```text
embabel-slm / drivine-slm          export / merge              slm-kiln
  PEFT/TRL LoRA  ──►  merge into base  ──►  GGUF quant  ──►  models/*.gguf
                                                              config/models.json
                                                              Kiln LlamaModel
```

1. Train or select a baseline in `embabel-slm` or `drivine-slm` (Halo AMD recipes stay there).
2. Merge the adapter into the base weights (or keep a base-only GGUF for M1).
3. Quantize to GGUF (`Q4_K_M` / `Q8_0` as appropriate) with the same tooling you use for Ollama exports.
4. Copy the file into `models/` and register it in `config/models.json` (start from `models.example.json`).
5. On Halo: build Kiln natives (`vendor/kiln/native/build.sh vulkan`), then:

```bash
./gradlew run --args='embabel-lora-merged "Your Embabel coding prompt"'
```

## What stays out

| Concern | Repo |
| --- | --- |
| MCP bridge to Cursor | `slm-setup` (Ollama today) |
| Task freeze / paper harness | `embabel-slm` |
| Drivine corpus + LoRA scripts | `drivine-slm` |
| In-process JVM inference of **your** GGUFs | **this repo** via [kiln](https://github.com/liberation-data/kiln) |

Later: optional Embabel `LocalModelSource` / OpenAI-compatible façade so agents and MCP can consume the same GGUFs without duplicating training.
