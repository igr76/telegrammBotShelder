package com.example.test.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.base.Objects;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Используется для хранения информации о приютах:
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    //Название приюта
    String name;
    //Адрес
    String address;
    //Телефон
    String phone;
    //Время работы приюта
    String workingTime;
    //Информация о приюте
    String aboutShelter;
    //Информация о технике безопасности
    String shelterSafetyEquipment;
    //Правила знакомства с собакой
    String ruleOfMeeting;
    //Список документов, необходимых для того, чтобы взять собаку из приюта
    @ElementCollection
    @CollectionTable(name = "shelter_list_of_doc", joinColumns = @JoinColumn(name = "shelter_id"))
    @Column(name = "list_if_doc")
    List<String> listOfDoc;
    //Рекомендации по транспортировке животного
    String recOfTransportation;
    //Рекомендации по обустройству дома для щенка
    String homeImprovementForPuppy;
    //Рекомендации по обустройству дома для взрослой собаки
    String homeImprovementForPet;
    //Рекомендации по обустройству дома для взрослой собаки с ограниченными возможностями
    String homeImprovementForPetWithDisabilities;
    //Советы кинолога по первичному общению с собакой
    String cynologistAdvice;
    //Список рекомендуемых приютом кинологов
    @ElementCollection
    @CollectionTable(name = "shelter_cynologist", joinColumns = @JoinColumn(name = "shelter_id"))
    @Column(name = "cynologist")
    List<String> cynologist;
    //Список причин, по которым могут отказать и не дать забрать собаку
    @ElementCollection
    @CollectionTable(name = "shelter_list_of_reason_for_rejection", joinColumns = @JoinColumn(name = "shelter_id"))
    @Column(name = "list_of_reason_for_rejection")
    List<String> listOfReasonForRejection;
    //Список собак, которые принадлежат приюту
    @OneToMany(mappedBy = "shelter", fetch=FetchType.EAGER)
    @JsonBackReference
    Set<Pet> pets;
    //Список волонтеров приюта
    @OneToMany(mappedBy = "shelter", fetch=FetchType.EAGER)
    @JsonBackReference
    Set<Volunteer> volunteers;
    //Схема проезда
    byte[] locationMap;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shelter shelter = (Shelter) o;
        return Objects.equal(id, shelter.id)
            && Objects.equal(name, shelter.name)
            && Objects.equal(address, shelter.address)
            && Objects.equal(phone, shelter.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, address, phone);
    }
}
