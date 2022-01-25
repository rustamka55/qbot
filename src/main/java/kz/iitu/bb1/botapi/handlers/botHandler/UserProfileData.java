package kz.iitu.bb1.botapi.handlers.botHandler;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileData {
    String name;
    String number;
    String lang;
    String contact;
}
