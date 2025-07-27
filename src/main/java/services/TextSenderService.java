package services;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;

public class TextSenderService {
    private final TelegramLongPollingBot bot;

    public TextSenderService(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public void sendText(long chatId, String text) {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText(text);
        try {
            bot.execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPhoto(long userId, String url, String caption) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(userId);
        photo.setPhoto(new InputFile(url));
        photo.setCaption(caption);
        photo.setParseMode("Markdown");

        try {
            bot.execute(photo);
        } catch (TelegramApiException e) {
            sendText(userId, "Ошибка при отправке фото");
        }
    }

    public void sendPhoto(long userId, byte[] imageBytes, String caption) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(userId);
        photo.setPhoto(new InputFile(new ByteArrayInputStream(imageBytes), "screenshot.png"));
        photo.setCaption(caption);

        try {
            bot.execute(photo);
        } catch (TelegramApiException e) {
            sendText(userId, "Ошибка при отправке фото");
        }
    }

    public void sendTemporaryText(long chatId, Runnable task, String text) {
        SendMessage loading = new SendMessage();
        loading.setChatId(chatId);
        loading.setText(text);

        Message loadingMsg;
        try {
            loadingMsg = bot.execute(loading);
        } catch (TelegramApiException e) {
            task.run();
            return;
        }

        try {
            task.run();
        } finally {
            try {
                deleteText(loadingMsg);
            } catch (TelegramApiException e) {
            }
        }
    }

    private void deleteText(Message msg) throws TelegramApiException {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(msg.getChatId());
        deleteMessage.setMessageId(msg.getMessageId());
        bot.execute(deleteMessage);
    }
}
