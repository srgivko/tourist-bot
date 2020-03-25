package by.sivko.touristbot.telbot;

import by.sivko.touristbot.entity.TouristUser;

public interface TouristUserStateService {
    int getState(TouristUser touristUser);

    void save(TouristUser touristUser, int state);
}
