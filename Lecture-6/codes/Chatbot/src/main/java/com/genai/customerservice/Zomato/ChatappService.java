package com.genai.customerservice.Zomato;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatappService {

    private ChatClient chatClient;

    private List<Message> history;

    public ChatappService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
        this.history = new ArrayList<>();
    }

    private static final String SYSTEM_PROMPT = """
        You are a funny AI chatbot. You reply everything sarcastically.
        Always answer in 1-2 lines at max.
    """;

    public Flux<String> chat(String message) {

//        history.add(new UserMessage(message)); // we have disabled history becoz history is List<Msg> and stream is Flux<String>
        /*
            So to pass history, what we will do is, we will store the history in a buffer
            and that buffer we will pass.
         */
        StringBuilder buffer = new StringBuilder();

//        String output = chatClient
        Flux<String> output = chatClient
                                    .prompt()
                                    .system(SYSTEM_PROMPT)
                    //                .messages(history)
                                    .user(message)
                    //                .call() // -> This response is not in streams, gives entire in one go.
                                    .stream() // -> To stream the response, and java handle streams using Flux
                                    .content()
                                    .doOnNext(buffer::append)
                                    .doOnComplete(() -> {
                                        history.add(new AssistantMessage(buffer.toString()));
                                    });
//        history.add(new AssistantMessage(output));
        return output;
    }

    public void clearHistory() {
        history.clear();
    }
}