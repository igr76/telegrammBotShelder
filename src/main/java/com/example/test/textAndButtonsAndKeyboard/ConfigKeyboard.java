package com.example.test.textAndButtonsAndKeyboard;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, где создается кнопки в боте
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
@Slf4j
public class ConfigKeyboard extends Button {

  /**
   * Метод установки клавиатуры на нажатие start
   */
  public ReplyKeyboardMarkup initKeyboardOnClickStart() {

    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> KEYBOARD_BUTTONS_ROW_INFO_REPORT = new ArrayList<>();
    KeyboardRow row = new KeyboardRow();
    row.add(INFO_SHELTER_BUTTON);
    row.add(SEND_PET_REPORT_BUTTON);
    row.add(SHOW_ALL_ANIMAL_BUTTON);
    KEYBOARD_BUTTONS_ROW_INFO_REPORT.add(row);
    keyboardMarkup.setResizeKeyboard(true);
    keyboardMarkup.setKeyboard(KEYBOARD_BUTTONS_ROW_INFO_REPORT);
    return keyboardMarkup;
  }


  /**
   * Метод формирует клавиатуры из переданного текста
   *
   * @param textButtons текст кнопок, список может быть любой длинны
   * @return клавиатуру с текстом кнопок из textButtons
   */
  public ReplyKeyboardMarkup formReplyKeyboardInOneRow(String... textButtons) {

    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> KEYBOARD_BUTTONS = new ArrayList<>();
    KeyboardRow row = new KeyboardRow();

    for (String textButton : textButtons) {
      row.add(new KeyboardButton(textButton));
    }
    KEYBOARD_BUTTONS.add(row);
    keyboardMarkup.setResizeKeyboard(true);
    keyboardMarkup.setKeyboard(KEYBOARD_BUTTONS);
    return keyboardMarkup;
  }

  public ReplyKeyboardMarkup initKeyboardOnClickRegistration() {

    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    List<KeyboardRow> KEYBOARD_BUTTONS_ROW_INFO_REPORT = new ArrayList<>();
    KeyboardRow row = new KeyboardRow();
    row.add(REGISTRATION_CANCEL);
    KEYBOARD_BUTTONS_ROW_INFO_REPORT.add(row);
    keyboardMarkup.setResizeKeyboard(true);
    keyboardMarkup.setKeyboard(KEYBOARD_BUTTONS_ROW_INFO_REPORT);
    return keyboardMarkup;

  }

  public InlineKeyboardMarkup formReplyKeyboardInOneRowInline(String... textButtons) {

    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> listRow = new ArrayList<>();
    List<InlineKeyboardButton> row = new ArrayList<>();
//    inlineKeyboardButton row = new inlineKeyboardButton();

    for (String textButton : textButtons) {
      InlineKeyboardButton button = new InlineKeyboardButton(textButton);
      button.setCallbackData(textButton);
      row.add(button);
    }
    listRow.add(row);
    inlineKeyboardMarkup.setKeyboard(listRow);

    return inlineKeyboardMarkup;
  }


  public InlineKeyboardMarkup formReplyKeyboardAnyRowInline(int numberPerLine,
      List<String> textButtons) {

    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> listRow = new ArrayList<>();
    List<InlineKeyboardButton> row;
    int i = 0, j = 0;
    while (i < textButtons.size()) {
      row = new ArrayList<>();
      while (i < textButtons.size() && j < numberPerLine) {
        InlineKeyboardButton button = new InlineKeyboardButton(textButtons.get(i));
        button.setCallbackData(textButtons.get(i));
        row.add(button);
        i++;
        j++;
      }
      listRow.add(row);
      j = 0;
    }
    inlineKeyboardMarkup.setKeyboard(listRow);
    return inlineKeyboardMarkup;
  }

}
