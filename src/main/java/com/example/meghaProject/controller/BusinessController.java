package com.example.meghaProject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.meghaProject.model.Business;
import com.example.meghaProject.repo.BusinessRepository;
import com.example.meghaProject.service.BusinessService;
import com.example.meghaProject.service.UserService;

@Controller
@RequestMapping("/dashboard")
public class BusinessController {

    @Autowired
    private UserService userService;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private BusinessService businessService;

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @GetMapping("/business")
    public String getBusiness(Model model) {
        model.addAttribute("username", userService.getUser());
        model.addAttribute("business", businessService.getAllBusiness());
        return "admin/business";
    }

    @GetMapping("/business/create")
    public String createBusinessShow(Model model) {
        logger.info("User creation page");
        model.addAttribute("username", userService.getUser());
        model.addAttribute("business", new Business());
        return "admin/business/createBusiness";
    }

    @PostMapping("/business/create")
    public String createBusiness(Model model, @ModelAttribute("business") Business business) {
        String businessName = business.getName();

        logger.info("Business Creation " + businessName);
        Business newBusiness = new Business();
        newBusiness.setName(businessName);

        businessRepository.save(business);

        model.addAttribute("username", userService.getUser());
        return "redirect:/dashboard/business";
    }

    @GetMapping("/business/edit/{id}")
    public String editBusinessShow(Model model, @PathVariable Long id) {
        logger.info("User creation page");
        model.addAttribute("username", userService.getUser());
        model.addAttribute("business", businessService.getBusinessById(id));
        return "admin/business/editBusiness";
    }

    @PostMapping("/business/edit/{id}")
    public String editBusiness(@PathVariable Long id, @RequestParam String name, Model model) {
        Business business = (Business) businessService.getBusinessById(id);
        if (business == null) {
            return "redirect:/error";
        }
        business.setName(name);
        businessService.saveBusiness(business);
        model.addAttribute("business", businessService.getAllBusiness());

        return "redirect:/dashboard/business";
    }

    @GetMapping("/business/delete/{id}")
    public String deleteBusiness(@PathVariable Long id) {
        businessService.deleteBusiness(id);
        return "redirect:/dashboard/business";
    }
}
