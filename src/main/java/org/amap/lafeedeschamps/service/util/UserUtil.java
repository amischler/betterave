package org.amap.lafeedeschamps.service.util;

import org.amap.lafeedeschamps.domain.User;
import org.amap.lafeedeschamps.service.dto.UserDTO;
import org.apache.commons.lang3.StringUtils;

public class UserUtil {

    public static String formatFirstName(User user) {
        if (StringUtils.isNotBlank(user.getFirstName())) {
            return user.getFirstName();
        } else {
            return user.getLogin();
        }
    }

    public static String formatFirstName(UserDTO user) {
        if (StringUtils.isNotBlank(user.getFirstName())) {
            return user.getFirstName();
        } else {
            return user.getLogin();
        }
    }

}
