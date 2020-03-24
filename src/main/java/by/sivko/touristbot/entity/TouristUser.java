package by.sivko.touristbot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Data
public class TouristUser implements Serializable {

    private static final long serialVersionUID = 4173465687878445371L;

    @NotBlank
    private Long userId;

    @NotBlank
    private Long chatId;

    public TouristUser(Long userId, Long chatId) {
        this.userId = userId;
        this.chatId = chatId;
    }
}
