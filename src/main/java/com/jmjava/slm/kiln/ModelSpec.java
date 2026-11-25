package com.jmjava.slm.kiln;

import java.nio.file.Path;

/** One trained (or baseline) GGUF the SLM family wants Kiln to run. */
public record ModelSpec(
        String id,
        String sourceRepo,
        String note,
        Path gguf,
        int context,
        int gpuLayers
) {
}
