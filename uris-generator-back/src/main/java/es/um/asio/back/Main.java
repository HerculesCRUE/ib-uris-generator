package es.um.asio.back;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> elements = Arrays.asList(new String[] {"$domain$", "$sub-domain$", "$type$", "$concept$", "$reference$" });
        Collections.shuffle(elements);
        System.out.println("http://"+String.join("/", elements));
    }
}
