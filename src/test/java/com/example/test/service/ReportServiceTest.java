package com.example.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.whiskerspawstailtelegrambot.entity.AdoptiveParent;
import pro.sky.whiskerspawstailtelegrambot.entity.Report;
import pro.sky.whiskerspawstailtelegrambot.exception.ElemNotFound;
import pro.sky.whiskerspawstailtelegrambot.mapper.ReportMapper;
import pro.sky.whiskerspawstailtelegrambot.mapper.ReportMapperImpl;
import pro.sky.whiskerspawstailtelegrambot.record.PetRecord;
import pro.sky.whiskerspawstailtelegrambot.record.ReportRecord;
import pro.sky.whiskerspawstailtelegrambot.repository.PetRepository;
import pro.sky.whiskerspawstailtelegrambot.repository.ReportRepository;
import pro.sky.whiskerspawstailtelegrambot.textAndButtonsAndKeyboard.AllText;
import pro.sky.whiskerspawstailtelegrambot.util.ParserToBot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

  @Mock
  ReportRepository reportRepository;
  @Spy
  ReportMapper reportMapper = new ReportMapperImpl();
  //  @Mock
//  ReportMapper reportMapper;
  @InjectMocks
  ReportService reportService;

  @Mock
  PetService petService;
  @Mock
  PetRepository petRepository;

  ReportRecord reportRecord;
  ReportRecord updateReportRecord;
  Report report;
  Report updateReport;

  @BeforeEach
  void setUp() throws IOException {

    reportRecord = new ReportRecord();
    updateReportRecord = new ReportRecord();
    report = new Report();
    updateReport = new Report();

    byte[] data;
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      BufferedImage bImage = ImageIO.read(new File("./src/test/img/photoForTest.png"));
      ImageIO.write(bImage, "png", bos);
      data = bos.toByteArray();
    }
    reportRecord.setId(1l);
    reportRecord.setPet_id(1l);
//    reportRecord.setPhotoDog(data);
    reportRecord.setDiet("test ");
    reportRecord.setReportAboutFeelings("test ");
    reportRecord.setReportAboutHabits("test ");
    reportRecord.setStateReport("FINISHED");

    updateReportRecord.setId(1l);
    updateReportRecord.setPet_id(1l);
//    updateReportRecord.setPhotoDog(data);
    updateReportRecord.setDiet("update test ");
    updateReportRecord.setReportAboutFeelings("update test ");
    updateReportRecord.setReportAboutHabits("update test ");
    updateReportRecord.setStateReport("NOT_STARTED");

    report.setId(1l);
    report.setPet_id(1l);
//    report.setPhotoDog(data);
    report.setDiet("test ");
    report.setReportAboutFeelings("test ");
    report.setReportAboutHabits("test ");
    report.setStateReport("FINISHED");

    updateReport.setId(1l);
    updateReport.setPet_id(1l);
//    newReport.setPhotoDog(data);
    updateReport.setDiet("update test ");
    updateReport.setReportAboutFeelings("update test ");
    updateReport.setReportAboutHabits("update test ");
    updateReport.setStateReport("NOT_STARTED");
  }


  @Test
  void getReportById() {
    when(reportRepository.findById(anyLong())).thenReturn(Optional.of(report));
    when(reportMapper.toRecord(any(Report.class))).thenReturn(reportRecord);

    assertThat(reportService.getReportById(1l)).isEqualTo(reportRecord);
    verify(reportRepository, times(1)).findById(any());
  }

  @Test
  void getReportByIdNegative() {
    assertThatExceptionOfType(ElemNotFound.class).isThrownBy(() -> reportService.getReportById(2));
    verify(reportRepository, times(1)).findById(any());
  }

  @Test
  void getReportByPetId() {
    when(reportRepository.getReportByPet_id(anyLong())).thenReturn(report);
    when(reportMapper.toRecord(any(Report.class))).thenReturn(reportRecord);

    assertThat(reportService.getReportByPetId(1l)).isEqualTo(reportRecord);
    verify(reportRepository, times(1)).getReportByPet_id(any());
  }

  @Test
  void getReportByPetIdNegative() {
    assertThat(reportService.getReportByPetId(2)).isNull();
    verify(reportRepository, times(1)).getReportByPet_id(any());
  }

  @Test
  void addNewReportInDbForPetByPetId() {
    when(reportRepository.getReportByPet_id(anyLong())).thenReturn(report);
    when(reportMapper.toRecord(any(Report.class))).thenReturn(reportRecord);

    assertThat(reportService.getReportByPetId(1l)).isEqualTo(reportRecord);
    verify(reportRepository, times(1)).getReportByPet_id(any());
  }

  @Test
  void addNewReportInDbForPetByPetIdNegative() {
    assertThat(reportService.getReportByPetId(2)).isNull();
    verify(reportRepository, times(1)).getReportByPet_id(any());
  }

  @Test
  void updateReportByReportId() {
    when(reportRepository.findById(anyLong())).thenReturn(Optional.of(report));
    when(reportService.getReportById(anyLong())).thenReturn(reportRecord);
    when(reportRepository.save(updateReport)).thenReturn(updateReport);

    assertThat(reportService.updateReportByReportId(1, updateReportRecord)).isEqualTo(
        updateReportRecord);
    verify(reportRepository, times(2)).findById(any());
    verify(reportRepository, times(1)).save(any());

  }

  @Test
  void updateReportByReportIdNegative() {
    assertThatExceptionOfType(ElemNotFound.class).isThrownBy(
        () -> reportService.updateReportByReportId(3, updateReportRecord));
    verify(reportRepository, times(1)).findById(any());
    verify(reportRepository, times(1)).save(any());
  }

  @Test
  void showAllAdoptedPets() {

    PetRecord petRecord = initDogReport(initAdoptiveParent(111));

    Collection<PetRecord> petRecords = new ArrayList<>();
    petRecords.add(petRecord);

    ParserToBot parserToBot = new ParserToBot();
    String allAdoptedPets = parserToBot.parserPet(petRecords);
    when(petService.findAllPet()).thenReturn(petRecords);

    assertThat(reportService.showAllAdoptedPets(111)).isEqualTo(
        allAdoptedPets);
    verify(petService, times(1)).findAllPet();
  }

  @Test
  void showAllAdoptedPetsNegative() {
    PetRecord petRecord = initDogReport(initAdoptiveParent(111));

    Collection<PetRecord> petRecords = new ArrayList<>();
    petRecords.add(petRecord);

    ParserToBot parserToBot = new ParserToBot();
    String allAdoptedPets = parserToBot.parserPet(petRecords);
    when(petService.findAllPet()).thenReturn(petRecords);

    assertThat(reportService.showAllAdoptedPets(222)).isEqualTo(
        AllText.YOU_HAVE_NO_ADOPTED_PETS_TEXT);
    verify(petService, times(1)).findAllPet();
  }

  @Test
  void changeStateAdoptiveParent() {
  }

  @Test
  void changeStateAdoptiveParentNegative() {
  }


  @Test
  void getStateAdoptiveParentByChatId() {
  }

  @Test
  void getStateAdoptiveParentByChatIdNegative() {
  }

  @Test
  void getStateReportByPetId() {
  }

  @Test
  void getStateReportByPetIdNegative() {
  }

  AdoptiveParent initAdoptiveParent(long id) {
    AdoptiveParent adoptiveParent = new AdoptiveParent();
    adoptiveParent.setChatId(id);
    return adoptiveParent;
  }

  PetRecord initDogReport(AdoptiveParent adoptiveParent) {

    PetRecord petRecord = new PetRecord();
    petRecord.setId(1l);
    petRecord.setFullName("dog 1");
    petRecord.setAdoptiveParent(adoptiveParent);
    return petRecord;
  }
}