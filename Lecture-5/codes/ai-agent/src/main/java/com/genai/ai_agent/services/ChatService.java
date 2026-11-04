package com.genai.ai_agent.services;

import com.genai.ai_agent.aitools.CalculatorTool;
import com.genai.ai_agent.aitools.CurrencyExchangeTool;
import com.genai.ai_agent.aitools.WeatherTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private ChatClient chatClient;
    private CalculatorTool calculatorTool;
    private WeatherTool weatherTool;
    private CurrencyExchangeTool currencyExchangeTool;

    private List<Message> history;

    public ChatService(ChatClient.Builder builder,
                       CalculatorTool calculatorTool,
                       WeatherTool weatherTool,
                       CurrencyExchangeTool currencyExchangeTool) {
        this.chatClient = builder.build();
        this.history = new ArrayList<>();
        this.calculatorTool = calculatorTool;
        this.weatherTool = weatherTool;
        this.currencyExchangeTool = currencyExchangeTool;
    }

    private static final String SYSTEM_PROMPT = """
      You are a helpful AI assistant with access to external tools.
      For these rules : 
      1. For arithmetic calculations, ALWAYS use the calculator tool.
      2. For current weather, ALWAYS use Weather tool.
      3. For currency conversion and exchange rates, ALWAYS use the Currency Exchange Tool.
      4. You may call multiple tools when solving a multi-step requests.
      5. After receiving tool results, explain the answer naturally.
      6. Never invent current weather and exchange-rate information.
    """;

    public String chat(String message) {


        history.add(new UserMessage(message));
        String output = chatClient
                .prompt()
                .system(SYSTEM_PROMPT)
                .messages(history)
                .tools(calculatorTool, weatherTool, currencyExchangeTool)
                .call()
                .content();
        history.add(new AssistantMessage(output));
        return output;
    }

    public void clearHistory() {
        history.clear();
    }
}
