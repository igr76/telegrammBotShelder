package com.example.test.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.whiskerspawstailtelegrambot.entity.Volunteer;
import pro.sky.whiskerspawstailtelegrambot.mapper.VolunteerMapper;
import pro.sky.whiskerspawstailtelegrambot.mapper.VolunteerMapperImpl;
import pro.sky.whiskerspawstailtelegrambot.repository.VolunteerRepo;
import pro.sky.whiskerspawstailtelegrambot.service.VolunteerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VolunteerController.class)
class VolunteerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VolunteerRepo repository;
    @SpyBean
    private VolunteerService volunteerService;
    @Autowired
    private VolunteerMapper volunteerMapper;
    @InjectMocks
    private VolunteerController volunteerController;

    @Test
    void getVolunteerPositiveTest() throws Exception {
        Volunteer volunteer = getTestVolunteer();

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(volunteer));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer/" + volunteer.getId()));
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(volunteer.getId()))
                .andExpect(jsonPath("$.fullName").value(volunteer.getFullName()));
    }

    @Test
    void getVolunteerNegativeTest() throws Exception {
        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/volunteer/1"));
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteVolunteerByIdPositiveTest() throws Exception {
        Volunteer volunteer = getTestVolunteer();

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(volunteer));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete("/volunteer/" + volunteer.getId()));
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(volunteer.getId()))
                .andExpect(jsonPath("$.fullName").value(volunteer.getFullName()));
    }

    @Test
    void deleteVolunteerByIdNegativeTest() throws Exception {
        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .delete("/volunteer/1"));
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    void getListOfVolunteerRecord() throws Exception{
        List<Volunteer> volunteerList = new ArrayList<>();
        volunteerList.add(getTestVolunteer());

        Volunteer volunteer = new Volunteer();
        volunteer.setId(2l);
        volunteer.setFullName("Иванов Иван Иванович");
        volunteer.setPhone("89146667454");
        volunteer.setInfoVolunteer("Дорогой посетитель, наши волонтеры с удовольствием окажут вам помощь, вам будет " +
                "предложен списокнаших волонтеров и их номера. Пожалуста уитывайте их расписание и они" +
                " будут рады вам помочь.");
        volunteer.setSchedule("рабочие дни 14-22ч");

        volunteerList.add(volunteer);
        when(repository.findAll()).thenReturn(volunteerList);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/volunteer/getListOfVolunteer"));
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].fullName").value("Ивченко Валентин Генадьевич"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].fullName").value("Иванов Иван Иванович"));
    }

    @Test
    void addVolunteerPositiveTest() throws Exception {
        Volunteer volunteer = getTestVolunteer();
        String paramString = "?fullName=" + volunteer.getFullName() +
                "&phone=" + volunteer.getPhone() +
                "&info=" + volunteer.getInfoVolunteer() +
                "&schedule=" + volunteer.getSchedule();

        when(repository.save(any(Volunteer.class))).thenReturn(volunteer);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/volunteer/addVolunteer" + paramString));
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(volunteer.getId()))
                .andExpect(jsonPath("$.fullName").value(volunteer.getFullName()));
    }

    @Test
    void addVolunteerNegativeTest() throws Exception {
        Volunteer volunteer = getTestVolunteer();
        String paramString = "?fullName=" + volunteer.getFullName() +
                "&info=" + volunteer.getInfoVolunteer() +
                "&schedule=" + volunteer.getSchedule();

        when(repository.save(any(Volunteer.class))).thenReturn(volunteer);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/volunteer/addVolunteer" + paramString));
        resultActions
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateVolunteerPositiveTest() throws Exception {
        Volunteer volunteer = getTestVolunteer();
        String paramString = "?fullName=" + volunteer.getFullName() +
                "&phone=" + volunteer.getPhone() +
                "&info=" + volunteer.getInfoVolunteer() +
                "&schedule=" + volunteer.getSchedule();

        when(repository.findById(any(Long.class))).thenReturn(Optional.of(volunteer));
        when(repository.save(any(Volunteer.class))).thenReturn(volunteer);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/volunteer/" + volunteer.getId() + paramString));
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(volunteer.getId()))
                .andExpect(jsonPath("$.fullName").value(volunteer.getFullName()));
    }


    @Test
    void updateVolunteerNegativeTest() throws Exception {
        Volunteer volunteer = getTestVolunteer();
        String paramString = "?fullName=" + volunteer.getFullName() +
                "&phone=" + volunteer.getPhone() +
                "&info=" + volunteer.getInfoVolunteer() +
                "&schedule=" + volunteer.getSchedule();

        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/volunteer/" + volunteer.getId() + paramString))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getVolunteerService() {
    }

    private Volunteer getTestVolunteer() {
        Volunteer volunteer = new Volunteer();
        volunteer.setId(1l);
        volunteer.setFullName("Ивченко Валентин Генадьевич");
        volunteer.setPhone("89146667454");
        volunteer.setInfoVolunteer("Дорогой посетитель, наши волонтеры с удовольствием окажут вам помощь, вам будет " +
                "предложен списокнаших волонтеров и их номера. Пожалуста уитывайте их расписание и они" +
                " будут рады вам помочь.");
        volunteer.setSchedule("рабочие дни 14-22ч");

        return volunteer;
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public VolunteerMapper volunteerMapper() {
            return new VolunteerMapperImpl();
        }
    }
}