package com.example.test.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.whiskerspawstailtelegrambot.entity.AdoptiveParent;
import pro.sky.whiskerspawstailtelegrambot.entity.Pet;
import pro.sky.whiskerspawstailtelegrambot.mapper.AdoptiveParentMapper;
import pro.sky.whiskerspawstailtelegrambot.mapper.AdoptiveParentMapperImpl;
import pro.sky.whiskerspawstailtelegrambot.mapper.ReportMapper;
import pro.sky.whiskerspawstailtelegrambot.mapper.ReportMapperImpl;
import pro.sky.whiskerspawstailtelegrambot.record.AdoptiveParentRecord;
import pro.sky.whiskerspawstailtelegrambot.record.PetRecord;
import pro.sky.whiskerspawstailtelegrambot.repository.AdoptiveParentRepo;
import pro.sky.whiskerspawstailtelegrambot.service.AdoptiveParentService;

import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тесты контроллера усыновителя
 */

@WebMvcTest(AdoptiveParentController.class)
class AdoptiveParentControllerTest {

  AdoptiveParentRecord adoptiveParentPositive;
  AdoptiveParentRecord adoptiveParentWithEmptyName;
  Long anyLong = 1L;
  List<PetRecord> listdogs;
  List<Pet> listOfdog;
  AdoptiveParent adoptiveParent;
  JSONObject parentObject;
  JSONObject parentObjectNegative;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private AdoptiveParentRepo repository;
  @SpyBean
  private AdoptiveParentService adoptiveParentService;
  @Autowired
  private ReportMapper reportMapper;
  @Autowired
  private AdoptiveParentMapper adoptiveParentMapper;
  @InjectMocks
  private AdoptiveParentController adoptiveParentController;

  @BeforeEach
  void init() {
    adoptiveParentPositive = new AdoptiveParentRecord();
    listdogs = new ArrayList<>();
    adoptiveParentWithEmptyName = new AdoptiveParentRecord(anyLong, "", "89139131233", false,
        "Empty", 12312L, listdogs);
    adoptiveParent = new AdoptiveParent(1l, "fullName", "phone", true, "start", 0l, listOfdog);
    parentObject = new JSONObject();
    parentObject.put("id", 1L);
    parentObject.put("fullName", "fullName");
    parentObject.put("phone", "89131231213");
    parentObjectNegative = new JSONObject();
    parentObjectNegative.put("id", 1L);
    parentObjectNegative.put("fullName", "");
    parentObjectNegative.put("phone", "");
  }

  @Test
  void getAdoptiveParentByIdPositive() throws Exception {

    when(repository.findById(1L)).thenReturn(Optional.of(adoptiveParent));

    mockMvc.perform(MockMvcRequestBuilders.get(
                "/getAdoptiveParentByID?parentId=" + adoptiveParent.getId()) //send
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()) //receive
        .andExpect(jsonPath("$.fullName").value("fullName"))
        .andExpect(jsonPath("$.phone").value("phone"));
  }

  @Test
  void getAdoptiveParentByIdNegative() throws Exception {

    when(repository.findById(1L)).thenReturn(Optional.empty());

    mockMvc.perform(MockMvcRequestBuilders.get(
                "/getAdoptiveParentByID?parentId=" + adoptiveParent.getId()) //send
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()); //receive

  }


  @Test
  void deleteAdoptiveParentByIDPositive() throws Exception {

    when(repository.findById(any(Long.class))).thenReturn(Optional.of(adoptiveParent));

    mockMvc.perform(MockMvcRequestBuilders.delete(
                "/deleteAdoptiveParentByID?parentId=" + adoptiveParent.getId()) //send
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.fullName").value(adoptiveParent.getFullName()))
        .andExpect(jsonPath("$.phone").value("phone"));


  }

  @Test
  void deleteAdoptiveParentByIDNegative() throws Exception {

    when(repository.findById(any(Long.class))).thenReturn(Optional.of(adoptiveParent));

    mockMvc.perform(MockMvcRequestBuilders.delete(
                "/deleteAdoptiveParentByID?parentId=" + adoptiveParent.getId()) //send
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());


  }

  @Test
  void addAdoptiveParentPositive() throws Exception {
    when(repository.save(any(AdoptiveParent.class))).thenReturn(adoptiveParent);

    mockMvc.perform(MockMvcRequestBuilders.post("/addAdoptiveParent") //send
            .content(parentObject.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()) //receive
        .andExpect(jsonPath("$.fullName").value(adoptiveParent.getFullName()))
        .andExpect(jsonPath("$.phone").value(adoptiveParent.getPhone()));
  }

  @Test
  void addAdoptiveParentNegative() throws Exception {

    when(repository.save(any(AdoptiveParent.class))).thenReturn(adoptiveParent);

    mockMvc.perform(MockMvcRequestBuilders.post("/addAdoptiveParent") //send
            .content(parentObjectNegative.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getListOfAdoptiveParent() throws Exception {
    List<AdoptiveParent> adoptiveParentRecordCollection
        = new ArrayList<>();
    AdoptiveParent adoptiveParent1 = new AdoptiveParent();
    adoptiveParentRecordCollection.add(adoptiveParent);
    adoptiveParentRecordCollection.add(adoptiveParent1);
    when(repository.findAll()).thenReturn(adoptiveParentRecordCollection);

    mockMvc.perform(MockMvcRequestBuilders.get("/getListOfAdoptiveParent") //send
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()) //receive
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  void updateAdoptiveParentPositive() throws Exception {
    when(repository.findById(any(Long.class))).thenReturn(Optional.of(adoptiveParent));
    when(repository.save(any(AdoptiveParent.class))).thenReturn(adoptiveParent);

    mockMvc.perform(MockMvcRequestBuilders.put(
                "/updateAdoptiveParent?parentId=" + adoptiveParent.getId()) //send
            .content(parentObject.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()) //receive
        .andExpect(jsonPath("$.fullName").value(adoptiveParent.getFullName()))
        .andExpect(jsonPath("$.phone").value(adoptiveParent.getPhone()));
  }

  @Test
  void updateAdoptiveParentNegative() throws Exception {
    when(repository.findById(any(Long.class))).thenReturn(Optional.of(adoptiveParent));
    when(repository.save(any(AdoptiveParent.class))).thenReturn(adoptiveParent);

    mockMvc.perform(MockMvcRequestBuilders.put(
                "/updateAdoptiveParent?parentId=" + adoptiveParent.getId()) //send
            .content(parentObjectNegative.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getParentIdByNameAndPhoneAndChatIdPositive() throws Exception {

    Long longInit = 1L;

    when(repository.getAdoptiveParentByFullName(adoptiveParent.getFullName()))
        .thenReturn(adoptiveParent);
    when(repository.getAdoptiveParentByChatId(adoptiveParent.getChatId()))
        .thenReturn(adoptiveParent);
    when(repository.getAdoptiveParentByPhone(adoptiveParent.getPhone()))
        .thenReturn(adoptiveParent);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/getParentIdByNameAndPhoneAndChatId?fullName=" + adoptiveParent.getFullName()
                + "&phone=" + adoptiveParent.getPhone() + "&chatId=" + adoptiveParent.getChatId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()) //receive
        .andExpect(jsonPath("$").value(adoptiveParent.getId()));

  }

  @Test
  void getParentIdByNameAndPhoneAndChatIdNegative() throws Exception {
    when(repository.getAdoptiveParentByFullName(adoptiveParent.getFullName()))
        .thenReturn(adoptiveParent);
    when(repository.getAdoptiveParentByChatId(adoptiveParent.getChatId()))
        .thenReturn(adoptiveParent);
    when(repository.getAdoptiveParentByPhone(adoptiveParent.getPhone()))
        .thenReturn(adoptiveParent);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/getParentIdByNameAndPhoneAndChatId?fullName=" + null
                + "&phone=" + adoptiveParent.getPhone() + "&chatId=" + adoptiveParent.getChatId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest()); //receive

  }

  @TestConfiguration
  static class TestConfig {

    @Bean
    public AdoptiveParentMapper adoptiveParentMapper() {
      return new AdoptiveParentMapperImpl();
    }

    @Bean
    public ReportMapper reportMapper() {
      return new ReportMapperImpl();
    }

  }
}