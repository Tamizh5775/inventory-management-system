package com.ims.backend.controller;

import com.ims.backend.model.Role;
import com.ims.backend.model.User;
import com.ims.backend.repository.RoleRepository;
import com.ims.backend.repository.UserRepository;
import com.ims.backend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authManager,
                          UserRepository userRepo,
                          RoleRepository roleRepo,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.get("email"), req.get("password"))
            );
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        var userOpt = userRepo.findByEmail(req.get("email"));
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "User not found"));

        var user = userOpt.get();
        String token = jwtUtil.generateToken(user.getEmail());

        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        return ResponseEntity.ok(Map.of("token", token, "roles", roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepo.existsByEmail(user.getEmail()))
            return ResponseEntity.badRequest().body(Map.of("error", "Email already in use"));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = user.getRoles() == null || user.getRoles().isEmpty()
                ? Set.of(roleRepo.findByName("ROLE_USER").orElseThrow())
                : user.getRoles().stream()
                .map(r -> roleRepo.findByName(r.getName())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + r.getName())))
                .collect(Collectors.toSet());

        user.setRoles(roles);

        var savedUser = userRepo.save(user);
        savedUser.setPassword(null);
        return ResponseEntity.ok(savedUser);
    }
}
