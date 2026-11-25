package com.jmjava.slm.kiln;

import java.nio.file.Path;

/**
 * Halo smoke: load one catalogued trained/baseline GGUF via Kiln and stream a reply.
 *
 * <pre>
 * ./gradlew run --args='embabel-base "Name three Embabel annotations"'
 * </pre>
 */
public final class SmokeMain {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: SmokeMain <model-id> [prompt]");
            System.err.println("Catalog: config/models.json (see models.example.json)");
            System.exit(2);
        }
        String modelId = args[0];
        String prompt = args.length >= 2 ? args[1] : "Reply with one short sentence confirming you loaded.";

        Path catalogPath = Path.of(System.getProperty("slm.kiln.catalog", "config/models.json"));
        if (!catalogPath.toFile().isFile()) {
            catalogPath = Path.of("config/models.example.json");
        }

        ModelCatalog catalog = ModelCatalog.load(catalogPath);
        ModelSpec spec = catalog.find(modelId).orElseThrow(() ->
                new IllegalArgumentException("Unknown model id '" + modelId + "'. Known: " + catalog.all().keySet()));

        System.out.println("host_class=" + catalog.hostClass()
                + " backend=" + catalog.backend()
                + " model=" + spec.id()
                + " gguf=" + spec.gguf()
                + " source=" + spec.sourceRepo());
        System.out.println("---");

        try (KilnModelRunner runner = new KilnModelRunner(spec)) {
            var result = runner.chat(prompt, System.out::print);
            System.out.println();
            System.out.println("---");
            System.out.println("prompt_tokens=" + result.promptTokens()
                    + " generated_tokens=" + result.generatedTokens());
        }
    }
}
