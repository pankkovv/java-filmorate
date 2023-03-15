package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationTest {
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

    Film filmEmpty;
    Film filmErrOne;
    Film filmErrTwo;
    Film filmErrThree;
    Film filmErrFour;

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

        filmEmpty = Film.builder().releaseDate(LocalDate.of(2000, 1, 1)).build();
        filmErrOne = Film.builder().id(1).name("").description("testErr").releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        filmErrTwo = Film.builder().id(1).name("test").description("testdsflkfdgjkjdfhgjhfdjgjkdfgkjfdghjkdfhgjkdfjkghkdfhgkdfhghdfjkgkjdfhgkjhdfkjghkdfjhgkjdfhgjhdfkjghkjdfhgkjdfhgkhdfkjghkjdfhgkjdfhgkjdfhgjkdfkjghdfjkhgkjdfhgkjdfhkghdfkghkdfjhgkjhfgkjhdfkjghkjdfhgkjdfhgjhdfkgkdfjhgkjdfhkghdfkjghkjfdhgjkdfkjkgfkfgkjghkjdfkjfhkgjhgjkhfkj").releaseDate(LocalDate.of(2000, 1, 1)).duration(100).build();
        filmErrThree = Film.builder().id(1).name("test").description("test").releaseDate(LocalDate.of(1800, 1, 1)).duration(100).build();
        filmErrFour = Film.builder().id(1).name("test").description("test").releaseDate(LocalDate.of(2000, 1, 1)).duration(-100).build();
    }

    //С неверными данными пользователя.
    @Test
    public void validationErrorTestUser() throws IOException, InterruptedException {
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

    @Test
    public void validationErrorTestFilm() throws IOException, InterruptedException {
        HttpResponse<String> responseCreateFilmOne = client.send(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(filmErrOne)))
                .uri(URI.create("http://localhost:8080/films"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        HttpResponse<String> responseCreateFilmTwo = client.send(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(filmErrTwo)))
                .uri(URI.create("http://localhost:8080/films"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        HttpResponse<String> responseCreateFilmThree = client.send(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(filmErrThree)))
                .uri(URI.create("http://localhost:8080/films"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        HttpResponse<String> responseCreateFilmFour = client.send(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(filmErrFour)))
                .uri(URI.create("http://localhost:8080/films"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        HttpResponse<String> responsePutFilmOne = client.send(HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(filmErrOne)))
                .uri(URI.create("http://localhost:8080/films"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        HttpResponse<String> responsePutFilmTwo = client.send(HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(filmErrTwo)))
                .uri(URI.create("http://localhost:8080/films"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        HttpResponse<String> responsePutFilmThree = client.send(HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(filmErrThree)))
                .uri(URI.create("http://localhost:8080/films"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        HttpResponse<String> responsePutFilmFour = client.send(HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(filmErrFour)))
                .uri(URI.create("http://localhost:8080/films"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responseCreateFilmOne.body());
        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responseCreateFilmTwo.body());
        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responseCreateFilmThree.body());
        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responseCreateFilmFour.body());

        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responsePutFilmOne.body());
        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responsePutFilmTwo.body());
        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responsePutFilmThree.body());
        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responsePutFilmFour.body());
    }

    //Без данных пользователя
    @Test
    public void validationEmptyTestUser() throws IOException, InterruptedException {
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

    @Test
    public void validationEmptyTestFilm() throws IOException, InterruptedException {
        HttpResponse<String> responseCreateFilmOne = client.send(HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(filmEmpty)))
                .uri(URI.create("http://localhost:8080/films"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        HttpResponse<String> responsePutFilmOne = client.send(HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(filmEmpty)))
                .uri(URI.create("http://localhost:8080/films"))
                .version(HttpClient.Version.HTTP_1_1)
                .build(), handler);

        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responseCreateFilmOne.body());
        assertEquals("{\"error\":\"Произошла непредвиденная ошибка\"}", responsePutFilmOne.body());
    }
}
