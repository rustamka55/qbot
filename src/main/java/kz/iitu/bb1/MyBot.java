package kz.iitu.bb1;

import kz.iitu.bb1.botapi.TelegramFacade;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MyBot extends TelegramWebhookBot {

    private String botUsername;
    private String botToken;
    private String webHookPath;

    private TelegramFacade telegramFacade;

    public MyBot(TelegramFacade telegramFacade){
        this.telegramFacade = telegramFacade;
    }

    public MyBot(DefaultBotOptions defaultBotOptions, TelegramFacade telegramFacade){
        super(defaultBotOptions);
        this.telegramFacade = telegramFacade;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        BotApiMethod<?> replyMessageToUser = telegramFacade.handleUpdate(update);

        return replyMessageToUser;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }
}
