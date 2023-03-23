package com.example.test.mainHandler.MessageHandler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import pro.sky.whiskerspawstailtelegrambot.service.AdoptiveParentService;
import pro.sky.whiskerspawstailtelegrambot.textAndButtonsAndKeyboard.AllText;
import pro.sky.whiskerspawstailtelegrambot.textAndButtonsAndKeyboard.ConfigKeyboard;
import pro.sky.whiskerspawstailtelegrambot.util.FormReplyMessages;
import pro.sky.whiskerspawstailtelegrambot.util.StateAdoptiveParent;

/**
 * обработчик кнопки отмена
 */
public class ButtonCancelHandler {

  /**
   * реакция на кнопку отмена, изменение любого статуса пользователя на FREE
   *
   * @return
   */
  public SendMessage clickCancel(Message message, AdoptiveParentService adoptiveParentService,
      String chatId) {

    SendMessage sendMessage = new SendMessage();
    FormReplyMessages formReplyMessages = new FormReplyMessages();
    ConfigKeyboard configKeyboard = new ConfigKeyboard();

    adoptiveParentService.updateStateAdoptiveParentByChatId(Long.parseLong(chatId),
        StateAdoptiveParent.FREE);

    return formReplyMessages.replyMessage(message,
        AllText.CANCEL_RETURN_MAIN_MENU_TEXT,
        configKeyboard.initKeyboardOnClickStart());

  }

  /**
   * реакция на кнопку отмена - возврат в главное меню, удаление сущности из бд
   * @return
   */
  public SendMessage clickCancelInRegistration(Message message,
      AdoptiveParentService adoptiveParentService, String chatId) {

    FormReplyMessages formReplyMessages = new FormReplyMessages();
    ConfigKeyboard configKeyboard = new ConfigKeyboard();

    adoptiveParentService
        .deleteAdoptiveParentByID(
            adoptiveParentService.getAdoptiveParentByChatId(
                Long.parseLong(chatId)).getId());

    return formReplyMessages.replyMessage(message,
        "Регистрация",
        configKeyboard.formReplyKeyboardInOneRow("Регистрация"));

  }


}
