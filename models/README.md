# Trained model artifacts (gitignored weights)

This directory holds **GGUF files produced by the SLM training track**, not by Kiln.

| Comes from | Goes here |
| --- | --- |
| [embabel-slm](https://github.com/jmjava/embabel-slm) baseline / merged LoRA | `embabel-base.gguf`, `embabel-lora-merged.gguf` |
| [drivine-slm](https://github.com/jmjava/drivine-slm) AMD LoRA export | `drivine-lora-merged.gguf` |

Kiln loads paths listed in `config/models.json` (copy from `config/models.example.json`).
Weights are **not** committed. See [docs/from-training.md](../docs/from-training.md).
