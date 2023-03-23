package com.example.test.mainHandler.reportHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import pro.sky.whiskerspawstailtelegrambot.record.PetRecord;
import pro.sky.whiskerspawstailtelegrambot.record.ReportRecord;
import pro.sky.whiskerspawstailtelegrambot.service.AdoptiveParentService;
import pro.sky.whiskerspawstailtelegrambot.service.ReportService;
import pro.sky.whiskerspawstailtelegrambot.textAndButtonsAndKeyboard.AllText;
import pro.sky.whiskerspawstailtelegrambot.textAndButtonsAndKeyboard.ConfigKeyboard;
import pro.sky.whiskerspawstailtelegrambot.util.FormReplyMessages;
import pro.sky.whiskerspawstailtelegrambot.util.StateAdoptiveParent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pro.sky.whiskerspawstailtelegrambot.textAndButtonsAndKeyboard.AllText.*;
import static pro.sky.whiskerspawstailtelegrambot.util.StateReport.*;

/**
 * обработка репорт на будующее
 */
@Slf4j
@Component
public class ReportAddHandler {

  private final FormReplyMessages formReplyMessages;
  private final ReportService reportService;
  private final ConfigKeyboard configKeyboard;
  private SendMessage sendMessage = null;
  private final AdoptiveParentService adoptiveParentService;

  public ReportAddHandler(FormReplyMessages formReplyMessages, ReportService reportService,
      ConfigKeyboard configKeyboard, AdoptiveParentService adoptiveParentService) {
    this.formReplyMessages = formReplyMessages;
    this.reportService = reportService;
    this.configKeyboard = configKeyboard;
    this.adoptiveParentService = adoptiveParentService;
  }

  public SendMessage handler(Message message) {

    SendMessage sendMessage = null;
    String textMessage = message.getText();
    long chatId = message.getChatId();

    FillingReportRecord fillingReportRecord = new FillingReportRecord();
    PetRecord petRecord = fillingReportRecord.checkCorrectPetId(textMessage, reportService);
    if (petRecord == null) {
      sendMessage = formReplyMessages.replyMessage(message, ENTER_ERROR_ID_TEXT,
          configKeyboard.initKeyboardOnClickStart());
    }

    switch (textMessage) {

    }

    String stateReport = reportService.getStateReportByPetId(chatId) == null ? NOT_STARTED
        : reportService.getStateReportByPetId(chatId);

    switch (stateReport) {

//      case NOT_STARTED:
//        FillingReportRecord fillingReportRecord = new FillingReportRecord();
//        saveReportInDb(message, fillingReportRecord.newReport());
//        sendMessage = formReplyMessages.replyMessage(message, PHOTO_SEND_REPORT_TEXT,
//            configKeyboard.formReplyKeyboardInOneRow(CANCEL_TEXT));
//        return sendMessage;

      case NOT_STARTED:
        sendMessage = clickButton_SEND_REPORT(message);
        return sendMessage;

      case WAIT_ID_PET_REPORT:
        sendMessage = formReplyMessages.replyMessage(message, PHOTO_SEND_REPORT_TEXT,
            configKeyboard.formReplyKeyboardInOneRowInline(SHOW_ALL_YOUR_PET_TEXT,
                CANCEL_TEXT));

      case WAIT_PHOTO_REPORT:
        return sendMessage = formReplyMessages.replyMessage(message, DIET_SEND_REPORT_TEXT,
            configKeyboard.formReplyKeyboardInOneRow(CANCEL_TEXT));

      case WAIT_DIET_REPORT:
        return sendMessage = formReplyMessages.replyMessage(message, FEELINGS_SEND_REPORT_TEXT,
            configKeyboard.formReplyKeyboardInOneRow(CANCEL_TEXT));
//
//      case WAIT_FEELINGS_REPORT:
//        return sendMessage = reportService.changeStateAdoptiveParent(message,
//            FEELINGS_SEND_REPORT_TEXT, StateAdoptiveParent.FREE);
//
//      case WAIT_HABITS_REPORT:
//        return sendMessage = reportService.changeStateAdoptiveParent(message,
//            HABITS_SEND_REPORT_TEXT, StateAdoptiveParent.FREE);
    }

    return sendMessage;

  }

  //region clickButton

  /**
   * Метод обрабатывает нажатие на кнопку показать всех ваших животных
   *
   * @param message сообщение из update
   */
//  public SendMessage clickButton_SHOW_ALL_YOUR_PET(Message message) {
//    log.info("Вызов метода " + new Throwable()
//        .getStackTrace()[0]
//        .getMethodName() + " класса " + this.getClass().getName());
//    String allPetByChatId = reportService.showAllAdoptedPets(message.getChatId());
//    String[] buttonsPets = new String[allPetByChatId.length() + 1];
//    buttonsPets = allPetByChatId.split(AllText.DELIMITER_FOR_PARSER_PETS);
//    buttonsPets[buttonsPets.length - 1] = CANCEL_TEXT;
//
////    InlineKeyboardMarkup inlineKeyboardMarkup = configKeyboard.formReplyKeyboardInOneRowInline(
////    );
//
//    int numberPerLine = 1;
//    InlineKeyboardMarkup inlineKeyboardMarkup = configKeyboard.formReplyKeyboardAnyRowInline(
//        numberPerLine, buttonsPets);
//
//    return sendMessage = formReplyMessages.replyMessage(message,
//        DESCRIPTION_SEND_REPORT_TEXT, inlineKeyboardMarkup);
//  }

  /**
   * Метод обрабатывает нажатие на кнопку показать всех отправить отчет и изменят статус
   * пользователя на ожидание отправки отчета
   *
   * @param messageFromStandardHandler сообщение из update
   */
  public SendMessage clickButton_SEND_REPORT(Message messageFromStandardHandler) {
    log.info("Вызов метода " + new Throwable()
        .getStackTrace()[0]
        .getMethodName() + " класса " + this.getClass().getName());
    String allPetByChatId = reportService.showAllAdoptedPets(messageFromStandardHandler.getChatId());
    if (allPetByChatId == null) {
      return sendMessage = formReplyMessages.replyMessage(messageFromStandardHandler,
          YOU_HAVE_NO_ADOPTED_PETS_TEXT, configKeyboard.initKeyboardOnClickStart());
    }

    List<String> buttonsPets = new ArrayList<>(
        List.of(allPetByChatId.split(AllText.DELIMITER_FOR_PARSER_PETS)));
    buttonsPets.add(CANCEL_TEXT);

    int numberPerLine = 1;
    InlineKeyboardMarkup inlineKeyboardMarkup = configKeyboard.formReplyKeyboardAnyRowInline(
        numberPerLine, buttonsPets);

    adoptiveParentService.updateStateAdoptiveParentByChatId((messageFromStandardHandler.getChatId()),
        StateAdoptiveParent.START_SEND_REPORT);

    return sendMessage = formReplyMessages.replyMessage(messageFromStandardHandler,
        DESCRIPTION_SEND_REPORT_TEXT, inlineKeyboardMarkup);
  }

  //endregion

  /**
   * Метод пытается сохранить отчет о животном в БД
   *
   * @param message сообщение из update
   */
  public SendMessage sendReport(Message message) {
    log.info("Вызов метода " + new Throwable()
        .getStackTrace()[0]
        .getMethodName() + " класса " + this.getClass().getName());

    ReplyKeyboardMarkup replyKeyboardMarkup = configKeyboard.formReplyKeyboardInOneRow(
        CANCEL_TEXT);

    String textMessage = message.getText();
    List<PhotoSize> photoSizes = message.getPhoto();
    Document messageDocument = message.getDocument();

    boolean isTextOk = checkTextInSentReport(textMessage);
    boolean isPhotoOk = true;

    if (!isTextOk) {
      return sendMessage = formReplyMessages.replyMessage(message,
          NO_TEXT_SEND_REPORT_TEXT,
          configKeyboard.initKeyboardOnClickStart());
    }
    if (!isPhotoOk) {
      return sendMessage = formReplyMessages.replyMessage(message,
          NO_PHOTO_SEND_REPORT_TEXT,
          configKeyboard.initKeyboardOnClickStart());
    }

//    return sendMessage = saveReportInDb(message);
    return null;
  }

  public boolean checkTextInSentReport(String textMessage) {

    Pattern pattern = Pattern.compile("^(\\d+)(?:\\s+\\w+|\\s+[а-яА-Я]+){15,}");
    Matcher matcher = pattern.matcher(textMessage);

    if (matcher.matches()) {
      return true;
    }
    return true;//true для теста

  }

  public void checkPhotoInSentReport() {

  }

  public boolean isStatusUpdate() {

    return true;
  }

  /**
   * метод сохраняет отчет в БД
   *
   * @param message сообщение из update
   */
  public SendMessage saveReportInDb(Message message, ReportRecord reportRecord) {

    long chatId = message.getChatId();
//    reportService.addReport();

    reportService.updateStateAdoptiveParentByChatId(chatId, StateAdoptiveParent.FREE);
    return sendMessage = formReplyMessages.replyMessage(message,
        CANCEL_RETURN_MAIN_MENU_TEXT, configKeyboard.initKeyboardOnClickStart());

  }


}
