package kz.iitu.bb1.cache;

import kz.iitu.bb1.botapi.BotState;
import kz.iitu.bb1.botapi.handlers.botHandler.UserProfileData;

public interface DataCache {
    void setUsersCurrentBotState(Long userId, BotState botState);

    BotState getUsersCurrentBotState(Long userId);

    UserProfileData getUserProfileData(Long userId);

    void saveUserProfileData(Long userId, UserProfileData userProfileData);
}
