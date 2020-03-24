package by.sivko.touristbot.repository;

import by.sivko.touristbot.entity.TouristUser;
import by.sivko.touristbot.entity.TouristUserState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TouristStateRepository extends JpaRepository<TouristUserState, TouristUser> {
}
