package financial_dashboard_api.demo.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "financial_records")
public class FinancialRecord {
    @Id
    private String id;
    private String userId;
    private String type; // "income" or "expense"
    private double amount;
    private String category;
    private Date date; // ISO format: YYYY-MM-DD
    public FinancialRecord(String userId, String type, double amount, String category, Date date) {
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }
    public String getId() {
        return id;
    }
    public String getUserId() {
        return userId;
    }
    public String getType() {
        return type;
    }
    public double getAmount() {
        return amount;
    }
    public String getCategory() {
        return category;
    }
    public Date getDate() {
        return date;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}