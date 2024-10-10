package com.example.meghaProject;

import com.example.meghaProject.controller.DashboardController;
import com.example.meghaProject.model.User;
import com.example.meghaProject.model.User.Role;
import com.example.meghaProject.service.DashboardService;
import com.example.meghaProject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

// @WebMvcTest(DashboardController.class)

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        User mockUser = new User();
        mockUser.setUsername("testuser");

        Mockito.when(userService.getUser()).thenReturn(mockUser.getUsername());
        Mockito.when(userService.getAllUsers()).thenReturn(Collections.singletonList(mockUser));
        Mockito.when(userService.getUserById(1L)).thenReturn(mockUser);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testGetUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testCreateUserGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/users/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/createUser"))
                .andExpect(model().attributeExists("username"));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testCreateUserPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/users/create")
                .param("username", "newuser")
                .param("password", "password").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/dashboard/users"));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testEditUserGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/users/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/editUser"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testEditUserPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/dashboard/users/edit/1")
                .param("name", "updateduser")
                .param("role", Role.ROLE_USER.name()).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/dashboard/users"));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    void testDeleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard/users/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/dashboard/users"));
    }
}
