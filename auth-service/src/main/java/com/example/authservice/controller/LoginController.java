package com.example.authservice.controller;

import com.example.authservice.email.EmailVerificationService;
import com.example.authservice.email.EmailVerificationTokenRepository;
import com.example.authservice.entity.EmailVerificationToken;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.User;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.config.UserDetailsImpl;
import com.example.authservice.security.payload.request.LoginRequest;
import com.example.authservice.security.payload.request.SignupRequest;
import com.example.authservice.security.payload.response.JwtResponse;
import com.example.authservice.security.payload.response.MessageResponse;
import com.example.authservice.security.utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3001")
@RequestMapping(value = "api/v1")
public class LoginController{

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
    private EmailVerificationService emailVerificationService;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!userDetails.isEmailVerified()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is not verified!"));
        }

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        // Step 1: Hash the password
        String hashedPassword = encoder.encode(signUpRequest.getPassword());

// Step 2: Create the User object with username, email, and hashed password
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), hashedPassword);

// Step 3: Set the telephone number on the User object
        user.setTel(signUpRequest.getTel());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase(Locale.ROOT)) {
                    case "admin":
                        Role adminRole = roleRepository.findByName("ADMIN")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "manager":
                        Role modRole = roleRepository.findByName("MANAGER")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName("USER")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles((Set<Role>) roles);
        user = userRepository.save(user);
        emailVerificationService.sendVerificationEmail(user);


//        emailVerificationService.sendVerificationEmail(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            // Mã xác nhận không hợp lệ
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid verification token"));
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true); // Đánh dấu email là đã xác nhận
        userRepository.save(user);

        // Xóa mã xác nhận khỏi cơ sở dữ liệu
        emailVerificationTokenRepository.delete(verificationToken);

        return ResponseEntity.ok(new MessageResponse("Email verified successfully!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String token = extractAuthToken(request);
        if (token != null) {
            jwtUtils.expireJwtToken(token);
        }
        return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
    }

    private String extractAuthToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Lấy token từ header
        }

        return null;
    }
}
