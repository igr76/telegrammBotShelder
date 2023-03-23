package com.example.test.mapper;

import org.mapstruct.Mapper;
import pro.sky.whiskerspawstailtelegrambot.entity.Volunteer;
import pro.sky.whiskerspawstailtelegrambot.record.VolunteerRecord;

import java.util.Collection;

/**
 * маппер для {@link pro.sky.whiskerspawstailtelegrambot.entity.Volunteer}
 * готовый рекорд {@link pro.sky.whiskerspawstailtelegrambot.mapper.VolunteerMapperImpl}
 */
@Mapper(componentModel = "spring")
public interface VolunteerMapper {

    Volunteer toEntity(VolunteerRecord volunteerRecord);

    VolunteerRecord toRecord(Volunteer volunteers);

    Collection<Volunteer> toEntityList(Collection<VolunteerRecord> volunteerRecord);

    Collection<VolunteerRecord> toRecordList(Collection<Volunteer> volunteers);
}
