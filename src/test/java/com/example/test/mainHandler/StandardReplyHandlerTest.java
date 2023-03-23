package com.example.test.mainHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import pro.sky.whiskerspawstailtelegrambot.entity.Shelter;
import pro.sky.whiskerspawstailtelegrambot.mainHandler.reportHandler.ReportAddHandler;
import pro.sky.whiskerspawstailtelegrambot.record.VolunteerRecord;
import pro.sky.whiskerspawstailtelegrambot.service.AdoptiveParentService;
import pro.sky.whiskerspawstailtelegrambot.service.ShelterService;
import pro.sky.whiskerspawstailtelegrambot.service.VolunteerService;
import pro.sky.whiskerspawstailtelegrambot.textAndButtonsAndKeyboard.AllText;
import pro.sky.whiskerspawstailtelegrambot.textAndButtonsAndKeyboard.ConfigKeyboard;
import pro.sky.whiskerspawstailtelegrambot.util.FormReplyMessages;
import pro.sky.whiskerspawstailtelegrambot.util.ParserToBot;
import pro.sky.whiskerspawstailtelegrambot.util.StateAdoptiveParent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class StandardReplyHandlerTest {
    @Mock
    private AdoptiveParentService adoptiveParentService;
    @Mock
    private VolunteerService volunteerService;
    @Mock
    private ShelterService shelterService;
    @Mock
    private ReportAddHandler reportAddHandler;
    @Mock
    private RegistrationHandler registrationHandler;
    @InjectMocks
    private StandardReplyHandler out;
    private FormReplyMessages formReplyMessages;
    private ConfigKeyboard configKeyboard;
    private ParserToBot parserToBot;

    @BeforeEach
    public void init() {
        configKeyboard = new ConfigKeyboard();
        parserToBot = new ParserToBot();

        formReplyMessages = new FormReplyMessages();
        out = new StandardReplyHandler(formReplyMessages, configKeyboard,volunteerService, reportAddHandler, parserToBot,
                shelterService, adoptiveParentService, registrationHandler);

    }

    @Test
    public void startTextIfNotRegistertTest() {
        Message testMessage = new Message();
        long chatId = 3399;
        testMessage.setText(AllText.START_TEXT);
        testMessage.setChat(new Chat(chatId, "private"));

        lenient().when(adoptiveParentService.getStateAdoptiveParentByChatId(any(Long.class))).thenReturn(null);
        SendMessage excepted = new SendMessage(String.valueOf(chatId), AllText.REGISTRATION_INIT);
        InlineKeyboardMarkup inlineKeyboardMarkup = configKeyboard.formReplyKeyboardInOneRowInline(
                AllText.REGISTRATION_BUTTON);

        excepted.setReplyMarkup(inlineKeyboardMarkup);

        SendMessage outMessage = out.handler(testMessage);

        assertEquals(excepted, outMessage);

    }

    @Test
    public void startTextIfRegisterTest() {
        Message testMessage = new Message();
        long chatId = 3399;
        testMessage.setText(AllText.START_TEXT);
        testMessage.setChat(new Chat(chatId, "private"));
        lenient().when(adoptiveParentService.getStateAdoptiveParentByChatId(any(Long.class))).thenReturn(
                StateAdoptiveParent.SUCCESS_REG);

        SendMessage excepted = new SendMessage(String.valueOf(chatId), AllText.WELCOME_MESSAGE_TEXT);
        ReplyKeyboardMarkup replyKeyboardMarkup = configKeyboard.initKeyboardOnClickStart();

        excepted.setReplyMarkup(replyKeyboardMarkup);

        SendMessage outMessage = out.handler(testMessage);

        assertEquals(excepted, outMessage);

    }
    @Test
    public void cancelTextTest() {
        Message testMessage = new Message();
        long chatId = 3399;
        testMessage.setText(AllText.CANCEL_TEXT);
        testMessage.setChat(new Chat(chatId, "private"));

        SendMessage excepted = new SendMessage(String.valueOf(chatId), AllText.CANCEL_RETURN_MAIN_MENU_TEXT);
        ReplyKeyboardMarkup replyKeyboardMarkup = configKeyboard.initKeyboardOnClickStart();

        excepted.setReplyMarkup(replyKeyboardMarkup);

        SendMessage outMessage = out.handler(testMessage);

        assertEquals(excepted, outMessage);

    }

    @Test
    public void callToVolunteerTextTest() {
        Message testMessage = new Message();
        long chatId = 3399;
        testMessage.setText(AllText.CALL_TO_VOLUNTEER_TEXT);
        testMessage.setChat(new Chat(chatId, "private"));

        lenient().when(volunteerService.getAllVolunteers()).thenReturn(getTestVolunteerRecordList());
        SendMessage excepted = new SendMessage(String.valueOf(chatId),
                parserToBot.parserVolunteer(getTestVolunteerRecordList()));

        SendMessage outMessage = out.handler(testMessage);

        assertEquals(excepted, outMessage);

    }

    @Test
    public void sendPetReportTextTest() {
        Message testMessage = new Message();
        long chatId = 3399;
        testMessage.setText(AllText.SEND_PET_REPORT_TEXT);
        testMessage.setChat(new Chat(chatId, "private"));

        SendMessage excepted = new SendMessage(String.valueOf(chatId), AllText.MENU_SEND_PET_REPORT_TEXT);
        InlineKeyboardMarkup replyKeyboardMarkup = configKeyboard.formReplyKeyboardInOneRowInline(
                AllText.SHOW_ALL_YOUR_PET_TEXT, AllText.SEND_REPORT_TEXT, AllText.CANCEL_TEXT);

        excepted.setReplyMarkup(replyKeyboardMarkup);

        SendMessage outMessage = out.handler(testMessage);

        assertEquals(excepted, outMessage);
    }

    @Test
    public void registrationButtonTextAllreadyRegistertedTest() {
        Message testMessage = new Message();
        long chatId = 3399;
        testMessage.setText(AllText.REGISTRATION_BUTTON);
        testMessage.setChat(new Chat(chatId, "private"));

        lenient().when(adoptiveParentService.getStateAdoptiveParentByChatId(any(Long.class)))
                .thenReturn(StateAdoptiveParent.IN_PROCESS_SEND_REPORT);

        SendMessage excepted = new SendMessage(String.valueOf(chatId),AllText.ALREADY_REGISTERED);

        SendMessage outMessage = out.handler(testMessage);

        assertEquals(excepted, outMessage);

    }

    @Test
    public void registrationButtonTextTest() {
        Message testMessage = new Message();
        long chatId = 3399;
        testMessage.setText(AllText.REGISTRATION_BUTTON);
        testMessage.setChat(new Chat(chatId, "private"));

        lenient().when(adoptiveParentService.getStateAdoptiveParentByChatId(any(Long.class)))
                .thenReturn(null);

        SendMessage excepted = new SendMessage(String.valueOf(chatId),AllText.REG_FULL_NAME);
        ReplyKeyboardMarkup replyKeyboardMarkup = configKeyboard.initKeyboardOnClickRegistration();
        excepted.setReplyMarkup(replyKeyboardMarkup);

        lenient().when(registrationHandler.addToTable(testMessage, String.valueOf(chatId)))
                .thenReturn(excepted);


        SendMessage outMessage = out.handler(testMessage);

        assertEquals(excepted, outMessage);

    }

    @Test
    public void howTakePetTextTest() {
        Message testMessage = new Message();
        long chatId = 3399;
        testMessage.setText(AllText.HOW_TAKE_DOG);
        testMessage.setChat(new Chat(chatId, "private"));

        SendMessage excepted = new SendMessage(String.valueOf(chatId), AllText.HOW_TAKE_DOG_SHELTER);
        ReplyKeyboardMarkup replyKeyboardMarkup = configKeyboard.initKeyboardOnClickStart();
        excepted.setReplyMarkup(replyKeyboardMarkup);

        SendMessage outMessage = out.handler(testMessage);

        assertEquals(excepted, outMessage);
    }

    @Test
    public void shelterTextTest() {
        Message testMessage = new Message();
        long chatId = 3399;
        testMessage.setText(AllText.INFO_SHELTER_TEXT);
        testMessage.setChat(new Chat(chatId, "private"));

        SendMessage excepted = new SendMessage(String.valueOf(chatId), AllText.INFO);
        ReplyKeyboardMarkup replyKeyboardMarkup = configKeyboard.initKeyboardOnClickStart();
        excepted.setReplyMarkup(replyKeyboardMarkup);

        SendMessage outMessage = out.handler(testMessage);

        assertEquals(excepted, outMessage);
    }

    @Test
    public void unknownCommandTextTest() {
        Message testMessage = new Message();
        long chatId = 3399;
        testMessage.setText("Просто текст");
        testMessage.setChat(new Chat(chatId, "private"));

        SendMessage excepted = new SendMessage(String.valueOf(chatId), AllText.UNKNOWN_COMMAND_TEXT);
        SendMessage outMessage = out.handler(testMessage);

        assertEquals(excepted, outMessage);
    }


    /**
     * Создает список волонтеров
     * @return  - список волонтеров
     */
    private Collection<VolunteerRecord> getTestVolunteerRecordList() {
        List<VolunteerRecord> volunteerRecords = new ArrayList<>();
        Shelter shelter = getTestShelter();

        volunteerRecords.add(
                new VolunteerRecord(1L, "Иванов Иван Иванович", "+79876543210",
                        "это волонтер", "будни с 9:00 до 17:00",shelter)
        );
        volunteerRecords.add(
                new VolunteerRecord(2L, "Петров Петр Петрович", "+79876543211",
                        "это тоже волонтер", "будни с 9:00 до 18:00",shelter)
        );

        return volunteerRecords;
    }

    /**
     * Возвращает тестовую сущность приюта
     * @return - тестовая сущность приюта
     */
    private Shelter getTestShelter() {
        Shelter shelter = new Shelter();
        shelter.setId(1L);
        return shelter;
    }
}