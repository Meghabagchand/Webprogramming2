package com.example.meghaProject;

import com.example.meghaProject.model.*;
import com.example.meghaProject.repo.UserRepository;
import com.example.meghaProject.service.CommentService;
import com.example.meghaProject.service.BusinessService;
import com.example.meghaProject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class CommentControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BusinessService businessService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Mock SecurityContext and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
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
    // UserDetails userDetails = mock(UserDetails.class);
    // when(userDetails.getUsername()).thenReturn("testUser");
    // when(authentication.getPrincipal()).thenReturn(userDetails);

    // User user = new User();
    // Business business = new Business();
    // business.setId(1L);

    // when(userService.findByUsername("testUser")).thenReturn(user);
    // when(businessService.getBusinessById(1L)).thenReturn(business);

    // mockMvc.perform(post("/business/1/comments")
    // .content("content=This+is+a+test+comment")
    // .contentType(MediaType.APPLICATION_FORM_URLENCODED))
    // .andExpect(status().is3xxRedirection())
    // .andExpect(redirectedUrl("/"));

    // verify(commentService).addComment(user, business, "This is a test comment");
    // }

    // @Test
    // public void testDeleteComment() throws Exception {
    // User user = new User();
    // user.setUsername("testUser");
    // when(authentication.getName()).thenReturn("testUser");

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
    // user.setUsername("testUser");
    // when(authentication.getPrincipal()).thenReturn(user);

    // Comment comment = new Comment();
    // comment.setUser(user);

    // when(commentService.getCommentById(1L)).thenReturn(Optional.of(comment));

    // mockMvc.perform(post("/business/1/comments/1/edit")
    // .param("newContent", "Updated content")
    // .contentType(MediaType.APPLICATION_FORM_URLENCODED))
    // .andExpect(status().is3xxRedirection())
    // .andExpect(redirectedUrl("/businesses/1/comments"));

    // verify(commentService).updateComment(comment, "Updated content");
    // }
}
