package com.example.test.configuration;

import com.example.test.TelegramBotUpdatesListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, где создается меню комнад в боте
 */
@Configuration
@Slf4j
@Data
public class ConfigMenu {
    public void initMenu(TelegramBotUpdatesListener bot){
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand(AllText.START_TEXT, AllText.START_MENU_TEXT));
        listofCommands.add(new BotCommand(AllText.CALL_TO_VOLUNTEER_TEXT, AllText.CALL_VOLUNTEER_TEXT));
        listofCommands.add(new BotCommand(AllText.SHOW_ME_ID, AllText.SHOW_ID));
        try {
            bot.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }
}
