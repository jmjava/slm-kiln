package com.jmjava.slm.kiln;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** Reads config/models.json — ids point at GGUFs from embabel-slm / drivine-slm. */
public final class ModelCatalog {
    private final String hostClass;
    private final String backend;
    private final Map<String, ModelSpec> byId;

    private ModelCatalog(String hostClass, String backend, Map<String, ModelSpec> byId) {
        this.hostClass = hostClass;
        this.backend = backend;
        this.byId = Map.copyOf(byId);
    }

    public String hostClass() {
        return hostClass;
    }

    public String backend() {
        return backend;
    }

    public Optional<ModelSpec> find(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    public Map<String, ModelSpec> all() {
        return byId;
    }

    public static ModelCatalog load(Path jsonPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(Files.readString(jsonPath));
        String host = text(root, "host_class", "halo-395");
        String backend = text(root, "backend", "vulkan");
        Map<String, ModelSpec> map = new LinkedHashMap<>();
        Path base = jsonPath.getParent() == null ? Path.of(".") : jsonPath.getParent().getParent();
        if (base == null) {
            base = Path.of(".");
        }
        for (JsonNode node : root.withArray("models")) {
            String id = required(node, "id");
            Path gguf = Path.of(required(node, "gguf"));
            if (!gguf.isAbsolute()) {
                gguf = base.resolve(gguf).normalize();
            }
            map.put(id, new ModelSpec(
                    id,
                    text(node, "source_repo", ""),
                    text(node, "note", ""),
                    gguf,
                    node.path("context").asInt(8192),
                    node.path("gpu_layers").asInt(-1)
            ));
        }
        return new ModelCatalog(host, backend, map);
    }

    private static String text(JsonNode node, String field, String fallback) {
        JsonNode v = node.get(field);
        return v == null || v.isNull() ? fallback : v.asText();
    }

    private static String required(JsonNode node, String field) {
        JsonNode v = node.get(field);
        if (v == null || v.isNull() || v.asText().isBlank()) {
            throw new IllegalArgumentException("models.json missing field: " + field);
        }
        return v.asText();
    }
}
