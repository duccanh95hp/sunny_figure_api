package com.example.be.service.impl;

import com.example.be.common.Page;
import com.example.be.dto.OrderDetailDto;
import com.example.be.dto.OrderDetailStatusDto;
import com.example.be.dto.OrderDto;
import com.example.be.entity.*;
import com.example.be.exception.BusinessException;
import com.example.be.model.OrderFilter;
import com.example.be.model.OrderPayloadFilter;
import com.example.be.payload.OrderPayload;
import com.example.be.payload.StatusPayload;
import com.example.be.repository.*;
import com.example.be.service.OrderService;
import com.example.be.statics.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.example.be.common.Constants.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final DeliveryInformationRepo deliveryRepo;

    private final OrderRepo orderRepo;

    private final OrderDetailRepo detailRepo;

    private final ProductRepo productRepo;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public OrderEntity createOrder(OrderPayload payload) {
        // tao delivery information
        Optional<User> user = getUser();
        if(user.isEmpty()){
            return null;
        }
        if(!payload.getAffiliateCode().isEmpty()){
            if(!userRepository.existsByAffiliateCode(payload.getAffiliateCode())){
                throw new BusinessException(
                        AFFILIATE_CODE_NOT_FOUND,
                        HttpStatus.BAD_REQUEST.value()
                );
            }
            if(user.get().getAffiliateCode().equals(payload.getAffiliateCode())){
                throw new BusinessException(
                        NO_SELF_INTRODUCTION,
                        HttpStatus.BAD_REQUEST.value()
                );
            }
        }

        Long deliveryInformationId = null;
        if(payload.getDeliveryInformation().getId() == null){
            DeliveryInformationEntity delivery = new DeliveryInformationEntity();

            delivery.setName(payload.getDeliveryInformation().getName());
            delivery.setTelephone(payload.getDeliveryInformation().getTelephone());
            delivery.setAddress(payload.getDeliveryInformation().getAddress());
            delivery.setLevelPrior(1l);
            delivery.setUserId(user.get().getId());
            delivery = deliveryRepo.save(delivery);
            deliveryInformationId = delivery.getId();
        } else {
            deliveryInformationId = payload.getDeliveryInformation().getId();
        }
        // tao order
        OrderEntity order = new OrderEntity();
        order.setStatus("NEW");
        order.setUserId(user.get().getId());
        order.setPaymentMethod(payload.getPaymentMethod());
        order.setDeliveryInformationId(deliveryInformationId);
        order.setAffiliateCode(payload.getAffiliateCode());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Double totalPrice =0d;
        for (OrderDetailEntity orderDetail : payload.getOrderDetailEntities()) {
            totalPrice += (orderDetail.getPrice() * orderDetail.getQuantity());
        }
        order.setTotalPrice(totalPrice);
        order.setOrderCode(UUID.randomUUID().toString().replace("-", "").substring(0, 5));
        order = orderRepo.save(order);
        // tao detail order

        for (OrderDetailEntity orderDetail : payload.getOrderDetailEntities()){
            totalPrice += (orderDetail.getPrice() * orderDetail.getQuantity());
            Product product = productRepo.findById(orderDetail.getProductId()).orElseThrow();
            if(orderDetail.getQuantity() > product.getStockQuantity()){
                throw new BusinessException(
                        PURCHASE_EXCEEDS_AVAILABLE,
                        HttpStatus.BAD_REQUEST.value()
                );
            }
            product.setStockQuantity(product.getStockQuantity() - orderDetail.getQuantity());
            productRepo.save(product);
            OrderDetailEntity orderDetail1 = OrderDetailEntity.builder()
                    .orderId(order.getId())
                    .price(orderDetail.getPrice())
                    .quantity(orderDetail.getQuantity())
                    .productId(orderDetail.getProductId())
                    .build();
            detailRepo.save(orderDetail1);
        }
        return order;
    }

    @Override
    public boolean updateStatus(StatusPayload payload) {
        OrderEntity order = findById(payload.getOrderId());
        if(order != null){
            order.setStatus(payload.getStatus());
            orderRepo.save(order);
            return true;
        }
        return false;
    }

    @Override
    public List<OrderEntity> findByUser() {
        Optional<User> user = getUser();
        if(user.isPresent()){
            List<OrderEntity> orderEntities = orderRepo.findByUserId(user.get().getId());
            return orderEntities;
        }
        return null;
    }

    @Override
    public Page<Object> getAllAndSearch(OrderPayloadFilter filter) {

        Optional<User> users = getUser();
        if (!users.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        OrderFilter orderFilter = new OrderFilter();
        orderFilter.setSize(filter.getSize());
        orderFilter.setPage(filter.getPage());
        orderFilter.setUserId(users.get().getId());
        for (Role role : users.get().getRoles()){
            if(role.getName().equals(ERole.ROLE_ADMIN)){
                orderFilter.setUserId(0);
                break;
            }
        }
        if(filter.getToDate() != null) {
            try{
                LocalDate toDate = LocalDate.parse(filter.getToDate(), formatter);
                orderFilter.setToDate(toDate.atTime(23, 59, 59));
            } catch (DateTimeParseException e){
                throw new BusinessException(
                        NOT_FORMAT_DATE,
                        HttpStatus.BAD_REQUEST.value()
                );
            }
        }
        if(filter.getFromDate() != null) {
            try{
                LocalDate fromDate = LocalDate.parse(filter.getFromDate(), formatter);
                orderFilter.setFromDate(fromDate.atTime(00,00,00));
            } catch (DateTimeParseException e){
                throw new BusinessException(
                        NOT_FORMAT_DATE,
                        HttpStatus.BAD_REQUEST.value()
                );
            }

        }
        orderFilter.setStatus(filter.getStatus());
        orderFilter.setOrderCode(filter.getOrderCode());
        orderFilter.setEmail(filter.getEmail());
        orderFilter.setName(filter.getName());
        orderFilter.setPhone(filter.getPhone());
        org.springframework.data.domain.Page<OrderEntity> page = orderRepo.getAllAndSearch(orderFilter, PageRequest.of(filter.getPage() - 1, filter.getSize()));
        if(page.isEmpty()) {
            return null;
        }
        List<Object> objectList = new ArrayList<>();

        for (OrderEntity order : page) {
            OrderDto orderDto = new OrderDto();
            User user = userRepository.findById(order.getUserId()).get();
            orderDto.setId(order.getId());
            orderDto.setName(user.getUsername());
            orderDto.setEmail(user.getEmail());

            Optional<DeliveryInformationEntity> deliveryAddress = deliveryRepo.findById(order.getDeliveryInformationId());
            if(deliveryAddress.isPresent()){
                orderDto.setDeliveryAddress(deliveryAddress.get().getAddress());
                orderDto.setPhone(deliveryAddress.get().getTelephone());
            }
            orderDto.setTotalPrice(order.getTotalPrice());
            orderDto.setStatus(order.getStatus());
            orderDto.setReason(order.getReason());
            orderDto.setDeliveryDate(order.getDeliveryDate());
            orderDto.setCreatedAt(order.getCreatedAt());
            orderDto.setOrderCode(order.getOrderCode());
            orderDto.setAffiliateCode(order.getAffiliateCode());
            orderDto.setPaymentTime(order.getPaymentTime());
            objectList.add(orderDto);
        }
        return com.example.be.common.Page.builder()
                .result(objectList)
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .pageSize(filter.getSize())
                .pageNumber(filter.getPage())
                .build();
    }

    @Override
    public OrderDetailStatusDto detailOrder(long orderId) {
        OrderDetailStatusDto orderDetailStatusDto = new OrderDetailStatusDto();
        OrderEntity order = orderRepo.findById(orderId).get();
        orderDetailStatusDto.setStatus(order.getStatus());
        List<OrderDetailEntity> orderDetails = detailRepo.findByOrderId(orderId);
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        if(orderDetails.isEmpty()) {
            return null;
        }
        for (OrderDetailEntity orderDetail : orderDetails) {
            Product product = productRepo.findById(orderDetail.getProductId()).get();
            OrderDetailDto orderDetailDto = new OrderDetailDto();
            orderDetailDto.setNameProduct(product.getName());
            orderDetailDto.setPrice(orderDetail.getPrice());
            orderDetailDto.setQuantity(orderDetail.getQuantity());
            orderDetailDto.setImage(product.getAvatarUrl());
            orderDetailDtos.add(orderDetailDto);
        }
        orderDetailStatusDto.setDetailDtoList(orderDetailDtos);
        return orderDetailStatusDto;
    }

    @Override
    public boolean updateStatus(Long orderId, Status status) {
        Optional<OrderEntity> order = orderRepo.findById(orderId);
        if(order.isPresent()){
            if("COMPLETED".equals(order.get().getStatus())){
                throw new BusinessException(
                        COMPLETED_NOT_UPDATE,
                        HttpStatus.BAD_REQUEST.value()
                );
            }
            order.get().setStatus(status.toString());
            if(Status.COMPLETED.equals(status)){
                order.get().setPaymentTime(LocalDateTime.now());
            }
            orderRepo.save(order.get());
            return true;
        }
        return false;
    }

    private OrderEntity findById(Long id){
        return orderRepo.findById(id).orElse(null);
    }
    private Optional<User> getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> users = userRepository.findByUsername(username);
        return users;
    }
}
