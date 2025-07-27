package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entites.Movie;
import entites.MovieResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MovieService {
    private static final String API_KEY = "b04ac2f6905787ac34bc346b7969dea3";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/top_rated";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public Movie getRandomMovie() {
        int page = new Random().nextInt(11) + 1;
        String url = BASE_URL + "?api_key=" + API_KEY + "&language=ru-RU&page=" + page;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        MovieResponse movieList;
        try {
            movieList = mapper.readValue(response.body(), MovieResponse.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }

        if (movieList.results == null || movieList.results.isEmpty()) {
            return null;
        }

        return movieList.results.get(new Random().nextInt(movieList.results.size()));
    }

    public Movie getRandomMovieByYear(int year) {
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY +
                "&language=ru-RU&sort_by=vote_average.desc&include_adult=false&include_video=false" +
                "&primary_release_year=" + year + "&vote_count.gte=5500";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        MovieResponse movieList;
        try {
            movieList = mapper.readValue(response.body(), MovieResponse.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }

        if (movieList.results == null || movieList.results.isEmpty()) {
            return null;
        }

        return movieList.results.get(new Random().nextInt(movieList.results.size()));

    }

    public static final Map<Integer, String> GENRE_MAP = new HashMap<>();

    static {
        GENRE_MAP.put(28, "Боевик");
        GENRE_MAP.put(12, "Приключения");
        GENRE_MAP.put(16, "Мультфильм");
        GENRE_MAP.put(35, "Комедия");
        GENRE_MAP.put(80, "Криминал");
        GENRE_MAP.put(99, "Документальный");
        GENRE_MAP.put(18, "Драма");
        GENRE_MAP.put(10751, "Семейный");
        GENRE_MAP.put(14, "Фэнтези");
        GENRE_MAP.put(36, "Исторический");
        GENRE_MAP.put(27, "Ужасы");
        GENRE_MAP.put(10402, "Музыка");
        GENRE_MAP.put(9648, "Мистика");
        GENRE_MAP.put(10749, "Мелодрама");
        GENRE_MAP.put(878, "Научная фантастика");
        GENRE_MAP.put(10770, "Телефильм");
        GENRE_MAP.put(53, "Триллер");
        GENRE_MAP.put(10752, "Военный");
        GENRE_MAP.put(37, "Вестерн");
    }
 }



