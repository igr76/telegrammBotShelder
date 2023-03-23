package com.example.test.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.base.Objects;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;


/**
 * Сущность для отчета о животном
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Report {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  /**
   * Рацион животного
   */
  @Column(name = "diet")
  String diet;

  /**
   * Общее самочувствие и привыкание к новому месту
   */
  @Column(name = "report_about_feelings")
  String reportAboutFeelings;

  /**
   * Изменение в поведении: отказ от старых привычек, приобретение новых
   */
  @Column(name = "report_about_habits")
  String reportAboutHabits;

  /**
   * Фото животного
   */
  @Column(name = "photo_pet")
  byte[] photoPet;

  /**
   * id питомца
   */
  @Column(name = "pet_id")
  Long pet_id;

  /**
   * Присоединение к питомцу
   */
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "pet_id", referencedColumnName = "id", insertable = false, updatable = false)
  @JsonBackReference
  Pet pet;

  /**
   * Этапы заполнения отчета
   */
  @Column(name = "state_report")
  String stateReport;


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Report report = (Report) o;
    return Objects.equal(id, report.id)
        && Objects.equal(diet, report.diet)
        && Objects.equal(reportAboutFeelings, report.reportAboutFeelings)
        && Objects.equal(reportAboutHabits, report.reportAboutHabits);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, diet, reportAboutFeelings, reportAboutHabits);
  }
}

