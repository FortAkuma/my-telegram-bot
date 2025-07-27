package commands;

import services.TextSenderService;
import services.UserStateService;


public class StartCommandHandler implements CommandHandler{

    private final TextSenderService senderService;
    private final UserStateService userStates;

    public StartCommandHandler(TextSenderService senderService, UserStateService userStates) {
        this.senderService = senderService;
        this.userStates = userStates;
    }

    @Override
    public void handle(long userId) {
        userStates.resetState(userId);
        String text = """
                Привет! 👋

                Вот что я умею:
                • Сделать скриншот сайта (/screenshot)
                • Получить случайный фильм (/randomMovie)
                • Получить фильм по году (/movieByYear)
                • Получать ежедневые тренировки (/workout) | для останоавки (/stopWorkout)
                """;
        senderService.sendText(userId, text);
    }
}
