package com.example.test.configuration;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;


/**
 * Настройка бота
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
public class TelegramBotConfiguration {

    //инжект меню команд
    final ConfigMenu configMenu;

    /**
     * токен бота
     */
    @Value("${telegram.bot.token}")
    String token;

    /**
     * ссылка на адрес ( либо ngrok, либо сервер)
     */
    @Value("${telegram.bot.webHookPath}")
    String webHookPath;

    /**
     * название бота в телеге
     */
    @Value("${telegram.bot.userName}")
    String userName;

    public TelegramBotConfiguration(ConfigMenu configMenu) {
        this.configMenu = configMenu;
    }


    /**
     * установка url для хука
     *
     * @return хук
     */
    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(this.getWebHookPath()).build();
    }

    /**
     * установка хука для бота
     *
     * @param setWebhook наш хук выше
     * @return бот с установленным хуком
     * @see pro.sky.whiskerspawstailtelegrambot.TelegramBotUpdatesListener
     */
    @Bean
    public TelegramBotUpdatesListener springWebhookBot(SetWebhook setWebhook) {
        TelegramBotUpdatesListener bot = new TelegramBotUpdatesListener(setWebhook);

        bot.setWebHookPath(this.getWebHookPath());
        bot.setBotUserName(this.getUserName());
        bot.setBotToken(this.getToken());
        //инит меню комнад
        configMenu.initMenu(bot);


        return bot;
    }

}
