package com.example.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


/**
 * Питомцем. В базе данных pet .
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    /**
     * Кличка питомца
     */
    @Column(name = "name")
    String fullName;

    /**
     * При создании таблицы будет установлено ограничение: age > 0
     */
    String age;


    /**
     * Описание питомца
     */
    String description;


    /**
     * Путь по которому будеть храниться фото
     */
    @Column(name = "file_path")
    String filePath;

    /**
     * Указывается размер файлов
     */
    @Column(name = "size")
    long fileSize;

    /**
     * Медиатип
     */
    @Column(name = "type")
    String mediaType;

    /**
     * Фото
     */
    @Lob
    @Column(name = "photo")
    byte[] photo;
    @Column(name = "test_period")
    boolean testPeriod;

    @Column(name = "test_period_time")
    LocalDateTime testPeriodTime;


    /**
     * Приют, к которому принадлежит питомцем
     */
//    @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "shelter_id")
    Shelter shelter;

    /**
     * Хозяин питомца
     */
//    @ManyToOne(cascade = CascadeType.ALL)
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "adoptive_parent_id")
    AdoptiveParent adoptiveParent;


    /**
     * Отчеты хозяина для данному питомцу
     */
    @OneToMany(mappedBy = "pet", fetch=FetchType.EAGER)
//    @JsonManagedReference // Мешает заполнению через сваггер
    @JsonIgnore
    List<Report> reports;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pet pet = (Pet) o;
        return age == pet.age && Objects.equal(id, pet.id)
            && Objects.equal(fullName, pet.fullName)
            && Objects.equal(description, pet.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, fullName, age, description);
    }
}
