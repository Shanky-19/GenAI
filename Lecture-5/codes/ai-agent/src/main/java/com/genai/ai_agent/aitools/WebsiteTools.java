package com.genai.ai_agent.aitools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

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

    private Path safePath(String path) {
        Path resolved = workspace.resolve(path).normalize();

        if(!resolved.startsWith(workspace)) {
            throw new IllegalArgumentException("Access to ");
        }
        return resolved;
    }

    @Tool(description = "Create a new directory inside the website workspace")
    public String createDirectory (
            @ToolParam(description = "Relative directory path, for example brewlab")
            String path
    ) {
        try {
            Path directory = safePath(path);
            Files.createDirectory(directory);
            return "Directory created successfully : " + path;
        }catch (IOException e) {
            return "Failed to create directory: " + e.getMessage();
        }
    }

    @Tool(description = """
                Creates or overwrites a text file inside website workspace.
                Use this to create HTML, CSS and Javascript files.
            """)
    private String writeFile(
            @ToolParam(description = "Releative file path, for example brewlab/index.html")
            String path,

            @ToolParam(description = "Complete content that should be written into the file")
            String content) {
        try {
            Path file = safePath(path);
            Files.createDirectories(file.getParent());
            Files.writeString(file, content, StandardCharsets.UTF_8);
            return "File written successfully: " + path;
        } catch (IOException e) {
            return "Failed to write file: " + e.getMessage();
        }
    }

    @Tool(description = "Lists all files and directories inside a website project")
    public String listFiles(
            @ToolParam(description = "Relative directory path for example brewlab")
            String path
    ) {
        try {
            Path directory = safePath(path);

            if(!Files.exists(directory)) {
                return "Directory does not exists: " + path;
            }

            try(var files = Files.walk(directory)) {
                return files
                        .filter(file -> !files.equals(directory))
                        .map(workspace::relativize)
                        .map(Path::toString)
                        .collect(Collectors.joining("\n"));
            }

        } catch (IOException e) {
            return "Failed to list files: " + e.getMessage();
        }
    }



}
