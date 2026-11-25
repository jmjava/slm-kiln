# slm-kiln

Run **your trained SLM-family models** through [Kiln](https://github.com/liberation-data/kiln) — in-process on the JVM/GPU.

| Sibling | Role |
| --- | --- |
| [jmjava/slm-setup](https://github.com/jmjava/slm-setup) | MCP bridge → Ollama for Cursor / Copilot / Claude |
| [jmjava/embabel-slm](https://github.com/jmjava/embabel-slm) | Embabel model program (train / evaluate) |
| [jmjava/drivine-slm](https://github.com/jmjava/drivine-slm) | Drivine corpus + Halo AMD LoRA |
| **jmjava/slm-kiln** (this repo) | **Kiln runtime** for GGUFs those training tracks produce |

This repo is in the SLM family because of **which weights it loads**, not because it trains. Training stays in `embabel-slm` / `drivine-slm`. Inference here uses real `org.liberationdata.kiln.llama.LlamaModel` — no Ollama, no stub.

Site: [kiln4j.org](https://kiln4j.org/).

## Quick start (Halo)

```bash
./scripts/fetch-kiln.sh
(cd vendor/kiln && native/build.sh vulkan)   # needs JDK 25 + Vulkan SDK
cp config/models.example.json config/models.json
# put exported GGUFs in models/  (gitignored)
./gradlew run --args='embabel-base "Write a one-line Embabel @Action example"'
```

## Layout

| Path | Purpose |
| --- | --- |
| `config/models.json` | Catalog: model id → GGUF path (from your training export) |
| `models/` | GGUF artifacts (not committed) |
| `KilnModelRunner` | Thin wrapper around Kiln `LlamaModel` |
| `docs/from-training.md` | How LoRA/baseline becomes a GGUF entry |
| `docs/halo.md` | Halo Vulkan operator notes |

## Standing rules

- Call Kiln; do not reimplement llama.cpp bindings.
- Do not commit weights or LAN addresses.
- Do not put training loops here.
- MCP delivery of these models (if ever) goes through a later bridge decision — not by folding Kiln into `slm-setup` on day one.
