package ru.skillfactory.weather;

import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class Weather {
    private final Scanner scanner = new Scanner(System.in);
    private final Client client = new Client();

    public void run() {
        System.out.println("Привет! Прежде чем наченм давай сначала введем некоторые параметры.");

        scanner.useLocale(Locale.US);

        Float lat = setParams("Укажи пожалуйста широту в DD: ");
        Float lon = setParams("Укажи пожалуйста долготу в DD: ");

        System.out.println("Сейчас будет выполнен запрос по твоим координатам, lat: " + lat + " " + "lon: " + lon);

        String baseUrl = "https://api.weather.yandex.ru/v2/forecast";
        String queryString = String.format("?lat=%s&lon=%s",
                URLEncoder.encode(String.valueOf(lat), StandardCharsets.UTF_8),
                URLEncoder.encode(String.valueOf(lon), StandardCharsets.UTF_8)
        );

        String uri = URI.create(baseUrl + queryString).toString();
        String token = "1833f2e2-ae08-4df1-958a-3355b7410979";
        String[] headers = {"X-Yandex-Weather-Key", token};


        JsonNode response = client.getRequest(uri, headers);

        System.out.println(response);
    }

    private Float setParams(String message) {
        System.out.println(message);

        while (!scanner.hasNextFloat()) {
            System.out.println("Ты ввел что-то не то... давай попробуем еще раз");
            scanner.next();
        }


        System.out.println("Отлично все правильно! Давай дальше!");
        return scanner.nextFloat();
    }
}
