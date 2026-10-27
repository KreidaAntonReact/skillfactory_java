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
    private String lat;
    private String lon;
    private String limit;
    private JsonNode response;

    public void run() {
        System.out.println("Привет! Прежде чем наченм давай сначала введем некоторые параметры.");

        scanner.useLocale(Locale.US);

        lat = this.setParams("Укажи пожалуйста широту в DD: ", false);
        lon = this.setParams("Укажи пожалуйста долготу в DD: ", false);
        limit = this.setParams(
                "Можешь указать на сколько дней тебе нужен прогноз или нажать Enter для пропуска",
                true
        );

        System.out.println(this.getParamsMessage());

        response = client.getRequest(this.generateUrl(lat, lon, limit), this.getHeaders());

        this.showResponseMessage();
    }

    private void showResponseMessage() {
        Number temp = response.path("fact").path("temp").asInt();
        String averageTemperature = this.getAverageTemperature(response, limit);

        System.out.println("Полная информация о погоде: " + response);
        System.out.println("Текущая температура: " + temp);

        if (averageTemperature != null) {
            System.out.println("Средняя температура по прогнозу: " + averageTemperature);
        }
    }

    private String getParamsMessage() {
        String paramsMessage = String.format("Сейчас будет выполнен запрос по твоим координатам, lat: %s lon: %s", lat, lon);

        if(!limit.isEmpty()) {
            paramsMessage = String.format(paramsMessage + " и прогноз на %s дней", limit);
        }

        return paramsMessage;
    }

    private String generateUrl(String lat, String lon, String limit) {
        String baseUrl = "https://api.weather.yandex.ru/v2/forecast";
        String queryString = String.format("?lat=%s&lon=%s",
                URLEncoder.encode(String.valueOf(lat), StandardCharsets.UTF_8),
                URLEncoder.encode(String.valueOf(lon), StandardCharsets.UTF_8)
        );

        if (!limit.isEmpty()) {
            queryString += String.format("&limit=%s", URLEncoder.encode(limit, StandardCharsets.UTF_8));
        }

        return URI.create(baseUrl + queryString).toString();
    }

    private String getAverageTemperature(JsonNode response, String limit) {
        if (limit.isEmpty()) {
            return null;
        }

        JsonNode forecasts = response.path("forecasts");
        int daysCount = Math.min(Integer.parseInt(limit), forecasts.size());
        int temperatureSum = 0;

        for (int i = 0; i < daysCount; i++) {
            temperatureSum += forecasts.get(i).path("parts").path("day").path("temp_avg").asInt();
        }

        Double answer = (double) temperatureSum / daysCount;

        return String.format(Locale.US,"%.2f", answer);
    }

    private String[] getHeaders() {
        String token = "1833f2e2-ae08-4df1-958a-3355b7410979";

        return new String[]{"X-Yandex-Weather-Key", token};
    }

    private String setParams(String message, Boolean isOption) {
        System.out.println(message);

        String params = scanner.nextLine().trim();

        if (isOption && params.isEmpty()) {
            System.out.println("Пропускаем этот параметр.");
            return params;
        }

        while (isOption && !isPositiveInteger(params)) {
            System.out.println("Ты ввел что-то не то... нужно положительное целое число или пустая строка, чтобы пропустить");
            params = scanner.nextLine().trim();

            if (params.isEmpty()) {
                System.out.println("Пропускаем этот параметр.");
                return params;
            }
        }

        while (!isOption && !isDouble(params)) {
            System.out.println("Ты ввел что-то не то... давай попробуем еще раз");
            params = scanner.nextLine().trim();
        }

        System.out.println("Отлично все правильно! Давай дальше!");
        return params;
    }

    private boolean isPositiveInteger(String value) {
        try {
            return Integer.parseInt(value) > 0;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }
}
