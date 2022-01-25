package kz.iitu.bb1.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotStateContext {
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (isBotHandleState(currentState)) {
            return messageHandlers.get(BotState.BOT_HANDLER);
        }
        if (isAskContactHandleState(currentState)) {
            return messageHandlers.get(BotState.ASK_CONTACT);
        }
        return messageHandlers.get(currentState);
    }

    private boolean isBotHandleState(BotState currentState) {
        if(currentState!=BotState.SHOW_MAIN_MENU && currentState!=BotState.SHOW_ABOUT_MENU && currentState!=BotState.ASK_CONTACT && currentState!=BotState.ASK_CONTACT_NAME){
            return true;
        }
        return false;
    }

    private boolean isAskContactHandleState(BotState currentState) {
        if(currentState==BotState.ASK_CONTACT || currentState==BotState.ASK_CONTACT_NAME){
            return true;
        }
        return false;
    }
}
