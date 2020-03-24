package by.sivko.touristbot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "state")
@NoArgsConstructor
@Data
public class TouristUserState {

    @EmbeddedId
    private TouristUser touristUser;

    private int state;

    public TouristUserState(Long userId, Long chatId, int state) {
        this.touristUser = new TouristUser(userId, chatId);
        this.state = state;
    }

    public TouristUserState(TouristUser touristUser, int state) {
        this.touristUser = touristUser;
        this.state = state;
    }
}
