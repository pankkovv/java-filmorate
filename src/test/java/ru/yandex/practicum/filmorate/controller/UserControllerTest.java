package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.SpringApplication;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public class LocalDateAdapter extends TypeAdapter<LocalDate> {
        private final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        private final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDate localDateTime) throws IOException {
            jsonWriter.value(localDateTime.format(formatterWriter));
        }

        @Override
        public LocalDate read(final JsonReader jsonReader) throws IOException {
            return LocalDate.parse(jsonReader.nextString(), formatterReader);
        }
    }
    HttpClient client;
    HttpResponse.BodyHandler<String> handler;
    User userEmpty;
    User userErrOne;
    User userErrTwo;
    User userErrThree;
    InMemoryUserStorage usersNormal = new InMemoryUserStorage();

    @BeforeAll
    public static void start() {
        SpringApplication.run(FilmorateApplication.class);
    }

    @BeforeEach
    public void assistant() {
        handler = HttpResponse.BodyHandlers.ofString();
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        userEmpty = User.builder().birthday(LocalDate.of(2000, 1, 1)).build();
        userErrOne = User.builder().id(3).email("errTestya.ru").login("err").name("err").birthday(LocalDate.of(2000, 1, 1)).build();
        userErrTwo = User.builder().id(4).email("errTest@ya.ru").login(" ").name("err").birthday(LocalDate.of(2000, 1, 1)).build();
        userErrThree = User.builder().id(5).email("errTest@ya.ru").login("err").name("err").birthday(LocalDate.of(2024, 1, 1)).build();
    }

    //С неверными данными пользователя.
    @Test
    public void validationErrorTest() throws IOException, InterruptedException {
        HttpResponse<String> responseCreateTaskOne = client.send(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(userErrOne)))
                .uri(URI.create("http://localhost:8080/users"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        HttpResponse<String> responseCreateTaskTwo = client.send(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(userErrTwo)))
                .uri(URI.create("http://localhost:8080/users"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        HttpResponse<String> responseCreateTaskThree = client.send(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(userErrThree)))
                .uri(URI.create("http://localhost:8080/users"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        HttpResponse<String> responsePutTaskOne = client.send(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(userErrOne)))
                .uri(URI.create("http://localhost:8080/users"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        HttpResponse<String> responsePutTaskTwo = client.send(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(userErrTwo)))
                .uri(URI.create("http://localhost:8080/users"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        HttpResponse<String> responsePutTaskThree = client.send(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(userErrThree)))
                .uri(URI.create("http://localhost:8080/users"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responseCreateTaskOne.body());
        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responseCreateTaskTwo.body());
        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responseCreateTaskThree.body());

        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responsePutTaskOne.body());
        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responsePutTaskOne.body());
        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responsePutTaskThree.body());
}

    //Без данных пользователя
    @Test
    public void validationEmptyTest() throws IOException, InterruptedException {
        HttpResponse<String> responseCreateTaskOne = client.send(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(userEmpty)))
                .uri(URI.create("http://localhost:8080/users"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        HttpResponse<String> responsePutTaskOne = client.send(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(userEmpty)))
                .uri(URI.create("http://localhost:8080/users"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responseCreateTaskOne.body());
        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responsePutTaskOne.body());
    }
}