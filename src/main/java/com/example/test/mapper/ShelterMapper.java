package com.example.test.mapper;

import org.mapstruct.Mapper;
import pro.sky.whiskerspawstailtelegrambot.entity.Shelter;
import pro.sky.whiskerspawstailtelegrambot.record.ShelterRecord;

import java.util.Collection;

/**
 * маппер для {@link pro.sky.whiskerspawstailtelegrambot.entity.Shelter}
 *  * готовый рекорд {@link pro.sky.whiskerspawstailtelegrambot.mapper.ShelterMapperImpl}
 */
@Mapper(componentModel = "spring")
public interface ShelterMapper {

    Shelter toEntity(ShelterRecord shelterRecord);

    ShelterRecord toRecord(Shelter shelter);

    Collection<Shelter> toEntityList(Collection<ShelterRecord> shelterRecord);

    Collection<ShelterRecord> toRecordList(Collection<Shelter> shelter);
}
