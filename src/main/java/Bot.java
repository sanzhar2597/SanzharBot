import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }


    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        sendMessage.setChatId(message.getChatId());

        sendMessage.setReplyToMessageId(message.getMessageId());

        sendMessage.setText(text);
        try {

            setButtons(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/help":
                    sendMsg(message, "Как пользоватся этим ботом ? \n " +
                            " /help  -Помощь Пользователю  \n " +
                            "/using  - Что делает бот\n" +
                            " /info  - Информация  о боте " );
                    break;
                case "/using":
                    sendMsg(message, "Что делает бот?  \n Нужно просто написать название Города он вам покажет следушие \n" +
                            " 1)Название города \n" +
                             "2)Температура \n" +
                            " 3)влажность \n" +
                            "4)Прогноз (Осадки) \n" +
                            "5)картинку соответсвуюшему прогнозу");
                    break;
                case "/info":
                    sendMsg(message, "Бот разработан в 2019 году 10 Марта .\n Разработчик Дадаханов Санжар Курбанбайулы .");

                    break;

                default:
                    try {
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e) {
                        sendMsg(message, "Город не найден!");
                    }

            }
        }

    }


    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("/help"));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("/using"));
        keyboardSecondRow.add(new KeyboardButton("/info"));
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public String getBotUsername() {
        return "Sanzhar_Dadakhanov_bot";
    }

    public String getBotToken() {
        return "719391284:AAHUuLMTHUfDfTBJTIXy75emZ0_kubCS2Ts";
    }
}
