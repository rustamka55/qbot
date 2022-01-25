package kz.iitu.bb1.botapi.handlers.menu;

import kz.iitu.bb1.botapi.BotState;
import kz.iitu.bb1.botapi.InputMessageHandler;
import kz.iitu.bb1.service.MainMenuService;
import kz.iitu.bb1.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MainMenuHandler implements InputMessageHandler {
    private ReplyMessageService messageService;
    private MainMenuService mainMenuService;

    public MainMenuHandler(ReplyMessageService messageService, MainMenuService mainMenuService) {
        this.messageService = messageService;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(Message message) {
        return mainMenuService.getMainMenuMessage(message.getChatId(), messageService.getReplyMessageText("reply.showMainMenu"));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }


}