package com.genai.customerservice.Zomato;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

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
      You are a customer-support executive for our
      Food ordering app named Zomato.

      Your job is to identify the customer's main
      problem and urgency. Answer them related to there query in 1 line.

      Use professional language. If user has an issue,
      use words like I understand your frustration,
      I am really sorry for your trouble etc.

      Do not answer any other question which is not
      related to Ordering Food query, refund query,
      order tracking status query or company policy query.
    """;

    public String chat(String message) {

//        String prompt = """
//            You are a customer support executive of our food delivery application
//            called Zomato.
//            Response to customer query professionally.
//
//            If user if furious, or angry or have any issue use words like I understand
//            your concern, or I am sorry you have to go through this and so on. Then solve
//            customer query and give me a response.
//
//            Always respond is not more than 1 line.
//
//            Do no respond to any other message which is not related to Ordering food query,
//            refund query, order tracking status query or company policy query.
//            Below is customer query :
//       """ + message;

        /**
         * Here, problem is user message and system message is same and
         * hence have same priority, user can ask ignore above and put his
         * irrelavant query in chat. So System prompt should be seperate from
         * user prompt.
         */

//        history.add(new UserMessage(prompt));
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