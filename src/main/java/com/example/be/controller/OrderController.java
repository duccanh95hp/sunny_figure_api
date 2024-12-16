package com.example.be.controller;

import com.example.be.common.Page;
import com.example.be.common.Result;
import com.example.be.dto.OrderDetailStatusDto;
import com.example.be.entity.OrderEntity;
import com.example.be.model.OrderPayloadFilter;
import com.example.be.payload.OrderPayload;
import com.example.be.payload.OrderStatusPayload;
import com.example.be.service.OrderService;
import com.example.be.statics.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.be.common.Constants.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    public Result<?> create(@RequestBody OrderPayload payload){
        OrderEntity order = orderService.createOrder(payload);
        if(order ==  null) return Result.result(HttpStatus.BAD_REQUEST.value(), FAILURE, null);
        return Result.result(HttpStatus.OK.value(), SUCCESS, order);
    }
    @GetMapping("/user")
    public Result<?> getByUser(){
        List<OrderEntity> results = orderService.findByUser();
        return Result.result(HttpStatus.OK.value(), SUCCESS, results);
    }
    @PostMapping("/all")
    public Result<?> getAll(@RequestBody OrderPayloadFilter filter) {
        Page<Object> orders = orderService.getAllAndSearch(filter);
        if (orders == null) {
            return Result.result(HttpStatus.NO_CONTENT.value(), EMPTY, null);
        } else {
            return Result.result(HttpStatus.OK.value(), SUCCESS, orders);
        }
    }
    @GetMapping("/{id}")
    public Result<?> findById(@PathVariable("id") Long id) {
        OrderDetailStatusDto orderDto = orderService.detailOrder(id);
        if (orderDto == null) {
            return Result.result(HttpStatus.NO_CONTENT.value(), NOT_FOUND,null);
        }
        return Result.result(HttpStatus.OK.value(),SUCCESS,orderDto);
    }
    @PostMapping("/status/{id}")
    public Result<?> updateStatus(@PathVariable("id") Long id,@RequestBody OrderStatusPayload payload){
        boolean result = orderService.updateStatus(id, payload.getStatus());
        if(result){
            return Result.result(HttpStatus.OK.value(), SUCCESS, result);
        }
        return Result.result(HttpStatus.BAD_REQUEST.value(), EMPTY, null);
    }
}
