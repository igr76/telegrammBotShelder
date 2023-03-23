package com.example.test;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import pro.sky.whiskerspawstailtelegrambot.mainHandler.MainHandler;

/**
 * наш бот
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class TelegramBotUpdatesListener extends SpringWebhookBot {


  String webHookPath;
  String botUserName;
  String botToken;

  @Qualifier("MessageHandler")
  @Autowired
  MainHandler messageHandler;

  public TelegramBotUpdatesListener(SetWebhook webhook) {
    super(webhook);
  }


  /**
   * Сейчас метод просто отвечает на любое сообщение
   *
   * @param update уведомление от пользвателя
   * @return ответ пользователю
   */
  @Override
  public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
    try {
      execute(messageHandler.handler(update));
    } catch (TelegramApiException e) {
      e.printStackTrace();
    }
    return null;
  }


  @Override
  public String getBotPath() {
    return webHookPath;
  }

  @Override
  public String getBotUsername() {
    return botUserName;
  }

  @Override
  public String getBotToken() {
    return botToken;
  }
}