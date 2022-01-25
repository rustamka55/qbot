package kz.iitu.bb1.config;

import kz.iitu.bb1.MyBot;
import kz.iitu.bb1.botapi.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUsername;
    private String botToken;

    @Bean
    public MyBot myBotyBot(TelegramFacade telegramFacade){
        MyBot myBot = new MyBot(telegramFacade);
        myBot.setBotUsername(botUsername);
        myBot.setWebHookPath(webHookPath);
        myBot.setBotToken(botToken);

        return myBot;
    }

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
