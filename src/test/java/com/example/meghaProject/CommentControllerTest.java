package com.example.meghaProject;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import com.example.meghaProject.controller.CommentController;
import com.example.meghaProject.model.*;
import com.example.meghaProject.repo.UserRepository;
import com.example.meghaProject.service.CommentService;
import com.example.meghaProject.service.BusinessService;
import com.example.meghaProject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;
    @Mock
    private BusinessService businessService;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
        authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testAddCommentForm() throws Exception {
        Business business = new Business();
        business.setId(1L);

        when(businessService.getBusinessById(1L)).thenReturn(business);

        mockMvc.perform(get("/business/1/comments/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("business"))
                .andExpect(view().name("comments/add"));
    }

    // @Test
    // public void testAddComment() throws Exception {
    // String content = "content=This+is+a+test+comment";

    // // Mock authentication
    // UserDetails userDetails = mock(UserDetails.class);
    // when(userDetails.getUsername()).thenReturn("testUser");
    // when(authentication.getPrincipal()).thenReturn(userDetails);

    // SecurityContextHolder.getContext().setAuthentication(authentication);

    // User user = new User();
    // Business business = new Business();
    // business.setId(1L);

    // when(userService.findByUsername("testUser")).thenReturn(user);
    // when(businessService.getBusinessById(1L)).thenReturn(business);

    // mockMvc.perform(
    // post("/business/1/comments").content(content).contentType(MediaType.APPLICATION_FORM_URLENCODED))
    // .andExpect(status().is3xxRedirection())
    // .andExpect(redirectedUrl("/"));

    // verify(commentService).addComment(user, business, "This is a test comment");
    // }

    // @Test
    // public void testDeleteComment() throws Exception {
    // User user = new User();
    // user.setUsername("testUser");
    // when(authentication.getName()).thenReturn("testUser");
    // SecurityContextHolder.getContext().setAuthentication(authentication);

    // when(userRepository.findByUsername("testUser")).thenReturn(user);
    // Comment comment = new Comment();
    // comment.setUser(user);
    // when(commentService.getCommentById(1L)).thenReturn(Optional.of(comment));

    // mockMvc.perform(get("/business/1/comments/1/delete"))
    // .andExpect(status().is3xxRedirection())
    // .andExpect(redirectedUrl("/"));

    // verify(commentService).deleteComment(comment);
    // }

    // @Test
    // public void testEditComment() throws Exception {
    // User user = new User();
    // when(authentication.getPrincipal()).thenReturn(user);
    // SecurityContextHolder.getContext().setAuthentication(authentication);

    // Comment comment = new Comment();
    // comment.setUser(user);
    // when(commentService.getCommentById(1L)).thenReturn(Optional.of(comment));

    // mockMvc.perform(post("/business/1/comments/1/edit").param("newContent",
    // "Updated content"))
    // .andExpect(status().is3xxRedirection())
    // .andExpect(redirectedUrl("/businesses/1/comments"));

    // verify(commentService).updateComment(comment, "Updated content");
    // }

    // @Test
    // public void testAddCommentInvalidContent() throws Exception {
    // String content = "content=";

    // mockMvc.perform(
    // post("/business/1/comments").content(content).contentType(MediaType.APPLICATION_FORM_URLENCODED))
    // .andExpect(status().is3xxRedirection())
    // .andExpect(redirectedUrl("/"));
    // }

    @Test
    public void testDeleteCommentNotAuthorized() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        when(authentication.getName()).thenReturn("anotherUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Comment comment = new Comment();
        comment.setUser(user);
        when(commentService.getCommentById(1L)).thenReturn(Optional.of(comment));

        mockMvc.perform(get("/business/1/comments/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(commentService, never()).deleteComment(comment);
    }

    // @Test
    // public void testEditCommentNotFound() throws Exception {
    // when(commentService.getCommentById(1L)).thenReturn(Optional.empty());

    // mockMvc.perform(post("/business/1/comments/1/edit").param("newContent",
    // "Updated content"))
    // .andExpect(status().is3xxRedirection())
    // .andExpect(redirectedUrl("/businesses/1/comments"));
    // }
}