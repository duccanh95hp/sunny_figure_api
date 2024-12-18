package com.example.be.service.impl;

import com.example.be.common.Page;
import com.example.be.dto.OrderDetailDto;
import com.example.be.dto.OrderDetailStatusDto;
import com.example.be.dto.OrderDto;
import com.example.be.entity.*;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
                throw new IllegalArgumentException("Quantity exceeds available stock for product ID: " + orderDetail.getProductId());
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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
            orderFilter.setToDate(LocalDateTime.parse(filter.getToDate(), formatter));
        }
        if(filter.getFromDate() != null) {
            orderFilter.setFromDate(LocalDateTime.parse(filter.getFromDate(), formatter));
        }
        orderFilter.setStatus(filter.getStatus());
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
            order.get().setStatus(status.toString());
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
