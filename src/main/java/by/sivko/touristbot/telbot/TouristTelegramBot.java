package by.sivko.touristbot.telbot;

import by.sivko.touristbot.entity.City;
import by.sivko.touristbot.entity.TouristUser;
import by.sivko.touristbot.service.CityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TouristTelegramBot extends TelegramLongPollingBot {

    private static final String DEFAULT_MESSAGE = String.format("%s Напишите название города\n%s Или выберите из списка в меню", Emoji.LARGE_ORANGE_DIAMOND, Emoji.RELIEVED_FACE);
    private static final String MESSAGE_SHOW_ALL_CITIES = String.format("Показать весь список городов %s", Emoji.ENVELOPE);
    private static final String MESSAGE_CHOOSE_CITY = String.format("Выберите город %s", Emoji.STATION);
    private static final String MESSAGE_CITY_NOT_FOUND = String.format("Город в базе не найден %s", Emoji.SUN_BEHIND_CLOUD);

    private static final int STARTSTATE = 0;
    private static final int MAINMENU = 1;
    private static final int CITYLIST = 2;

    @Value("${telegram.username}")
    private String telegramUsername;

    @Value("${telegram.token}")
    private String telegramToken;

    private final CityService cityService;
    private final TouristUserStateService touristUserStateService;

    @Autowired
    public TouristTelegramBot(CityService cityService, TouristUserStateService touristUserStateService) {
        this.cityService = cityService;
        this.touristUserStateService = touristUserStateService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                if (message.hasText() || message.hasLocation()) {
                    handleIncomingMessage(message);
                }
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    private void handleIncomingMessage(Message message) throws TelegramApiException {
        final TouristUser touristUser = new TouristUser(Long.valueOf(message.getFrom().getId()), message.getChatId());
        final int state = this.touristUserStateService.getState(touristUser);

        if (message.hasText() && isSimpleCommand(message.getText())) {
            execute(sendMessageDefault(message));
            return;
        }

        SendMessage sendMessageRequest;
        switch (state) {
            case MAINMENU:
                sendMessageRequest = messageOnMainMenu(message);
                break;
            case CITYLIST:
                sendMessageRequest = messageOnCityList(message);
                break;
            default:
                sendMessageRequest = sendMessageDefault(message);
                break;
        }

        execute(sendMessageRequest);
    }

    private static boolean isSimpleCommand(String text) {
        boolean isSimpleCommand = text.equals("/start") || text.equals("/help") || text.equals("/stop");
        return text.startsWith("/") && isSimpleCommand;
    }

    private SendMessage messageOnMainMenu(Message message) {
        SendMessage sendMessageRequest;
        if (message.hasText()) {
            if (message.getText().equals(MESSAGE_SHOW_ALL_CITIES)) {
                sendMessageRequest = onCityList(message);
            } else {
                sendMessageRequest = sendChoosenCityInfo(message, getMainMenuKeyboard());
            }
        } else {
            sendMessageRequest = sendChoosenCityInfo(message, getMainMenuKeyboard());
        }
        return sendMessageRequest;
    }

    private SendMessage messageOnCityList(Message message) {

        return sendChoosenCityInfo(message, getMainMenuKeyboard());
    }

    private SendMessage sendChoosenCityInfo(Message message, ReplyKeyboardMarkup mainMenuKeyboard) {
        String requestedCityName = message.getText();
        Optional<City> persistedCity = this.cityService.findByName(requestedCityName);
        SendMessage sendMessage = new SendMessage();
        if (persistedCity.isPresent()) {
            sendMessage.setText(persistedCity.get().getInfo());
        } else {
            sendMessage.setText(MESSAGE_CITY_NOT_FOUND);
        }
        sendMessage.setReplyMarkup(mainMenuKeyboard);
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setChatId(message.getChatId());

        final TouristUser touristUser = new TouristUser(Long.valueOf(message.getFrom().getId()), message.getChatId());
        this.touristUserStateService.save(touristUser, MAINMENU);

        return sendMessage;
    }

    private SendMessage onCityList(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        ReplyKeyboard replyKeyboardMarkup = getCityListKeyboard();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(MESSAGE_CHOOSE_CITY);

        final TouristUser touristUser = new TouristUser(Long.valueOf(message.getFrom().getId()), message.getChatId());
        this.touristUserStateService.save(touristUser, CITYLIST);
        return sendMessage;
    }

    private SendMessage sendMessageDefault(Message message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();

        final TouristUser touristUser = new TouristUser(Long.valueOf(message.getFrom().getId()), message.getChatId());
        this.touristUserStateService.save(touristUser, MAINMENU);

        return sendHelpMessage(message.getChatId(), message.getMessageId(), replyKeyboardMarkup);
    }

    private SendMessage sendHelpMessage(Long chatId, Integer messageId, ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyToMessageId(messageId);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        sendMessage.setText(DEFAULT_MESSAGE);
        return sendMessage;
    }

    private ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(MESSAGE_SHOW_ALL_CITIES);
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private ReplyKeyboard getCityListKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        for (City city : this.cityService.findAll()) {
            KeyboardRow row = new KeyboardRow();
            row.add(city.getName());
            keyboard.add(row);
        }

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    @Override
    public String getBotUsername() {
        return this.telegramUsername;
    }

    @Override
    public String getBotToken() {
        return this.telegramToken;
    }

}
