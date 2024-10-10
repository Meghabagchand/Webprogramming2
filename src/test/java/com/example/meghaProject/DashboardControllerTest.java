package com.example.meghaProject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.meghaProject.controller.DashboardController;
import com.example.meghaProject.model.Comment;
import com.example.meghaProject.model.User;
import com.example.meghaProject.model.User.Role;
import com.example.meghaProject.service.CommentService;
import com.example.meghaProject.service.DashboardService;
import com.example.meghaProject.service.UserService;

import org.springframework.ui.Model;

public class DashboardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private DashboardService dashboardService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private DashboardController dashboardController;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dashboardController).build();
    }

    @Test
    void testGetUsers() throws Exception {
        when(userService.getUser()).thenReturn("testUser");
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/dashboard/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(model().attribute("username", "testUser"))
                .andExpect(model().attribute("users", new ArrayList<>()));
    }

    @Test
    void testCreateUserPage() throws Exception {
        when(userService.getUser()).thenReturn("testUser");

        mockMvc.perform(get("/dashboard/users/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/createUser"))
                .andExpect(model().attribute("username", "testUser"));
    }

    @Test
    void testCreateUserPost_HappyPath() throws Exception {
        User user = new User();
        user.setUsername("newUser");
        user.setPassword("password123");

        mockMvc.perform(post("/dashboard/users/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", user.getUsername())
                .param("password", user.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/users"));

        verify(dashboardService).createUser(user.getUsername(), user.getPassword());
    }

    @Test
    void testCreateUserPost_EmptyUsername() throws Exception {
        mockMvc.perform(post("/dashboard/users/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "")
                .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("userCreationForm"))
                .andExpect(model().attribute("error", "Username cannot be empty."));
    }

    @Test
    void testCreateUserPost_EmptyPassword() throws Exception {
        mockMvc.perform(post("/dashboard/users/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "newUser")
                .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("userCreationForm"))
                .andExpect(model().attribute("error", "Password cannot be empty."));
    }

    @Test
    void testEditUser() throws Exception {
        User user = new User();
        user.setUsername("editUser");

        when(userService.getUser()).thenReturn("testUser");
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/dashboard/users/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/user/editUser"))
                .andExpect(model().attribute("username", "testUser"))
                .andExpect(model().attribute("user", user));
    }

    @Test
    void testEditUser_Post() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("oldUser");
        user.setRole(Role.ROLE_USER);

        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(post("/dashboard/users/edit/1")
                .param("name", "newUser")
                .param("role", Role.ROLE_ADMIN.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/users"));

        verify(userService).saveUser(user);
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(get("/dashboard/users/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/users"));

        verify(userService).deleteUser(1L);
    }

    @Test
    public void testShowComments_HappyPath() {
        // Arrange
        Comment comment1 = new Comment();
        comment1.setCommentText("Test comment 1");
        Comment comment2 = new Comment();
        comment2.setCommentText("Test comment 2");
        List<Comment> comments = Arrays.asList(comment1, comment2);

        when(userService.getUser()).thenReturn("testUser");
        when(commentService.getAllComments()).thenReturn(comments);

        String viewName = dashboardController.showComments(model);

        assertEquals("comments/list", viewName);
        verify(model).addAttribute("username", "testUser");
        verify(model).addAttribute("comments", comments);
    }

    @Test
    public void testShowComments_NoComments() {

        List<Comment> comments = Arrays.asList();

        when(userService.getUser()).thenReturn("testUser");
        when(commentService.getAllComments()).thenReturn(comments);

        String viewName = dashboardController.showComments(model);

        assertEquals("comments/list", viewName);
        verify(model).addAttribute("username", "testUser");
        verify(model).addAttribute("comments", comments);
    }
}