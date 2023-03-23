package com.example.test.textAndButtonsAndKeyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

/**
 * Класс в котором содежатся кнопки для взаимодействия пользователя с ботом
 */
public class Button extends AllText {
    protected static final KeyboardButton INFO_SHELTER_BUTTON = new KeyboardButton(INFO_SHELTER_TEXT);
    protected static final KeyboardButton SHOW_ALL_YOUR_PET_BUTTON = new KeyboardButton(
            SHOW_ALL_YOUR_PET_TEXT);
    protected static final KeyboardButton SEND_PET_REPORT_BUTTON = new KeyboardButton(
            SEND_PET_REPORT_TEXT);

    protected static final KeyboardButton SHOW_ALL_ANIMAL_BUTTON = new KeyboardButton(
        SHOW_ALL_ANIMAL);
    protected static final KeyboardButton SEND_REPORT_BUTTON = new KeyboardButton(SEND_REPORT_TEXT);
    protected static final KeyboardButton CANCEL_BUTTON = new KeyboardButton(CANCEL_TEXT);
    protected static final KeyboardButton SEND_BUTTON = new KeyboardButton(SEND_TEXT);

    //кнопка регистрации
    protected static final KeyboardButton REGISTRATION_BUT = new KeyboardButton(REGISTRATION_BUTTON);


}
