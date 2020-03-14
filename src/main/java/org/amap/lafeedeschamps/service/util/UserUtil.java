package org.amap.lafeedeschamps.service.util;

import org.amap.lafeedeschamps.domain.User;
import org.apache.commons.lang3.StringUtils;

public class UserUtil {

    public static String formatFirstName(User user) {
        if (StringUtils.isNotBlank(user.getFirstName())) {
            return user.getFirstName();
        } else {
            return user.getLogin();
        }
    }

}
