package financial_dashboard_api.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import financial_dashboard_api.demo.model.User;
import financial_dashboard_api.demo.repository.UserRepository;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    // CREATE USER (ADMIN ONLY)
    @PostMapping("/users")
    public ResponseEntity<?> createUser(
            @RequestParam String userId,
            @RequestBody User user) {

        User currentUser = userRepository.findById(userId).orElseThrow();

        if (!currentUser.getRole().equals("ADMIN")) {
            return ResponseEntity.status(403)
                    .body("Access Denied: Only ADMIN can create users");
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

    // GET USER (ADMIN ONLY) 
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        User user = userRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(user);
    }

    // UPDATE USER (ADMIN ONLY)
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(
            @RequestParam String userId,
            @PathVariable String id,
            @RequestBody User user) {

        User currentUser = userRepository.findById(userId).orElseThrow();

        if (!currentUser.getRole().equals("ADMIN")) {
            return ResponseEntity.status(403)
                    .body("Access Denied: Only ADMIN can update users");
        }

        User existingUser = userRepository.findById(id).orElseThrow();

        existingUser.setName(user.getName());
        existingUser.setRole(user.getRole());
        existingUser.setIsActive(user.getIsActive());

        return ResponseEntity.ok(userRepository.save(existingUser));
    }

    // DELETE USER (ADMIN ONLY)
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(
            @RequestParam String userId,
            @PathVariable String id) {

        User currentUser = userRepository.findById(userId).orElseThrow();

        if (!currentUser.getRole().equals("ADMIN")) {
            return ResponseEntity.status(403)
                    .body("Access Denied: Only ADMIN can delete users");
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}