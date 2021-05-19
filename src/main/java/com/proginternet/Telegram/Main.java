package com.proginternet.Telegram;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi api = new TelegramBotsApi();
        try {
            api.registerBot(new MyBot());
            System.out.println("MyBot Online");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
