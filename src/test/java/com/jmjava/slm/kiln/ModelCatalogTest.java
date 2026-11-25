package com.jmjava.slm.kiln;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelCatalogTest {
    @Test
    void loadsExampleCatalog() throws Exception {
        Path example = Path.of("config/models.example.json");
        ModelCatalog catalog = ModelCatalog.load(example);
        assertEquals("halo-395", catalog.hostClass());
        assertEquals("vulkan", catalog.backend());
        assertTrue(catalog.find("embabel-base").isPresent());
        assertTrue(catalog.find("drivine-lora-merged").isPresent());
        assertEquals("jmjava/embabel-slm", catalog.find("embabel-base").orElseThrow().sourceRepo());
    }
}
