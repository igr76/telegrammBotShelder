package com.example.test.controller;

import com.example.test.TelegramBotUpdatesListener;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * Контроллер для бота в телеге
 */
@RestController
@Getter
@Setter
@Slf4j
//@Hidden
public class BotController {
    final TelegramBotUpdatesListener telegramBotUpdatesListener;

    public BotController(TelegramBotUpdatesListener telegramBotUpdatesListener) {
        this.telegramBotUpdatesListener = telegramBotUpdatesListener;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBotUpdatesListener.onWebhookUpdateReceived(update);
    }
}
