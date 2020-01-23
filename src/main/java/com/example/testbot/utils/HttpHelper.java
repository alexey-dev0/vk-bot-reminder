package com.example.testbot.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HttpHelper {
    public static final HttpClient httpClient;

    static {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    public static File BytesToFile(String prefix, String suffix, byte[] bytes) throws IOException {
        var file = Files.createTempFile(prefix, suffix).toFile();
        var os = new FileOutputStream(file);
        os.write(bytes);
        os.close();
        return file;
    }

    public static byte[] downloadFile(URL url) throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url.toURI())
                .GET()
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(HttpResponse::body)
                .get(10, TimeUnit.SECONDS);
    }
}
