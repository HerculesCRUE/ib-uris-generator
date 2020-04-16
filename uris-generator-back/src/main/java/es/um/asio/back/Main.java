package es.um.asio.back;

import org.checkerframework.checker.regex.qual.Regex;

import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        Locale l = Locale.forLanguageTag("de-DE-u-co-phonebk");
        String country = l.getISO3Country();
        String lang = l.getISO3Language();
        System.out.println(l.toLanguageTag());
    }
}
