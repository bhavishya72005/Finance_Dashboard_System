package financial_dashboard_api.demo.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import financial_dashboard_api.demo.model.FinancialRecord;
import financial_dashboard_api.demo.model.User;
import financial_dashboard_api.demo.repository.RecordRepository;
import financial_dashboard_api.demo.repository.UserRepository;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        List<FinancialRecord> records = recordRepository.findAll();

        double totalIncome = records.stream()
                .filter(r -> r.getType().equalsIgnoreCase("income"))
                .mapToDouble(FinancialRecord::getAmount)
                .sum();

        double totalExpense = records.stream()
                .filter(r -> r.getType().equalsIgnoreCase("expense"))
                .mapToDouble(FinancialRecord::getAmount)
                .sum();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpense", totalExpense);
        summary.put("netBalance", totalIncome - totalExpense);

        return summary; 
    }
    @PostMapping
    public ResponseEntity<?> createRecord(
            @RequestParam String userId,
            @RequestBody FinancialRecord record) {

        User user = userRepository.findById(userId).orElseThrow();

        if (!user.getRole().equals("ADMIN")) {
            return ResponseEntity.status(403)
                    .body("Access Denied: Insufficient permissions");
        }

        return ResponseEntity.ok(recordRepository.save(record));
    }

    @GetMapping("/monthly")
    public List<Map<String, Object>> getMonthlyTrends() {

        List<FinancialRecord> records = recordRepository.findAll();

        Map<String, Map<String, Double>> monthlyData = new HashMap<>();

        for (FinancialRecord r : records) {

            // Skip invalid data (prevents 500 error)
            if (r.getDate() == null || r.getType() == null) {
                continue;
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(r.getDate());

            String month = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);

            // Initialize month if not present
            monthlyData.putIfAbsent(month, new HashMap<>());
            monthlyData.get(month).putIfAbsent("income", 0.0);
            monthlyData.get(month).putIfAbsent("expense", 0.0);

            // Add values
            if (r.getType().equalsIgnoreCase("INCOME")) {
                monthlyData.get(month).put("income",
                        monthlyData.get(month).get("income") + r.getAmount());
            } else if (r.getType().equalsIgnoreCase("EXPENSE")) {
                monthlyData.get(month).put("expense",
                        monthlyData.get(month).get("expense") + r.getAmount());
            }
        }

        // Convert to response format
        List<Map<String, Object>> result = new ArrayList<>();

        for (String month : monthlyData.keySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("month", month);
            map.put("income", monthlyData.get(month).get("income"));
            map.put("expense", monthlyData.get(month).get("expense"));
            result.add(map);
        }

        return result;
    }
}