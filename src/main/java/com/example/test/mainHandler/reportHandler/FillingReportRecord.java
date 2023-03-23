package com.example.test.mainHandler.reportHandler;

import pro.sky.whiskerspawstailtelegrambot.record.PetRecord;
import pro.sky.whiskerspawstailtelegrambot.record.ReportRecord;
import pro.sky.whiskerspawstailtelegrambot.service.ReportService;
import pro.sky.whiskerspawstailtelegrambot.util.ParserToBot;

import static pro.sky.whiskerspawstailtelegrambot.util.StateReport.WAIT_ID_PET_REPORT;


public class FillingReportRecord {

  /**
   * Метод создает отчет и меняет  StateReport на WAIT_ID_PET_REPORT
   * @return новый ReportRecord
   */
  public ReportRecord newReport(){

    ReportRecord reportRecord = new ReportRecord();
    reportRecord.setStateReport(WAIT_ID_PET_REPORT);
    return reportRecord;

  }

  public PetRecord checkCorrectPetId(String textMessage, ReportService reportService){

    ParserToBot parserToBot = new ParserToBot();
    Long petId = parserToBot.parserStringPetId(textMessage);

    PetRecord petRecord = petId != null ? reportService.getPetById(petId) : null;
   return petRecord;

  }



//  public ReportRecord saveReportInDb(Message message, ReportRecord reportRecord) {
//
//    long chatId = message.getChatId();
//    reportService.addReport();
//
//
//
//  }



}
