package financial_dashboard_api.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import financial_dashboard_api.demo.model.FinancialRecord;
import financial_dashboard_api.demo.model.User;
import financial_dashboard_api.demo.repository.RecordRepository;
import financial_dashboard_api.demo.repository.UserRepository;

@RestController
@RequestMapping("/records")
public class RecordController {
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private UserRepository userRepository;
    
    //create
    @PostMapping
    public ResponseEntity<?> createRecord(
            @RequestParam String userId,
            @RequestBody FinancialRecord record) {

        User user = userRepository.findById(userId).orElseThrow();

        if (!user.getRole().equals("ADMIN")) {
            return ResponseEntity.status(403)
                    .body("Access Denied: Insufficient permissions");
        }

        record.setUserId(userId); 

        return ResponseEntity.ok(recordRepository.save(record));
    }

    //read
    @GetMapping
    public ResponseEntity<?> getRecords(
            @RequestParam String userId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category) {
                
                User user = userRepository.findById(userId).orElseThrow();
                if (user.getRole().equals("VIEWER")) {
                    return ResponseEntity.status(403)
                            .body("Access Denied: Viewers cannot access financial records");
                }

        List<FinancialRecord> records = recordRepository.findAll();

        if (type != null) {
            records = records.stream()
                    .filter(r -> r.getType() != null && r.getType().equalsIgnoreCase(type))
                    .toList();
        }

        if (category != null) {
            records = records.stream()
                    .filter(r -> r.getCategory() != null && r.getCategory().equalsIgnoreCase(category))
                    .toList();
        }

        return ResponseEntity.ok(records);
    }

    //update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecord(
            @PathVariable String id,
            @RequestParam String userId,
            @RequestBody FinancialRecord updatedRecord) {

        try {

            //  Check user performing action
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(404).body("User not found");
            }

            User user = optionalUser.get();

            //  Only ADMIN allowed
            if (!user.getRole().equals("ADMIN")) {
                return ResponseEntity.status(403)
                        .body("Access Denied: Only ADMIN can update records");
            }

            //  Fetch record
            Optional<FinancialRecord> optionalRecord = recordRepository.findById(id);
            if (optionalRecord.isEmpty()) {
                return ResponseEntity.status(404).body("Record not found");
            }

            FinancialRecord record = optionalRecord.get();

            //  Update fields
            record.setType(updatedRecord.getType());
            record.setAmount(updatedRecord.getAmount());
            record.setCategory(updatedRecord.getCategory());
            record.setDate(updatedRecord.getDate());

            return ResponseEntity.ok(recordRepository.save(record));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Something went wrong: " + e.getMessage());
        }
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(
            @PathVariable String id,
            @RequestParam String userId) {

        try {

            // Check if user exists
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.status(404).body("User not found");
            }

            // Only ADMIN allowed
            if (!user.getRole().equals("ADMIN")) {
                return ResponseEntity.status(403)
                        .body("Access Denied: Only ADMIN can delete records");
            }

            // Check if record exists
            FinancialRecord record = recordRepository.findById(id).orElse(null);
            if (record == null) {
                return ResponseEntity.status(404).body("Record not found");
            }

            // Delete record
            recordRepository.deleteById(id);

            return ResponseEntity.ok("Record deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error: " + e.getMessage());
        }
    }
    
}