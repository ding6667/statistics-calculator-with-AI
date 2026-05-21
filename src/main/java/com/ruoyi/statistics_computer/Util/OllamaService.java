package com.ruoyi.statistics_computer.Util;


import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import javafx.concurrent.Task;

import java.util.List;

public class OllamaService {
    private static final OllamaService instance = new OllamaService();
    private static final String systemPrompt = "你是一个文本处理助手。请从用户输入的字符串中提取所有数字（包括整数、小数、负数），并按原顺序用英文逗号连接输出。输入中的任何非数字字符（空格、标点、字母、汉字等）均视为分隔符。连续多个分隔符只算一个，忽略空值。示例 1：输入 '1,2|5、7$6' → 输出 '1,2,5,7,6'\n" +
            "示例 2：输入 'a1.5,b-2c' → 输出 '1.5,-2'.如果文本中没有数字，则输出‘无数字’。不要询问或输出任何其他内容。";

    private final ChatModel model;

    public OllamaService() {
        this.model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("qwen2.5:7b")
                .temperature(0.5)
                .timeout(java.time.Duration.ofSeconds(60))
                .build();
    }
    public static OllamaService  getInstance(){
        return instance;
    }

    public Task<String> generateAsync(String userPrompt){
        return generateAsync(systemPrompt,userPrompt);
    }
    public Task<String> generateAsync(String sysPrompt ,String prompt) {

        return new Task<>() {
            @Override
            protected String call() throws Exception {
                List<ChatMessage> messages = List.of(SystemMessage.from(sysPrompt), UserMessage.from("用户输入:"+prompt));
                // 新版方法名是 chat
                return model.chat(messages).aiMessage().text();
            }
        };
    }
}