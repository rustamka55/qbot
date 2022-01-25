package kz.iitu.bb1.controller;

import kz.iitu.bb1.MyBot;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebHookController {

    private final MyBot myBot;

    public WebHookController(MyBot myBot) {
        this.myBot = myBot;
    }

    @RequestMapping(value = "/",method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateRecieved(@RequestBody Update update){
        return myBot.onWebhookUpdateReceived(update);
    }
}
