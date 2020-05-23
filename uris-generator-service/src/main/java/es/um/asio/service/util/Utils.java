package es.um.asio.service.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.internal.StringUtil;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.regex.Pattern;

public class Utils {

    private Utils(){}

    public static String getUUIDFromString(String s) {
        return  UUID.nameUUIDFromBytes(s.getBytes()).toString();
    }

    public static boolean isValidURL(String url) {
        try
        {
            URL iUri = new URL(url);
            iUri.toURI();
            return true;
        } catch (Exception exception)
        {
            return false;
        }
    }

    public static boolean isValidUUID(String uuid) {
        String pattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        return Pattern.matches(pattern, uuid);
    }

    public static boolean isValidString(String s) {
        return  !(s==null || s.equals(""));
    }

    public static String generateUUIDFromOject(Object o) throws NoSuchAlgorithmException {
        String hash = Integer.toString(o.hashCode());
        MessageDigest sha = null;
        sha = MessageDigest.getInstance("SHA-1");
        byte[] result =  sha.digest(hash.getBytes());
        UUID uuid = UUID.nameUUIDFromBytes(result);
        return uuid.toString();
    }

    public static String getClassNameFromPath(String path) {
        if (path == null )
            return null;
        else if (path.contains(".")) {
            String[] pathParts = path.split("\\.");
            return pathParts[pathParts.length-1];
        } else
            return path;
    }

    public static String toConceptFormat(String concept) {
        if (!isValidString(concept) || StringUtils.isAllLowerCase(concept)) {
            return concept;
        } else {
            return StringUtils.capitalize(toASIONormalization(concept).toLowerCase());
        }
    }

    public static String toASIONormalization(String token) {
        StringBuffer formatWord = new StringBuffer();
        if (isValidString(token)) {
            token = StringUtils.stripAccents(StringUtils.deleteWhitespace(token));
            formatWord.append(token.charAt(0));
            for (int i = 1; i < token.length(); i++) {
                if (Character.isUpperCase(token.charAt(i)) && Character.isLowerCase(token.charAt(i-1))) {
                    formatWord.append('-');
                }
                formatWord.append(token.charAt(i));
            }
            return formatWord.toString();
        } else
            return token;
    }


}
