package com.example.test.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.base.Objects;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;

/**
 * Класс сущность, взаимодействует с таблицей БД adoptive_parent, отвечает за пользователя, который
 * хочет взять животное к себе.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "adoptive_parent")
@Slf4j
public class AdoptiveParent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    /**
     * Поле полного имени
     */
    @Column(name = "full_name")
    String fullName;
    /**
     * Поле телефона
     */
    @Column(name = "phone")
    String phone;

    /**
     * явялется ли усыновителем или нет
     */
    @Column(name = "is_parent")
    boolean isParent;
    /**
     * Состояние (этапы) по которому проходит пользователь, от первоначального взятия животного, до
     * полного одобрения со стороны приюта
     */
    @Column(name = "state")
    String state;
    /**
     * chat id для отправки обратного сообщения
     */
    @Column(name = "chat_id")
    Long chatId;

    /**
     * Список питомцев
     */
    @OneToMany(mappedBy = "adoptiveParent", fetch = FetchType.EAGER)
    @JsonBackReference
    List<Pet> pets;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdoptiveParent that = (AdoptiveParent) o;
        return Objects.equal(id, that.id) && Objects.equal(
            fullName, that.fullName) && Objects.equal(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, fullName, phone);
    }
}
