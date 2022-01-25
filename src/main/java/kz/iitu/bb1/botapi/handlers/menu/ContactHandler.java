package kz.iitu.bb1.botapi.handlers.menu;

import kz.iitu.bb1.botapi.BotState;
import kz.iitu.bb1.botapi.InputMessageHandler;
import kz.iitu.bb1.botapi.handlers.botHandler.UserProfileData;
import kz.iitu.bb1.cache.UserDataCache;
import kz.iitu.bb1.service.AboutMenuService;
import kz.iitu.bb1.service.MainMenuService;
import kz.iitu.bb1.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ContactHandler implements InputMessageHandler {
    private ReplyMessageService messageService;
    private AboutMenuService aboutMenuService;
    private MainMenuService mainMenuService;
    private UserDataCache userDataCache;

    public ContactHandler(ReplyMessageService messageService, AboutMenuService aboutMenuService, UserDataCache userDataCache, MainMenuService mainMenuService) {
        this.messageService = messageService;
        this.aboutMenuService = aboutMenuService;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(Message message) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = aboutMenuService.getAboutMenuMessage(message.getChatId(), messageService.getReplyMessageText("reply.showMainMenu"));

        if (botState.equals(BotState.ASK_CONTACT)){
            replyToUser = messageService.getReplyMessage(chatId,"reply.ask.contact.text");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_CONTACT_NAME);
            return replyToUser;
        }

        if (botState.equals(BotState.ASK_CONTACT_NAME)) {
            profileData.setName(message.getText());
            replyToUser = mainMenuService.getMainMenuMessage(chatId,messageService.getReplyMessageText("reply.ask.contact.answer"));
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
            return replyToUser;
        }

        userDataCache.setUsersCurrentBotState(userId,BotState.SHOW_MAIN_MENU);
        replyToUser = mainMenuService.getMainMenuMessage(chatId,messageService.getReplyMessageText("reply.showMainMenu"));
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_CONTACT;
    }


}