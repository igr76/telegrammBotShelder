package com.example.test.mainHandler.MessageHandler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
//@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetBaseInfoFromUpdate {

  String chatId;
  Message message;
  String textMessage;
  boolean isCallbackQuery;
  CallbackQuery callbackQuery;

  public GetBaseInfoFromUpdate(Update update) {
    init(update);
  }

  public void init(Update update) {

    if (update.getCallbackQuery() != null) {
      callbackQuery = update.getCallbackQuery();
//      message = callbackQuery.getMessage();
      textMessage = callbackQuery.getData();
      chatId = callbackQuery.getMessage().getChatId().toString();
      isCallbackQuery = true;

    } else {
      message = update.getMessage();
      textMessage = message.getText();
      chatId = message.getChatId().toString();
      isCallbackQuery = false;
    }

  }


}
