package com.example.test.mapper;

import org.mapstruct.Mapper;
import pro.sky.whiskerspawstailtelegrambot.entity.AdoptiveParent;
import pro.sky.whiskerspawstailtelegrambot.entity.Pet;
import pro.sky.whiskerspawstailtelegrambot.record.PetRecord;

import java.util.Collection;

/**
 * маппер для {@link AdoptiveParent}
 * готовый рекорд {@link PetMapperImpl}
 */
@Mapper(componentModel = "spring")
public interface PetMapper {
    Pet toEntity(PetRecord petRecord);

    PetRecord toRecord(Pet pet);

    Collection<Pet> toEntityList(Collection<PetRecord> petRecords);

    Collection<PetRecord> toRecordList(Collection<Pet> pets);
}
