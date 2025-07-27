package services;

import enums.UserStates;

import java.util.HashMap;
import java.util.Map;

public class UserStateService {
    private final Map<Long, UserStates> userStates = new HashMap<>();

    public UserStates getState(long userId) {
        return userStates.getOrDefault(userId, UserStates.IDLE);
    }

    public void setState(long userId, UserStates state) {
        userStates.put(userId, state);
    }

    public void resetState(long userId) {
        userStates.put(userId, UserStates.IDLE);
    }
}
