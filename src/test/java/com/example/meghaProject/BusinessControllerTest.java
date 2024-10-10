package com.example.meghaProject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.meghaProject.controller.BusinessController;
import com.example.meghaProject.model.Business;
import com.example.meghaProject.repo.BusinessRepository;
import com.example.meghaProject.service.BusinessService;
import com.example.meghaProject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

public class BusinessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private BusinessService businessService;

    @Mock
    private BusinessRepository businessRepository;

    @InjectMocks
    private BusinessController businessController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(businessController).build();
    }

    @Test
    public void testGetBusiness() throws Exception {
        Business business1 = new Business();
        business1.setName("Business1");
        Business business2 = new Business();
        business2.setName("Business2");
        List<Business> businesses = Arrays.asList(business1, business2);
        when(userService.getUser()).thenReturn("testUser");
        when(businessService.getAllBusiness()).thenReturn(businesses);

        mockMvc.perform(get("/dashboard/business"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/business"))
                .andExpect(model().attribute("username", "testUser"))
                .andExpect(model().attribute("business", businesses));
    }

    @Test
    public void testCreateBusinessShow() throws Exception {
        when(userService.getUser()).thenReturn("testUser");

        mockMvc.perform(get("/dashboard/business/create"))
                .andExpect(view().name("admin/business/createBusiness"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attribute("username", "testUser"))
                .andExpect(model().attributeExists("business"));
    }

    @Test
    public void testCreateBusiness() throws Exception {
        Business business = new Business();
        business.setName("New Business");

        when(userService.getUser()).thenReturn("testUser");
        when(businessRepository.save(argThat(b -> b.getName().equals("New Business")))).thenReturn(business);
        mockMvc.perform(post("/dashboard/business/create")
                .param("name", "New Business")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/business"));

        verify(businessRepository, times(1)).save(argThat(b -> b.getName().equals("New Business")));
    }

    @Test
    public void testEditBusinessShow() throws Exception {
        Business business = new Business();
        business.setName("Editable Business");

        when(userService.getUser()).thenReturn("testUser");
        when(businessService.getBusinessById(1L)).thenReturn(business);

        mockMvc.perform(get("/dashboard/business/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/business/editBusiness"))
                .andExpect(model().attribute("username", "testUser"))
                .andExpect(model().attribute("business", business));
    }

    @Test
    public void testEditBusiness() throws Exception {
        Business business = new Business();
        business.setName("Old Business");

        when(businessService.getBusinessById(1L)).thenReturn(business);
        when(businessService.saveBusiness(any(Business.class))).thenAnswer(invocation -> {
            Business updatedBusiness = invocation.getArgument(0, Business.class);
            business.setName(updatedBusiness.getName());
            return business;
        });
        when(userService.getUser()).thenReturn("testUser");

        mockMvc.perform(post("/dashboard/business/edit/1")
                .param("name", "Updated Business")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/business"));

        verify(businessService, times(1)).saveBusiness(any(Business.class));
        assertEquals("Updated Business", business.getName());
    }

    @Test
    public void testDeleteBusiness() throws Exception {
        mockMvc.perform(get("/dashboard/business/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/business"));

        verify(businessService, times(1)).deleteBusiness(1L);
    }

    @Test
    public void testEditBusinessNotFound() throws Exception {
        when(businessService.getBusinessById(999L)).thenReturn(null);

        mockMvc.perform(post("/dashboard/business/edit/999")
                .param("name", "Non-existent Business")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/error"));
    }
}