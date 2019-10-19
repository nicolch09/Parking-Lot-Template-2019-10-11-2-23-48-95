package com.thoughtworks.parking_lot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.parking_lot.core.ParkingLot;
import com.thoughtworks.parking_lot.service.ParkingLotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ParkingLotController.class)
@ActiveProfiles(profiles = "test")
class ParkingLotControllerTest {
    @MockBean
    ParkingLotService parkingLotService;

    @Autowired
    private MockMvc mvc;

    public ParkingLot createSampleParkingLot() {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName("Tops");
        parkingLot.setCapacity(1);
        parkingLot.setLocation("Manila");
        return parkingLot;
    }

    @Test
    void should_return_200_with_get_all() throws Exception {
        when(parkingLotService.getAll(null, null)).thenReturn(Collections.singletonList(createSampleParkingLot()));

        ResultActions result = mvc.perform(get("/parkingLot/all"));

        result.andExpect(status().isOk());
    }

    @Test
    void should_return_200_with_get_all_with_page() throws Exception {
        when(parkingLotService.getAll(0, 1)).thenReturn(Collections.singletonList(createSampleParkingLot()));

        ResultActions result = mvc.perform(get("/parkingLot/all"));

        result.andExpect(status().isOk());
    }

    @Test
    void should_return_specific_data_when_name_is_sample() throws Exception {
        when(parkingLotService.get("Sample")).thenReturn(createSampleParkingLot());

        ResultActions result = mvc.perform(get("/parkingLot/Sample"));

        result.andExpect(status().isOk());
    }

    @Test
    void should_return_specific_data_when_name_like_elaine() throws Exception {
        when(parkingLotService.getSpecific("Elaine")).thenReturn(Arrays.asList(createSampleParkingLot()));

        ResultActions result = mvc.perform(get("/parkingLot?name=Elaine"));

        result.andExpect(status().isOk());
    }

    @Test
    void should_return_200_when_called_delete_with_correct_name() throws Exception {
        when(parkingLotService.delete("Toper")).thenReturn(createSampleParkingLot());

        ResultActions result = mvc.perform(delete("/parkingLot/Toper"));

        result.andExpect(status().isOk());
    }

    @Test
    void should_modify_parkingLot_when_name_is_correct() throws Exception {
        when(parkingLotService.modify(any(), anyString())).thenReturn(createSampleParkingLot());

        ResultActions result = mvc.perform(get("/parkingLot/" + any()));

        result.andExpect(status().isOk());
    }

    @Test
    void should_return_201_when_parkingLot_is_created() throws Exception {
        when(parkingLotService.add(createSampleParkingLot())).thenReturn(createSampleParkingLot());

        ResultActions result = mvc.perform(post("/parkingLot")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createSampleParkingLot())));

        result.andExpect(status().isCreated());
    }
}