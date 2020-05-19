package es.um.asio.service.util;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.regex.Pattern;


import static java.util.regex.Pattern.matches;

public class Utils {
    public static String getUUIDFromString(String s) {
        return  UUID.nameUUIDFromBytes(s.getBytes()).toString();
    }

    public static boolean isValidURL(String url) {
        try
        {
            URL i_url = new URL(url);
            i_url.toURI();
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
        if (s==null || s.equals(""))
            return false;
        else
            return true;
    }

    public static String generateUUIDFromOject(Object o) throws NoSuchAlgorithmException {
        String hash = Integer.valueOf(o.hashCode()).toString();
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


}
