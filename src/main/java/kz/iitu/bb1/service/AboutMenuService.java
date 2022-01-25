package kz.iitu.bb1.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class AboutMenuService {

    ReplyMessageService messageService;

    public AboutMenuService(ReplyMessageService messageService) {
        this.messageService = messageService;
    }

    public SendMessage getAboutMenuMessage(final Long chatId, final String textMessage){
        final ReplyKeyboardMarkup replyKeyboardMarkup = getAboutMenuKeyboard();
        final SendMessage aboutMenuMessage =
                createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);

        return aboutMenuMessage;
    }

    private ReplyKeyboardMarkup getAboutMenuKeyboard() {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add(new KeyboardButton(messageService.getReplyMessageText("reply.ask.about.us")));
        row2.add(new KeyboardButton(messageService.getReplyMessageText("reply.ask.about.news")));
        row3.add(new KeyboardButton(messageService.getReplyMessageText("reply.main.menu")));
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private SendMessage createMessageWithKeyboard(final Long chatId,
                                                  String textMessage,
                                                  final ReplyKeyboardMarkup replyKeyboardMarkup) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(textMessage);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }
}
