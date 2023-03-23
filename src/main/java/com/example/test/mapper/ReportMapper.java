package com.example.test.mapper;

import org.mapstruct.Mapper;
import pro.sky.whiskerspawstailtelegrambot.entity.AdoptiveParent;
import pro.sky.whiskerspawstailtelegrambot.entity.Report;
import pro.sky.whiskerspawstailtelegrambot.record.ReportRecord;

import java.util.Collection;

/**
 * маппер для {@link AdoptiveParent}
 * готовый рекорд {@link pro.sky.whiskerspawstailtelegrambot}
 */
@Mapper(componentModel = "spring")
public interface ReportMapper {
    Report toEntity(ReportRecord reportRecord);


    ReportRecord toRecord(Report report);

    Collection<Report> toEntityList(Collection<ReportRecord> reportRecords);

    Collection<ReportRecord> toRecordList(Collection<Report> reports);
}
