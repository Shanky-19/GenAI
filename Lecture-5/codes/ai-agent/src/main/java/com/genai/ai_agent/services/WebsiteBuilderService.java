package com.genai.ai_agent.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

public class WebsiteBuilderService {
    private ChatClient chatClient;

    private List<Message> history;

    public WebsiteBuilderService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
        this.history = new ArrayList<>();
    }

    private static final String SYSTEM_PROMPT = """
      
    """;

    public String chat(String message) {


        history.add(new UserMessage(message));
        String output = chatClient
                .prompt()
                .system(SYSTEM_PROMPT)
                .messages(history)
                .call()
                .content();
        history.add(new AssistantMessage(output));
        return output;
    }

    public void clearHistory() {
        history.clear();
    }
}
