package bot;

import commands.*;
import enums.UserStates;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import services.*;

import java.util.HashMap;
import java.util.Map;

public class MainBot extends TelegramLongPollingBot {

    private final Map<String, CommandHandler> commandHandler = new HashMap<>();
    private final UserStateService userStates = new UserStateService();
    private final MovieService movieService = new MovieService();
    private final Map<UserStates, StateHandler> stateHandlers = new HashMap<>();
    private final TextSenderService senderService;
    private final MovieByYearHandler movieByYearHandler;


    public MainBot(String botToken) {
        super(botToken);
        this.senderService = new TextSenderService(this);
        this.movieByYearHandler = new MovieByYearHandler(senderService, userStates, movieService);
        registerCommands();
        registerStatesHandlers();
    }

    @Override
    public String getBotUsername() {
        return "FortAkumaBot";
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;
        {
            Message msg = update.getMessage();
            String text = msg.getText();
            long userId = msg.getFrom().getId();

            UserStates state = userStates.getState(userId);

            if (isCommand(text)) {
                handleCommand(userId, text);
                return;
            }

            StateHandler handler = stateHandlers.get(state);
            if (handler != null) {
                handler.handle(userId, text);
            }

        }
    }

    private void registerCommands() {
        ScheduleCommandHandler workoutHandler = new ScheduleCommandHandler(senderService);

        commandHandler.put("/start", new StartCommandHandler(senderService, userStates));
        commandHandler.put("/randomMovie", new RandomMovieCommandHandler(senderService, movieService, userStates));
        commandHandler.put("/movieByYear", new RandomMovieByYearCommandHandler(senderService, userStates));
        commandHandler.put("/workout", workoutHandler);
        commandHandler.put("/stopWorkout", workoutHandler::stop);
    }

    private void registerStatesHandlers() {
        stateHandlers.put(UserStates.WAITING_FOR_MOVIEBYYEAR, movieByYearHandler);
    }

    private void handleCommand(long userId, String command) {
        CommandHandler handler = commandHandler.get(command);
        if (handler != null) {
            handler.handle(userId);
        } else {
            senderService.sendText(userId, "Неизвестаня команда");
        }
    }

    private boolean isCommand(String text) {
        return text.startsWith("/");
    }

}
