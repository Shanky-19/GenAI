package com.genai.customerservice.Zomato;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class ChatappController {

    private ChatappService chatappService;

    // constuctor injection
    public ChatappController(ChatappService chatappService) {
        this.chatappService = chatappService;
    }

    @PostMapping("/chat")
    public Flux<String> chat(@RequestBody String message) {
        return chatappService.chat(message);
    }

    @DeleteMapping
    public void clearChat() {
        chatappService.clearHistory();
    }
}