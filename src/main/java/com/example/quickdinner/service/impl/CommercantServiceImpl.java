package com.example.quickdinner.service.impl;

import com.example.quickdinner.model.Commercant;
import com.example.quickdinner.model.Role;
import com.example.quickdinner.repository.CommercantRepository;
import com.example.quickdinner.repository.RoleRepository;
import com.example.quickdinner.service.CommercantService;
import com.example.quickdinner.service.RoleService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log
public class CommercantServiceImpl implements CommercantService {

    private final CommercantRepository commercantRepository;

    public CommercantServiceImpl(CommercantRepository commercantRepository) {
        this.commercantRepository = commercantRepository;
    }


    @Override
    public List<Commercant> findAll() {
        return this.commercantRepository.findAll();
    }
}
