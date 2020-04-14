package es.um.asio.service.util;

import java.util.UUID;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.matches;

public class Utils {
    public static String getUUIDFromString(String s) {
        return  UUID.nameUUIDFromBytes(s.getBytes()).toString();
    }

    public static boolean isValidURL(String url) {
        String pattern = "^(\\/[a-z0-9_.~%-]*)*$";
        return Pattern.matches(pattern, url);
    }

    public static boolean isValidUUID(String uuid) {
        String pattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        return Pattern.matches(pattern, uuid);
    }
}
