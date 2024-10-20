package com.example.meghaProject;

import com.example.meghaProject.model.Business;
import com.example.meghaProject.model.Comment;
import com.example.meghaProject.repo.BusinessRepository;
import com.example.meghaProject.repo.CommentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BusinessControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setup() {
        // Optional setup if needed
        businessRepository.deleteAll();
    }

    @WithMockUser(username = "testUser", roles = { "ADMIN" })
    @Test
    void testGetBusiness() throws Exception {
        // Given - setup data
        Business business = new Business();
        business.setName("Test Business");
        businessRepository.save(business);

        MvcResult result = mockMvc.perform(get("/dashboard/business"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/business"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("business"))
                .andReturn();

        List<Business> businesses = (List<Business>) result.getModelAndView().getModel().get("business");
        assertThat(businesses).isNotEmpty();
    }

    @WithMockUser(username = "testUser", roles = { "ADMIN" })
    @Test
    void testCreateBusiness() throws Exception {
        mockMvc.perform(post("/dashboard/business/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "New Test Business"))
                .andExpect(status().is3xxRedirection()) 
                .andExpect(redirectedUrl("/dashboard/business"));

        List<Business> businesses = businessRepository.findAll();
        assertThat(businesses).hasSize(1);
        assertThat(businesses.get(0).getName()).isEqualTo("New Test Business");
    }

    @WithMockUser(username = "testUser", roles = { "ADMIN" })
    @Test
    void testEditBusiness() throws Exception {
        Business business = new Business();
        business.setName("Old Name");
        business = businessRepository.save(business);

        mockMvc.perform(post("/dashboard/business/edit/{id}", business.getId())
                .param("name", "Updated Business"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/business"));

        Business updatedBusiness = businessRepository.findById(business.getId()).orElseThrow();
        assertThat(updatedBusiness.getName()).isEqualTo("Updated Business");
    }

    @WithMockUser(username = "testUser", roles = { "ADMIN" })
    @Test
    void testDeleteBusiness() throws Exception {
        Business business = new Business();
        business.setName("Business to Delete");
        business = businessRepository.save(business);

        Comment comment = new Comment();
        comment.setBusiness(business);
        commentRepository.save(comment);

        commentRepository.delete(comment);

        mockMvc.perform(get("/dashboard/business/delete/{id}", business.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/business"));

        assertThat(businessRepository.findById(business.getId())).isEmpty();
    }

    @WithMockUser(username = "testUser", roles = { "ADMIN" })
    @Test
    void testCreateBusinessShow() throws Exception {
        mockMvc.perform(get("/dashboard/business/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/business/createBusiness"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("business"));
    }

    @WithMockUser(username = "testUser", roles = { "ADMIN" })
    @Test
    void testEditBusinessShow() throws Exception {
        Business business = new Business();
        business.setName("Existing Business");
        business = businessRepository.save(business);

        mockMvc.perform(get("/dashboard/business/edit/{id}", business.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/business/editBusiness"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("business"));
    }
}
