package commands;

import services.TextSenderService;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleCommandHandler implements CommandHandler {

    private static final Map<DayOfWeek, String> workoutPlan = new HashMap<>();
    private final TextSenderService textSenderService;
    private ScheduledExecutorService scheduler;

    static {
        workoutPlan.put(DayOfWeek.MONDAY, "Спина + плечи");
        workoutPlan.put(DayOfWeek.TUESDAY, "Грудь + трицепс");
        workoutPlan.put(DayOfWeek.WEDNESDAY, "Ноги (Квадрицепсы + Икры)");
        workoutPlan.put(DayOfWeek.THURSDAY, "Бицепс + предплечья + пресс");
        workoutPlan.put(DayOfWeek.FRIDAY, "Грудь + плечи");
        workoutPlan.put(DayOfWeek.SATURDAY, "Ноги (Бедра + Ягодицы)");
        workoutPlan.put(DayOfWeek.SUNDAY, "Спина + бицепс");
    }

    public ScheduleCommandHandler(TextSenderService textSenderService) {
        this.textSenderService = textSenderService;
    }

    @Override
    public void handle(long userId) {
        if (scheduler != null && !scheduler.isShutdown()) {
            textSenderService.sendText(userId, "Расписание уже запущено.");
            return;
        }

        scheduler = Executors.newSingleThreadScheduledExecutor();

        Runnable dailyTask = () -> {
            DayOfWeek today = LocalDate.now().getDayOfWeek();
            String message = workoutPlan.getOrDefault(today, "Отдых");
            textSenderService.sendText(userId, "Сегодня: " + message);
        };

        long initialDelay = Duration.between(LocalTime.now(), LocalTime.of(7, 0)).getSeconds();
        if (initialDelay < 0) initialDelay += 86400;

        scheduler.scheduleAtFixedRate(dailyTask, initialDelay, 86400, TimeUnit.SECONDS);

    }

    public void stop(long userId) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            textSenderService.sendText(userId, "Расписание остановлено.");
        } else {
            textSenderService.sendText(userId, "Расписание ещё не было запущено.");
        }
    }
}