package es.um.asio.back;

import org.checkerframework.checker.regex.qual.Regex;

import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        String uri = ("http://$domain$/$sub-domain$/$type$/$concept$/$reference$");
        uri = uri.replaceFirst("\\$domain","dominio");
        System.out.println(uri);
    }
}
