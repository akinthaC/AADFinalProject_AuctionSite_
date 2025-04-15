package lk.ijse.aadfinalproject_auctionsite_.entity;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class PurchaseIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        String prefix = "Or";  // Prefix "Or"
        String query = "SELECT COUNT(*) FROM Purchase";  // Query to count current records
        Long count = (Long) session.createQuery(query).getSingleResult();  // Get the count of existing records
        int id = count.intValue() + 1;  // Increment by 1
        return prefix + String.format("%03d", id);  // Format as Or001, Or002, Or003, etc.
    }
}
