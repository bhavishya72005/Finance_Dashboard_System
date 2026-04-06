package financial_dashboard_api.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import financial_dashboard_api.demo.model.User;

public interface UserRepository extends MongoRepository<User, String>
{
    
}
