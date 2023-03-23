package com.example.test.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pro.sky.whiskerspawstailtelegrambot.entity.Pet;
import pro.sky.whiskerspawstailtelegrambot.mapper.PetMapper;
import pro.sky.whiskerspawstailtelegrambot.mapper.PetMapperImpl;
import pro.sky.whiskerspawstailtelegrambot.record.PetRecord;
import pro.sky.whiskerspawstailtelegrambot.repository.PetRepository;
import pro.sky.whiskerspawstailtelegrambot.service.PetService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
@AutoConfigureMockMvc
class PetControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;
  @InjectMocks
  private PetController petController;

  @MockBean
  private PetService petService;

  @MockBean
  private PetRepository petRepository;

  @Autowired
  private PetMapper petMapper;

  @MockBean
  PetRecord petRecord;



  @Test
  public void dogTest() throws Exception {


    final Long id = 1L;
    final Long dogId = 1L;
    final String fullName = "Dog";
    final String age = "5";
    final String description = "DogDescription";

    MockMultipartFile file = new MockMultipartFile("data", "photo.jpeg",
        MediaType.MULTIPART_FORM_DATA_VALUE, "photo.jpeg".getBytes());

    Pet pet = new Pet();
    pet.setId(id);
    pet.setFullName(fullName);
    pet.setAge(age);
    pet.setDescription(description);
    pet.setPhoto(file.getBytes());
    pet.setFileSize(file.getSize());
    pet.setMediaType("image/jpeg");


    PetRecord record = petMapper.toRecord(pet);
    when(petService.findPet(id)).thenReturn(record);

    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    mockMvc.perform(multipart(HttpMethod.POST, "/pets/add").file(file)//post
        .param("name", "dogName")
        .param("age", "1111")
        .param("des", "DogDescription")
        .accept(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
    mockMvc.perform(multipart(HttpMethod.PUT,"/pets").file(file)
            .param("id", "1")//put изменение
            .param("name", fullName)
            .param("age", age)
            .param("des", description)
            .accept(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
    mockMvc.perform(MockMvcRequestBuilders//put изменение усыновителя
        .put("/pets/parent")
            .param("petId", "1")
            .param("id", "2")
            .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
    mockMvc.perform(MockMvcRequestBuilders//del
            .delete("/pets")
            .param("id", "2")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
    mockMvc.perform(MockMvcRequestBuilders//get
            .get("/pets")
            .param("id", "1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
    mockMvc.perform(MockMvcRequestBuilders//getAll
            .get("/pets/all")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
    mockMvc.perform(multipart(HttpMethod.GET,"/pets/photo").file(file)
            .param("id", "1")//get фото
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .accept(MediaType.MULTIPART_FORM_DATA_VALUE))
        .andDo(print())
        .andExpect(status().isOk());

  }


  @TestConfiguration
  static class TestConfig {
    @Bean
    public PetMapper petMapper() {
      return new PetMapperImpl();
    }
  }
}