package com.example.test.mainHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import pro.sky.whiskerspawstailtelegrambot.mainHandler.reportHandler.ReportAddHandler;
import pro.sky.whiskerspawstailtelegrambot.record.AdoptiveParentRecord;
import pro.sky.whiskerspawstailtelegrambot.service.AdoptiveParentService;
import pro.sky.whiskerspawstailtelegrambot.service.ShelterService;
import pro.sky.whiskerspawstailtelegrambot.service.VolunteerService;
import pro.sky.whiskerspawstailtelegrambot.textAndButtonsAndKeyboard.AllText;
import pro.sky.whiskerspawstailtelegrambot.textAndButtonsAndKeyboard.ConfigKeyboard;
import pro.sky.whiskerspawstailtelegrambot.util.FormReplyMessages;
import pro.sky.whiskerspawstailtelegrambot.util.ParserToBot;

/**
 * Обработчик стандартных сообщений от пользователя, в том числе и из обычной клавиатуры
 */
@Slf4j
@Component
public class StandardReplyHandler {

  private final FormReplyMessages formReplyMessages;
  private final ConfigKeyboard configKeyboard;
  private final VolunteerService volunteerService;
  private final ReportAddHandler reportAddHandler;
  private final ParserToBot parserToBot;
  private final ShelterService shelterService;

  private final AdoptiveParentService adoptiveParentService;

  private final RegistrationHandler registrationHandler;

  public StandardReplyHandler(FormReplyMessages formReplyMessages, ConfigKeyboard configKeyboard,
      VolunteerService volunteerService, ReportAddHandler reportAddHandler, ParserToBot parserToBot,
      ShelterService shelterService, AdoptiveParentService adoptiveParentService,
      RegistrationHandler registrationHandler) {
    this.formReplyMessages = formReplyMessages;
    this.configKeyboard = configKeyboard;
    this.volunteerService = volunteerService;
    this.reportAddHandler = reportAddHandler;
    this.parserToBot = parserToBot;
    this.shelterService = shelterService;
    this.adoptiveParentService = adoptiveParentService;
    this.registrationHandler = registrationHandler;
  }

  /**
   * обработка текста отправленого пользователем
   *
   * @param message сообщение из Update
   * @return SendMessage
   */
  public SendMessage handler(Message message) {

    SendMessage sendMessage = null;

    log.debug("Вызов метода handler класса" + this.getClass().getName());
    String chatId = message.getChatId().toString();
    String textMessage = message.getText();
//    if (isDigit(textMessage.charAt(1))) {
//      menuInfo(message);
//    } // Проверка команды на цифру и передача в цифровой метод

    //здесь инжект текст кнопок, любой текст крч
    switch (textMessage) {

      case (AllText.START_TEXT):
        if (adoptiveParentService.getStateAdoptiveParentByChatId(Long.parseLong(chatId)) != null) {
          //приветсвенное сообщение, вылетает только после регистрации
          return sendMessage = formReplyMessages.replyMessage(message,
              AllText.WELCOME_MESSAGE_TEXT,
              configKeyboard.initKeyboardOnClickStart());
        }
        //если не было регистрации, то просто повторяем цикл
        return sendMessage = formReplyMessages.replyMessage(message, AllText.REGISTRATION_INIT,
            configKeyboard.formReplyKeyboardInOneRowInline(AllText.REGISTRATION_BUTTON));

      case (AllText.CALL_TO_VOLUNTEER_TEXT): //ответ на позвать волонтера, просто инфа про волонтеров
        return new SendMessage(chatId,
            parserToBot.parserVolunteer(volunteerService.getAllVolunteers()));

      //region реализация логики Отправить отчет о питомце
      /*
      Менется стейт ПОЛЬЗОВАТЕЛЯ на начало отправки отчета
       */
      case (AllText.SEND_PET_REPORT_TEXT):     // нажатие кнопки Отправить отчет о питомце
        return sendMessage = reportAddHandler.clickButton_SEND_REPORT(message);
      //endregion

      //------------------> регистрация

      case (AllText.REGISTRATION_BUTTON):
        //добавляем в бд и ставим статус ферст стэйт в методе addToTable,
        //так же там меняем клаву на кнопку отмена регистрации
        //при следующем сообщении регистрация будет продолжаться в методе messengerHandler, пока не зарегается до конца,
        //либо отменет регистрацию и все заново
        if (adoptiveParentService.getStateAdoptiveParentByChatId(Long.parseLong(chatId)) != null) {
          //проверяем если есть в бд, то просто сообщение, что вы уже зареганы
          return new SendMessage(chatId, AllText.ALREADY_REGISTERED);
        }
        return registrationHandler.addToTable(message, chatId);

      //------------------> регистрация

      case (AllText.HOW_TAKE_DOG):
        return sendMessage = formReplyMessages.replyMessage(message, AllText.HOW_TAKE_DOG_SHELTER,
            configKeyboard.initKeyboardOnClickStart());


      case (AllText.INFO_SHELTER_TEXT):
        return  sendMessage = formReplyMessages.replyMessage(message,
            AllText.INFO,
            configKeyboard.initKeyboardOnClickStart());
        

      //------------------> Показать Id

      case (AllText.SHOW_ME_ID):
        AdoptiveParentRecord adoptiveParentRecord =
            adoptiveParentService.getAdoptiveParentByChatId(Long.parseLong(chatId));
        if (adoptiveParentRecord != null) {
          //проверяем если есть в бд, то просто id
          return new SendMessage(chatId, AllText.SHOW_ID_OK + adoptiveParentRecord.getId());
        }
        return new SendMessage(chatId, AllText.SHOW_ID_FAILED);

      //------------------> Показать Id

      //------------------> Показать Всех животных

      case (AllText.SHOW_ALL_ANIMAL):

        return sendMessage = formReplyMessages.replyMessage(message, AllText.CHOOSE_CATEGORY,
            configKeyboard
                .formReplyKeyboardInOneRowInline
                    (AllText.CAT,
                        AllText.DOG,
                        AllText.PIG,
                        AllText.BIRD));

      //------------------> Показать Всех животных
      case ("/1"): // Выбор информации из списка меню информации
        return sendMessage = formReplyMessages.replyMessage(message,
                shelterService.getOfShelterMessage((byte) 1),
                configKeyboard.initKeyboardOnClickStart());

      case ("/2"):

        return sendMessage = formReplyMessages.replyMessage(message,
                shelterService.getOfShelterMessage((byte) 2),
                configKeyboard.initKeyboardOnClickStart());
      case ("/3"):
        return sendMessage = formReplyMessages.replyMessage(message,
                shelterService.getOfShelterMessage((byte) 3),
                configKeyboard.initKeyboardOnClickStart());

      case ("/4"):
        return sendMessage = formReplyMessages.replyMessage(message,
                shelterService.getOfShelterMessage((byte) 4),
                configKeyboard.initKeyboardOnClickStart());
      case ("/5"):
        return sendMessage = formReplyMessages.replyMessage(message,
                shelterService.getOfShelterMessage((byte) 5),
                configKeyboard.initKeyboardOnClickStart());
      case ("/6"):
        return sendMessage = formReplyMessages.replyMessage(message,
                shelterService.getOfShelterMessage((byte) 6),
                configKeyboard.initKeyboardOnClickStart());
      case ("/7"):
        return sendMessage = formReplyMessages.replyMessage(message,
                shelterService.getOfShelterMessage((byte) 7),
                configKeyboard.initKeyboardOnClickStart());
      case ("/8"):
        return sendMessage = formReplyMessages.replyMessage(message,
                shelterService.getOfShelterMessage((byte) 8),
                configKeyboard.initKeyboardOnClickStart());

      default:
        return sendMessage = new SendMessage(chatId, AllText.UNKNOWN_COMMAND_TEXT);
    }
  }

}
