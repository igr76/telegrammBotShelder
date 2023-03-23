package com.example.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

/**
 * Класс шаблона волонтера
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /** Информация о волонтерах */
    @Column(name = "info_volunteer")
    String infoVolunteer;
    /** Поле полное имя волонтера */
    @Column(name = "full_name")
    String fullName;
    /** Поле номер телефона волонтера */
    @Column(name = "phone")
    String phone;
    /** Расписание волонтера */
    @Column(name = "schedule")
    String schedule;

    /**
     * Приют, которому помогает волонтер
     */
    @ManyToOne(cascade=CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "shelter_id")
    Shelter shelter;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Volunteer volunteer = (Volunteer) o;
        return Objects.equal(id, volunteer.id)
            && Objects.equal(infoVolunteer, volunteer.infoVolunteer)
            && Objects.equal(fullName, volunteer.fullName)
            && Objects.equal(phone, volunteer.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, infoVolunteer, fullName, phone);
    }
}
