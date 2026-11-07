package com.genai.ai_agent.controllers;

import com.genai.ai_agent.services.ChatService;
import com.genai.ai_agent.services.WebsiteBuilderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class WebsiteBuilderController {
    private WebsiteBuilderService websiteBuilderService;

    // constuctor injection
    public WebsiteBuilderController(WebsiteBuilderService websiteBuilderService) {
        this.websiteBuilderService = websiteBuilderService;
    }

    @PostMapping("/generate")
    public String generate(@RequestBody String message) {
        return websiteBuilderService.generate(message);
    }

    @DeleteMapping
    public void clearChat() {
        websiteBuilderService.clearHistory();
    }
}
