package com.example.testbot.utils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.example.testbot.utils.HttpHelper.httpClient;
import static com.example.testbot.utils.HttpHelper.ofFormData;

public class YaSpeechKitUtil {
    private static final String yandexApiKey;

    static {
        var prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/resources/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        yandexApiKey = prop.getProperty("yandexApiKey");
    }

    public static File Synthesis(String message) throws InterruptedException, ExecutionException, TimeoutException, IOException {
        Map<Object, Object> data = new HashMap<>();
        /*
        Текст, который нужно озвучить, в кодировке UTF-8.
        Для передачи слов-омографов используйте + перед ударной гласной. Например, гот+ов или def+ect.
        Чтобы отметить паузу между словами, используйте -.
        Ограничение на длину строки: 5000 символов.
         */
        data.put("text", message);

        /*
        Язык. Допустимые значения:
        ru-RU (по умолчанию) — русский язык;
        en-US — английский язык;
        tr-TR — турецкий язык.
         */
        //data.put("lang", "ru-RU");

        /*
        Желаемый голос для синтеза речи из списка. Значение параметра по умолчанию: oksana.
        Голос	    Язык    Пол
        oksana	    ru-RU	Ж
        jane	    ru-RU	Ж
        omazh	    ru-RU	Ж
        zahar	    ru-RU	M
        ermil	    ru-RU	M
        silaerkan	tr-TR	Ж
        erkanyavas	tr-TR	M
        alyss	    en-US	Ж
        nick	    en-US   M
        Премиум:
        alena	    ru-RU	Ж
        filipp	    ru-RU	M
         */
        data.put("voice", "filipp");

        /*
        Скорость (темп) синтезированной речи. Для премиум-голосов временно не поддерживается.
        Скорость речи задается дробным числом в диапазоне от 0.1 до 3.0. Где:
        3.0 — самый быстрый темп;
        1.0 (по умолчанию) — средняя скорость человеческой речи;
        0.1 — самый медленный темп.
         */
        //data.put("speed", "1.5");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(ofFormData(data))
                .uri(URI.create("https://tts.api.cloud.yandex.net/speech/v1/tts:synthesize"))
                .setHeader("Authorization", "Api-Key " + yandexApiKey)
                .build();

        var response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(HttpResponse::body)
                .get(10, TimeUnit.SECONDS);

        return HttpHelper.BytesToFile("voice_message", ".ogg", response);
    }

    public static String Recognition(byte[] voiceMessage) throws InterruptedException, ExecutionException, TimeoutException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://stt.api.cloud.yandex.net/speech/v1/stt:recognize"))
                .setHeader("Authorization", "Api-Key " + yandexApiKey)
                .POST(HttpRequest.BodyPublishers.ofByteArray(voiceMessage))
                .build();

        var response = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .get(10, TimeUnit.SECONDS);
        /* Ответ от сервера в формате:
            {
                result: "*разпознанная речь*"
            }
        */
        return new JSONObject(response).getString("result");
    }
}
