package kz.iitu.bb1.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class ReplyMessageService {

    private LocaleMessageService localeMessageService;

    public ReplyMessageService(LocaleMessageService localeMessageService){
        this.localeMessageService = localeMessageService;
    }

    public SendMessage getReplyMessage(Long chat_id, String replyMessage){
        return new SendMessage(chat_id.toString(),localeMessageService.getMessage(replyMessage));
    }
    public String getReplyMessageText( String replyMessage){
        return localeMessageService.getMessage(replyMessage);
    }
    public SendMessage getReplyMessage(Long chat_id, String replyMessage, Object... args){
        return new SendMessage(chat_id.toString(),localeMessageService.getMessage(replyMessage, args));
    }

}
