package commands;

import enums.UserStates;
import services.TextSenderService;
import services.UserStateService;


public class RandomMovieByYearCommandHandler implements CommandHandler {

    private final TextSenderService textSenderService;
    private final UserStateService userStates;

    public RandomMovieByYearCommandHandler(TextSenderService textSenderService, UserStateService userStates) {
        this.textSenderService = textSenderService;
        this.userStates = userStates;
    }

    @Override
    public void handle(long userId) {
        userStates.setState(userId, UserStates.WAITING_FOR_MOVIEBYYEAR);
        textSenderService.sendText(userId, "Введите год");
    }
}
