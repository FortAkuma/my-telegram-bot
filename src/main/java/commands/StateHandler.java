package commands;

public interface StateHandler {
    void handle(long userId, String text);
}
