package com.genai.ai_agent.aitools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class CalculatorTool {

    @Tool(description = """
                Performs arithmetic calculations.
                Supported operations : add, subtract, multiply, divide, mod, power
            """)
    public double calculate(
                @ToolParam(description = "Operation : add, subtract, multiply, divide and power")
                String operation,
                @ToolParam(description = "First number")
                double a,
                @ToolParam(description = "Second number")
                double b) {

        System.out.println("Calculator Tool Called !!");
        switch (operation) {
            case "add" -> {
                return a + b;
            }
            case "subtract" -> {
                return a - b;
            }
            case "divide" -> {
                if (b == 0) {
                    throw new IllegalArgumentException("Cannot divide by 0");
                }
                return a / b;
            }
            case "multiply" -> {
                return a * b;
            }
            case "mod" -> {
                if (b == 0) {
                    throw new IllegalArgumentException("Cannot calculate mod by 0");
                }
                return a % b;
            }
            case "power" -> {
                return Math.pow(a, b);
            }
            default -> throw new IllegalArgumentException("Unsupported Operation " + operation);
        }
    }
}
