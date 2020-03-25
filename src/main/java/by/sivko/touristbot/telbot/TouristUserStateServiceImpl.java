package by.sivko.touristbot.telbot;

import by.sivko.touristbot.entity.TouristUser;
import by.sivko.touristbot.entity.TouristUserState;
import by.sivko.touristbot.repository.TouristStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TouristUserStateServiceImpl implements TouristUserStateService {

    private final TouristStateRepository touristStateRepository;

    @Autowired
    public TouristUserStateServiceImpl(TouristStateRepository touristStateRepository) {
        this.touristStateRepository = touristStateRepository;
    }

    @Override
    public int getState(TouristUser touristUser) {
        return this.touristStateRepository.findById(touristUser).map(TouristUserState::getState).orElse(0);
    }

    @Transactional
    @Override
    public void save(TouristUser touristUser, int state) {
        final Optional<TouristUserState> persistedTouristUserState = this.touristStateRepository.findById(touristUser);
        if (persistedTouristUserState.isPresent()) {
            persistedTouristUserState.get().setState(state);
        } else {
            this.touristStateRepository.save(new TouristUserState(touristUser, state));
        }
    }
}
