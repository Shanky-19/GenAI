package com.genai.ai_agent.controllers;

import com.genai.ai_agent.services.ChatService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class WebsiteBuilderController {
    private ChatService chatappService;

    // constuctor injection
    public ChatController(ChatService chatappService) {
        this.chatappService = chatappService;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String message) {
        return chatappService.chat(message);
    }

    @DeleteMapping
    public void clearChat() {
        chatappService.clearHistory();
    }
}
