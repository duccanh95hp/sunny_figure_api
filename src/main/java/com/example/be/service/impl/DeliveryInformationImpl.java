package com.example.be.service.impl;

import com.example.be.entity.DeliveryInformationEntity;
import com.example.be.entity.User;
import com.example.be.repository.DeliveryInformationRepo;
import com.example.be.repository.UserRepository;
import com.example.be.service.DeliveryInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryInformationImpl implements DeliveryInformationService {
    private final UserRepository userRepository;
    private final DeliveryInformationRepo deliveryInformationRepo;
    @Override
    public List<DeliveryInformationEntity> getAllByUser() {
        Optional<User> user = getUser();
        if(user.isEmpty()){
            return null;
        }
        return deliveryInformationRepo.findAllByUserId(user.get().getId());
    }
    private Optional<User> getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> users = userRepository.findByUsername(username);
        return users;
    }
}
