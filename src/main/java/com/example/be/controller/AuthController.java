package com.example.be.controller;

import com.example.be.common.Result;
import com.example.be.dto.UserDto;
import com.example.be.entity.ERole;
import com.example.be.entity.OrderEntity;
import com.example.be.entity.Role;
import com.example.be.entity.User;
import com.example.be.model.*;
import com.example.be.repository.OrderRepo;
import com.example.be.repository.RoleRepository;
import com.example.be.repository.UserRepository;
import com.example.be.security.jwt.JwtUtils;
import com.example.be.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    OrderRepo orderRepo;

    @PostMapping("/signin")
    public Result<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return Result.result(HttpStatus.OK.value(),"Login success" ,new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));
        } catch (Exception e) {
            return Result.result(HttpStatus.FORBIDDEN.value(), "Login fail", null);
        }

    }

    @PostMapping("/signup")
    public Result<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return Result.result(HttpStatus.BAD_REQUEST.value(), "Error: User name is already in use!",null);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return Result.result(HttpStatus.BAD_REQUEST.value(), "Error: Email is already in use!",null);
        }
        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
//                    case "admin":
//                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(adminRole);
//
//                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        user.setAffiliateCode(UUID.randomUUID().toString().replace("-", "").substring(0, 8));
        userRepository.save(user);

        return Result.result(HttpStatus.OK.value(), "User registered successfully!",null);
    }
    @GetMapping("/detail")
    public Result<?> detail() {

        Optional<User> user = getUser();
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDto userDto = new UserDto();
        userDto.setId(user.get().getId());
        userDto.setUsername(user.get().getUsername());
        userDto.setAddress(user.get().getAddress());


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if(user.get().getBirthday() != null) {
            userDto.setBirthday(user.get().getBirthday().format(formatter));
        }
        userDto.setEmail(user.get().getEmail());
        userDto.setTelephone(user.get().getTelephone());
        userDto.setFullName(user.get().getFullName());
        userDto.setAffiliateCode(user.get().getAffiliateCode());

        // get all order by affilcate code and status = complete
        List<OrderEntity> orderEntities = orderRepo.findAllByAffiliateCodeAndStatus(user.get().getAffiliateCode(), "COMPLETED");
        Double affiliateAmount = 0d;
        for (OrderEntity order : orderEntities){
            affiliateAmount += (order.getTotalPrice() * 0.05);
        }
        userDto.setAffiliateAmount(affiliateAmount);
        return Result.result(HttpStatus.OK.value(), "", userDto);
    }
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserPayload payload) {

        Optional<User> user = getUser();
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDto userDto = new UserDto();
        try
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate localDate = LocalDate.parse(payload.getBirthday(), formatter);
            user.get().setAddress(payload.getAddress());
            user.get().setBirthday(localDate);
            user.get().setFullName(payload.getFullName());
            user.get().setTelephone(payload.getTelephone());
            userRepository.save(user.get());

            userDto.setId(user.get().getId());
            userDto.setUsername(user.get().getUsername());
            userDto.setAddress(user.get().getAddress());

            userDto.setBirthday(payload.getBirthday());
            userDto.setEmail(user.get().getEmail());
            userDto.setTelephone(user.get().getTelephone());
            userDto.setFullName(user.get().getFullName());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userDto);
    }
    private Optional<User> getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> users = userRepository.findByUsername(username);
        return users;
    }
}
