package commands;

import entites.Movie;
import services.MovieService;
import services.TextSenderService;
import services.UserStateService;

import java.time.Year;
import java.util.Arrays;
import java.util.stream.Collectors;

import static services.MovieService.GENRE_MAP;

public class MovieByYearHandler implements StateHandler {

    private final TextSenderService textSenderService;
    private final UserStateService userStates;
    private final MovieService movieService;

    public MovieByYearHandler(TextSenderService textSenderService, UserStateService userStates, MovieService movieService) {
        this.textSenderService = textSenderService;
        this.userStates = userStates;
        this.movieService = movieService;
    }

    public void handle(long userId, String text) {

        int year;

        try{
            year = Integer.parseInt(text);
            int currentYear = Year.now().getValue();
            if(year < 1950 || year > currentYear + 1) {
                textSenderService.sendText(userId, "Введите корректный год");
                return;
            }
        } catch (NumberFormatException e) {
            textSenderService.sendText(userId, "Год должен быть числом");
            return;
        }

        userStates.resetState(userId);

        textSenderService.sendTemporaryText(userId, () -> {

            Movie movie = movieService.getRandomMovieByYear(year);

            if (movie == null) {
                textSenderService.sendText(userId, "Что-то пошло не так...");
                return;
            }

            String posterUrl = movie.posterPath == null || movie.posterPath.isEmpty() ?
                    "https://upload.wikimedia.org/wikipedia/commons/7/75/No_image_available.png" : movie.getFullPosterPath();
            String genres = Arrays.stream(movie.genre_ids)
                    .mapToObj(i -> GENRE_MAP.getOrDefault(i, "Неизвестно"))
                    .collect(Collectors.joining(","));
            String caption = ("🎬 *" + movie.title + "* (" + movie.getReleaseYear() + ")\n" +
                    "⭐️ Рейтинг: "  + movie.votes + "\n" +
                    "🎭 Жанры: " + genres + "\n\n" +
                    "• " + movie.overview);

            textSenderService.sendPhoto(userId, posterUrl, caption);

        }, "Получаю для вас фильм...");
    }
}
