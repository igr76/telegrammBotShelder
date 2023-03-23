package com.example.test.mainHandler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * общий интерфейс для обработок
 */
@Component
public interface MainHandler {

  SendMessage handler(Update update);

}
