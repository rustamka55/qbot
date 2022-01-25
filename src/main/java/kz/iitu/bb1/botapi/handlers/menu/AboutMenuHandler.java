package kz.iitu.bb1.botapi.handlers.menu;

import kz.iitu.bb1.botapi.BotState;
import kz.iitu.bb1.botapi.BotStateContext;
import kz.iitu.bb1.botapi.InputMessageHandler;
import kz.iitu.bb1.cache.UserDataCache;
import kz.iitu.bb1.service.AboutMenuService;
import kz.iitu.bb1.service.MainMenuService;
import kz.iitu.bb1.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class AboutMenuHandler implements InputMessageHandler {
    private ReplyMessageService messageService;
    private AboutMenuService aboutMenuService;
    private MainMenuService mainMenuService;
    private UserDataCache userDataCache;

    public AboutMenuHandler(ReplyMessageService messageService, AboutMenuService aboutMenuService,UserDataCache userDataCache,MainMenuService mainMenuService) {
        this.messageService = messageService;
        this.aboutMenuService = aboutMenuService;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(Message message) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();

        BotState botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = aboutMenuService.getAboutMenuMessage(message.getChatId(), messageService.getReplyMessageText("reply.showMainMenu"));

        if (botState.equals(BotState.SHOW_ABOUT_MENU) && (message.getText().equals("О компании") || message.getText().equals("Компания туралы"))){
            return replyToUser;
        }

        if (botState.equals(BotState.SHOW_ABOUT_MENU) && (message.getText().equals("О нас") || message.getText().equals("Біз туралы"))) {
            replyToUser = messageService.getReplyMessage(chatId, "reply.about.us");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ABOUT_US);
            return replyToUser;
        }

        if (botState.equals(BotState.SHOW_ABOUT_MENU) && (message.getText().equals("Новости компании") || message.getText().equals("Компания жаңалықтары"))) {
            replyToUser = messageService.getReplyMessage(chatId,"reply.about.news");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_ABOUT_NEWS);
            return replyToUser;
        }

        if (botState.equals(BotState.SHOW_ABOUT_MENU) && (message.getText().equals("О чат-ботах") || message.getText().equals("Чат бот туралы"))){
            userDataCache.setUsersCurrentBotState(userId,BotState.SHOW_MAIN_MENU);
            replyToUser = mainMenuService.getMainMenuMessage(chatId,messageService.getReplyMessageText("reply.about.chatBots"));
            return replyToUser;
        }

        userDataCache.setUsersCurrentBotState(userId,BotState.SHOW_MAIN_MENU);
        replyToUser = mainMenuService.getMainMenuMessage(chatId,messageService.getReplyMessageText("reply.showMainMenu"));
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_ABOUT_MENU;
    }


}