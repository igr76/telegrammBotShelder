package com.example.test.record;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import pro.sky.whiskerspawstailtelegrambot.entity.Shelter;

import java.io.Serializable;

/**
 * A DTO for the {@link pro.sky.whiskerspawstailtelegrambot.entity.Volunteer} entity
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VolunteerRecord implements Serializable {
    Long id;
    String fullName;
    String phone;
    String infoVolunteer;
    String schedule;
    @JsonIgnore
    Shelter shelter;

    @Override
    public String toString() {
        return "Меня зовут " + fullName + ", мой телефон для связи " + phone +
                ", доступен : " + schedule + ".";
    }
}