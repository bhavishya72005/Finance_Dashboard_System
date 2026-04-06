package financial_dashboard_api.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;  
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String role;
    private boolean isActive;
    public User (String name, String role, boolean isActive) {
        this.name = name;
        this.role = role;
        this.isActive = isActive;
    }
    public String getId() {
        return id;
    }   
    public String getName() {
        return name;
    }   
    public String getRole() {
        return role;
    }
    public boolean getIsActive() {
        return isActive;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
