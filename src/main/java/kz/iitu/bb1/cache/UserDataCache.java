package kz.iitu.bb1.cache;

import kz.iitu.bb1.botapi.BotState;
import kz.iitu.bb1.botapi.handlers.botHandler.UserProfileData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache{

    private Map<Long,BotState> usersBotStates = new HashMap<>();
    private Map<Long,UserProfileData> usersProfileData = new HashMap<>();

    @Override
    public void setUsersCurrentBotState(Long userId, BotState botState) {
        usersBotStates.put(userId,botState);
    }

    @Override
    public BotState getUsersCurrentBotState(Long userId) {
        BotState botState = usersBotStates.get(userId);
        if(botState==null){
            botState = BotState.ASK_NAME;
        }

        return botState;
    }

    @Override
    public UserProfileData getUserProfileData(Long userId) {
        UserProfileData userProfileData = usersProfileData.get(userId);
        if (userProfileData==null){
            userProfileData = new UserProfileData();
        }
        return userProfileData;
    }

    @Override
    public void saveUserProfileData(Long userId, UserProfileData userProfileData) {
        usersProfileData.put(userId, userProfileData);
    }
}
