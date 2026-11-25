package com.jmjava.slm.kiln;

import org.liberationdata.kiln.llama.LlamaModel;

import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Runs an SLM-family GGUF through Kiln's {@link LlamaModel} (in-process, no Ollama).
 */
public final class KilnModelRunner implements AutoCloseable {
    private final ModelSpec spec;
    private final LlamaModel model;

    public KilnModelRunner(ModelSpec spec) {
        this.spec = Objects.requireNonNull(spec, "spec");
        if (!Files.isRegularFile(spec.gguf())) {
            throw new IllegalStateException(
                    "GGUF missing for model '" + spec.id() + "': " + spec.gguf()
                            + " — export from embabel-slm / drivine-slm first"
            );
        }
        this.model = new LlamaModel(spec.gguf().toString(), spec.gpuLayers(), spec.context());
    }

    public ModelSpec spec() {
        return spec;
    }

    public LlamaModel.Result chat(String userPrompt, Consumer<String> onPiece) {
        return model.chat(
                List.of(LlamaModel.Message.user(userPrompt)),
                LlamaModel.Options.DEFAULT,
                onPiece
        );
    }

    public LlamaModel.Result chat(String userPrompt) {
        return chat(userPrompt, null);
    }

    /** OpenAI-style JSON chat (tools supported by Kiln / llama.cpp templates). */
    public String chatJson(String requestJson, Consumer<String> onContent) {
        return model.chatJson(requestJson, onContent);
    }

    @Override
    public void close() {
        model.close();
    }
}
