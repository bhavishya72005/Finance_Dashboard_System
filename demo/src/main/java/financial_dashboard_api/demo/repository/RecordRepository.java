
package financial_dashboard_api.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import financial_dashboard_api.demo.model.FinancialRecord;
public interface RecordRepository extends MongoRepository<FinancialRecord, String> {
    
}