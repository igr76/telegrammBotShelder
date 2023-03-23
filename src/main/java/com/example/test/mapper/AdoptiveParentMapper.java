package com.example.test.mapper;

import org.mapstruct.Mapper;
import pro.sky.whiskerspawstailtelegrambot.entity.AdoptiveParent;
import pro.sky.whiskerspawstailtelegrambot.record.AdoptiveParentRecord;

import java.util.Collection;

/**
 * маппер для {@link pro.sky.whiskerspawstailtelegrambot.entity.AdoptiveParent}
 * готовый рекорд {@link pro.sky.whiskerspawstailtelegrambot.mapper.AdoptiveParentMapperImpl}
 */
@Mapper(componentModel = "spring")
public interface AdoptiveParentMapper {
    AdoptiveParent toEntity(AdoptiveParentRecord adoptiveParentRecord);


    AdoptiveParentRecord toRecord(AdoptiveParent adoptiveParent);

    Collection<AdoptiveParent> toEntityList(Collection<AdoptiveParentRecord> adoptiveParentRecord);

    Collection<AdoptiveParentRecord> toRecordList(Collection<AdoptiveParent> adoptiveParent);
}
