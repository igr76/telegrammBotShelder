package com.example.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.whiskerspawstailtelegrambot.entity.Report;
import pro.sky.whiskerspawstailtelegrambot.exception.ElemNotFound;
import pro.sky.whiskerspawstailtelegrambot.mapper.ReportMapper;
import pro.sky.whiskerspawstailtelegrambot.record.AdoptiveParentRecord;
import pro.sky.whiskerspawstailtelegrambot.record.PetRecord;
import pro.sky.whiskerspawstailtelegrambot.record.ReportRecord;
import pro.sky.whiskerspawstailtelegrambot.repository.ReportRepository;
import pro.sky.whiskerspawstailtelegrambot.textAndButtonsAndKeyboard.ConfigKeyboard;
import pro.sky.whiskerspawstailtelegrambot.util.*;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@Slf4j
@Transactional
public class ReportService {

  private final PetService petService;
  private final FormReplyMessages formReplyMessages;
  private final ReportRepository reportRepository;
  private final AdoptiveParentService adoptiveParentService;
  private final ConfigKeyboard configKeyboard;
  private final ReportMapper reportMapper;

  public ReportService(PetService petService,
      FormReplyMessages formReplyMessages, ReportRepository reportRepository,
      AdoptiveParentService adoptiveParentService,
      ConfigKeyboard configKeyboard, ReportMapper reportMapper) {
    this.petService = petService;
    this.formReplyMessages = formReplyMessages;
    this.reportRepository = reportRepository;
    this.adoptiveParentService = adoptiveParentService;
    this.configKeyboard = configKeyboard;
    this.reportMapper = reportMapper;
  }


  /**
   * Получить отчет по его Id
   *
   * @param id Id отчета
   * @return отчет по его Id
   */
  public ReportRecord getReportById(long id) {
    Report report = reportRepository.findById(id).orElseThrow(() -> new ElemNotFound());
    return reportMapper.toRecord(report);
  }

  /**
   * Получить отчет по Id животного
   *
   * @param petId Id животного
   * @return отчет по id животного
   */
  public ReportRecord getReportByPetId(long petId) {

    Report report = reportRepository.getReportByPet_id(petId);
    ReportRecord reportRecord = null;
    if (report != null) {
      reportRecord = reportMapper.toRecord(report);
    }
    return reportRecord;
  }

  /**
   * Создать новый отчет и сохранить его в базе данных с привязкой к животному по его id
   *
   * @param petId Id животного
   * @return новй созданый отчет
   */
  public ReportRecord addNewReportInDbForPetByPetId(long petId) {
    Report report = new Report();
    report.setPet_id(petId);
    reportRepository.save(report);
    return reportMapper.toRecord(report);
  }

  /**
   * Обновить существующий отчет по его id
   *
   * @param id              id отчета
   * @param newReportRecord новый отчет
   * @return обновленный отчет
   */
  public ReportRecord updateReportByReportId(long id, ReportRecord newReportRecord) {

    ReportRecord oldReportRecord = getReportById(id);
    oldReportRecord.setPhotoPet(newReportRecord.getPhotoPet());
    oldReportRecord.setDiet(newReportRecord.getDiet());
    oldReportRecord.setReportAboutFeelings(newReportRecord.getReportAboutFeelings());
    oldReportRecord.setReportAboutHabits(newReportRecord.getReportAboutHabits());
    oldReportRecord.setStateReport(newReportRecord.getStateReport());

    return reportMapper.toRecord(reportRepository.save(reportMapper.toEntity(oldReportRecord)));
  }


  /**
   * Показать всех животных принадлежащих пользователю
   *
   * @param chatId чат id пользователя
   * @return Список животных в  текстовом формате для отправки пользователю.
   */
  public String showAllAdoptedPets(long chatId) {
    log.info("Вызов метода " + new Throwable()
        .getStackTrace()[0]
        .getMethodName() + " класса " + this.getClass().getName());

    Collection<PetRecord> petRecords = petService.findAllPet();

    FilterAdoptedPets filterAdoptedPets = new FilterAdoptedPets();
    petRecords = filterAdoptedPets.byChatId(chatId, petRecords);

    ParserToBot parserToBot = new ParserToBot();
    String allAdoptedPets = parserToBot.parserPet(petRecords);

    return allAdoptedPets;
  }


  /**
   * Изменениние состояния пользователя
   *
   * @param chatId chat id пользователя
   * @param state  Список состояний пользвателя, га который нужно заменить уже сужествующий
   * @return новый статус
   */
  public StateAdoptiveParent updateStateAdoptiveParentByChatId(long chatId,
      StateAdoptiveParent state) {
    log.info("Вызов метода " + new Throwable()
        .getStackTrace()[0]
        .getMethodName() + " класса " + this.getClass().getName());

    AdoptiveParentRecord oldAPR = adoptiveParentService.getAdoptiveParentByChatId(
        chatId);
    long id = oldAPR.getId();
    oldAPR.setState(state.getText());

    AdoptiveParentRecord newAPR = adoptiveParentService.updateAdoptiveParent(id, oldAPR);
    return StateAdoptiveParent.valueOf(newAPR.getState());
  }

  /**
   * Получить состояние пользователя, по его чат ид
   *
   * @param chatId чат ид пользователя
   * @return состояние пользователя из бд
   */
  public StateAdoptiveParent getStateAdoptiveParentByChatId(long chatId) {
    return adoptiveParentService.getStateAdoptiveParentByChatId(chatId);
  }

  /**
   * Получить состояние отчета по id животного
   *
   * @param petId id животного
   * @return Состояние отчета из бд или null
   */
  public String getStateReportByPetId(long petId) {

    ReportRecord reportRecord = getReportByPetId(petId);
    StateReport stateReport;
    if (reportRecord != null) {
      return reportRecord.getStateReport();
    }
    return null;
  }

  public PetRecord getPetById(long petId) {
    PetRecord petRecord;
    try {
      petRecord = petService.findPet(petId);
    } catch (Exception e) {
      petRecord = null;
    }
    return petRecord;
  }

}
