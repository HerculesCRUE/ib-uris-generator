package es.um.asio.back;

import org.checkerframework.checker.regex.qual.Regex;

import java.util.UUID;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        String uri = "/aaa/";
        String pattern = "^(\\/[a-z0-9_.~%-]*)*$";

        boolean matches = Pattern.matches(pattern, uri);

        System.out.println("matches = " + matches);
    }
}
