package kz.iitu.bb1.botapi.handlers.botHandler;

import kz.iitu.bb1.botapi.BotState;
import kz.iitu.bb1.botapi.InputMessageHandler;
import kz.iitu.bb1.cache.UserDataCache;
import kz.iitu.bb1.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


@Slf4j
@Component
public class BotHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessageService messageService;

    public BotHandler(UserDataCache userDataCache,
                      ReplyMessageService messageService) {
        this.userDataCache = userDataCache;
        this.messageService = messageService;
    }

    @Override
    public SendMessage handle(Message message) {
        String usersAnswer = message.getText();
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_NAME)) {
            replyToUser = messageService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_NUMBER);
        }

        if (botState.equals(BotState.ASK_NUMBER)) {
            profileData.setName(usersAnswer);
            replyToUser = messageService.getReplyMessage(chatId, "reply.askNumber");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_LANG);
        }

        if (botState.equals(BotState.ASK_LANG)) {
            profileData.setNumber(usersAnswer);
            replyToUser = processUsersInput(message);
            userDataCache.setUsersCurrentBotState(userId, BotState.CHANGE_LANG);
        }

        userDataCache.saveUserProfileData(userId, profileData);

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.BOT_HANDLER;
    }
    private SendMessage processUsersInput(Message message){
        Long chatId = message.getChatId();

        SendMessage replyToUser = messageService.getReplyMessage(chatId,"reply.askLang");
        replyToUser.setReplyMarkup(getInlineMessageButtons());

        return replyToUser;
    }

    private InlineKeyboardMarkup getInlineMessageButtons(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton buttonRu = new InlineKeyboardButton("На русском");
        InlineKeyboardButton buttonKz = new InlineKeyboardButton("Қазақ тілінде");
        buttonRu.setCallbackData("buttonRu");
        buttonKz.setCallbackData("buttonKz");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonRu);
        keyboardButtonsRow1.add(buttonKz);

        List<List<InlineKeyboardButton>> keyboardButtons = new ArrayList<>();
        keyboardButtons.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(keyboardButtons);

        return inlineKeyboardMarkup;
    }
}
