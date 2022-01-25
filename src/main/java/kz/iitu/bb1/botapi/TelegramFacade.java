package kz.iitu.bb1.botapi;

import kz.iitu.bb1.botapi.handlers.botHandler.UserProfileData;
import kz.iitu.bb1.cache.UserDataCache;
import kz.iitu.bb1.service.LocaleMessageService;
import kz.iitu.bb1.service.MainMenuService;
import kz.iitu.bb1.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Locale;

@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private LocaleMessageService localeMessageService;
    private ReplyMessageService messageService;
    private MainMenuService mainMenuService;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache, LocaleMessageService localeMessageService,ReplyMessageService replyMessageService,MainMenuService mainMenuService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.localeMessageService = localeMessageService;
        this.messageService = replyMessageService;
        this.mainMenuService = mainMenuService;
    }

    public BotApiMethod<?> handleUpdate(Update update){
        SendMessage replyMessage = null;

        if(update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New CallbackQuery from User:{}, chatId: {}, with text: {}",update.getCallbackQuery().getFrom().getUserName(),callbackQuery.getFrom().getId(),update.getCallbackQuery().getData());
            return processCallbackQuery(callbackQuery);
        }

        Message message = update.getMessage();
        if(message!= null && message.hasText()){
            log.info("New info from User:{}, chatId: {}, with text: {}",message.getFrom().getUserName(),message.getChatId(),message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message){
        String inputMsg = message.getText();
        Long userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        switch (inputMsg){
            case "/start":
                botState = BotState.ASK_NAME;
                break;
            case "Главное меню":
            case "Басты мәзір":
                botState = BotState.SHOW_MAIN_MENU;
                break;
            case "О компании":
            case "Компания туралы":
            case "О нас":
            case "Новости компании":
            case "Біз туралы":
            case "Компания жаңалықтары":
            case "О чат-ботах":
            case "Чат бот туралы":
                botState = BotState.SHOW_ABOUT_MENU;
                break;
            case "Заказать обратный звонок":
            case "Қоңырау":
                botState = BotState.ASK_CONTACT;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery){
        final Long chatId = buttonQuery.getMessage().getChatId();
        final Long userId = buttonQuery.getFrom().getId();
        BotApiMethod<?> callBackAnswer;

        if (buttonQuery.getData().equals("buttonRu")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setLang("ru-Ru");
            localeMessageService.setLocale(Locale.forLanguageTag("ru-RU"));
            userDataCache.saveUserProfileData(userId, userProfileData);
            callBackAnswer = mainMenuService.getMainMenuMessage(chatId,messageService.getReplyMessageText("reply.showMainMenu"));
            log.info(String.valueOf(callBackAnswer));
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        } else{
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setLang("kk-KZ");
            localeMessageService.setLocale(Locale.forLanguageTag("kk-KZ"));
            userDataCache.saveUserProfileData(userId, userProfileData);
            callBackAnswer = mainMenuService.getMainMenuMessage(chatId,messageService.getReplyMessageText("reply.showMainMenu"));
            log.info(String.valueOf(callBackAnswer));
            log.info(String.valueOf(callBackAnswer));
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }

        return  callBackAnswer;
    }


}
