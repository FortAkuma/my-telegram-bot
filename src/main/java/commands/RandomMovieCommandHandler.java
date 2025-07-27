package commands;

import entites.Movie;
import services.MovieService;
import services.TextSenderService;
import services.UserStateService;

import java.util.Arrays;
import java.util.stream.Collectors;

import static services.MovieService.GENRE_MAP;

public class RandomMovieCommandHandler implements CommandHandler {

    private final TextSenderService textSenderService;
    private final MovieService movieService;
    private final UserStateService userStates;

    public RandomMovieCommandHandler(TextSenderService textSenderService, MovieService movieService, UserStateService userStates) {
        this.textSenderService = textSenderService;
        this.movieService = movieService;
        this.userStates = userStates;
    }

    @Override
    public void handle(long userId) {
        userStates.resetState(userId);
        textSenderService.sendTemporaryText(userId, () -> {
            Movie movie = movieService.getRandomMovie();

            if (movie == null) {
                textSenderService.sendText(userId, "Что-то пошло не так");
                return;
            }

            String posterUrl = movie.posterPath == null || movie.posterPath.isEmpty() ?
                    "https://upload.wikimedia.org/wikipedia/commons/7/75/No_image_available.png" : movie.getFullPosterPath();
            String genres = Arrays.stream(movie.genre_ids)
                    .mapToObj(i -> GENRE_MAP.getOrDefault(i, "Неизвестно"))
                    .collect(Collectors.joining(","));

            String caption = ("🎬 *" + movie.title + "* (" + movie.getReleaseYear() + ")\n" +
                    "⭐️ Рейтинг: " + movie.votes + "\n" +
                    "🎭 Жанры: " + genres + "\n\n" +
                    "• " + movie.overview);

            textSenderService.sendPhoto(userId, posterUrl, caption);


        }, "Получаю для вас фильм...");
    }
}
