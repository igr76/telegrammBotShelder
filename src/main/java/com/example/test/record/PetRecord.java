package com.example.test.record;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import pro.sky.whiskerspawstailtelegrambot.entity.AdoptiveParent;
import pro.sky.whiskerspawstailtelegrambot.entity.Pet;
import pro.sky.whiskerspawstailtelegrambot.entity.Shelter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO for the {@link Pet} entity
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PetRecord implements Serializable {
    Long id;
    @NotNull
    String fullName;

    @NotNull
    String age;

    @NotNull
    String description;

    String filePath;

    long fileSize;

    String mediaType;

    byte[] photo;

    boolean testPeriod;

    LocalDateTime testPeriodTime;

    @JsonIgnore
    Shelter shelter;


    @JsonIgnore
    AdoptiveParent adoptiveParent;

    @JsonIgnore
    List<ReportRecord> reports;

}