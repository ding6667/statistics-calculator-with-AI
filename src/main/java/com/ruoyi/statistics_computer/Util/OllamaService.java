package com.ruoyi.statistics_computer.Util;


import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import javafx.concurrent.Task;

public class OllamaService {

    private final ChatModel model;

    public OllamaService() {
        this.model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("qwen2.5:7b")
                .temperature(0.7)
                .timeout(java.time.Duration.ofSeconds(60))
                .build();
    }

    public Task<String> generateAsync(String prompt) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                // 新版方法名是 chat
                return model.chat(prompt);
            }
        };
    }
}