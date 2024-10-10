package com.example.meghaProject.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.meghaProject.controller.DashboardController;
import com.example.meghaProject.model.Business;
import com.example.meghaProject.repo.BusinessRepository;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    public Business findById(Long id) {
        return businessRepository.findById(id).orElse(null);
    }

    public void save(Business business) {
        businessRepository.save(business);
    }

    public boolean createBusiness(String name) {
        logger.info("Add Business: ", name);
        Business business = new Business();
        business.setName(name);
        businessRepository.save(business);
        return true;
    }

    public List<Business> getAllBusiness() {
        List<Business> businesses = businessRepository.findAll();
        for (Business business : businesses) {
            Hibernate.initialize(business.getComments());
        }
        return businesses;
    }

    // complete this method
    public Business getBusinessById(Long id) {

        return businessRepository.findById(id).orElseThrow();
    }

    public Business saveBusiness(Business existingBusiness) {
        // complete this method
        return businessRepository.save(existingBusiness);
    }

    public void deleteBusiness(Long id) {
        businessRepository.deleteById(id);
    }
}
