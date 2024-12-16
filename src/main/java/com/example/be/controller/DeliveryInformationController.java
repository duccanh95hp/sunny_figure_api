package com.example.be.controller;

import com.example.be.common.Result;
import com.example.be.entity.DeliveryInformationEntity;
import com.example.be.service.DeliveryInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.be.common.Constants.SUCCESS;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery_information")
public class DeliveryInformationController {
    private final DeliveryInformationService deliveryInformationService;
    @GetMapping
    public Result<?> getAllByUser(){
        List<DeliveryInformationEntity> result = deliveryInformationService.getAllByUser();
        return Result.result(HttpStatus.OK.value(), SUCCESS, result);
    }
}
