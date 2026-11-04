package com.genai.ai_agent.aitools;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class WebsiteTools {

    private final Path workspace =
            Path.of("generated-sites").toAbsolutePath().normalize();

    public WebsiteTools() {
        try{
            Files.createDirectories(workspace);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not find website workspace");
        }
    }

}
