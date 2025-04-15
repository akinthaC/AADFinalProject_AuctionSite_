package lk.ijse.aadfinalproject_auctionsite_.repo;

import lk.ijse.aadfinalproject_auctionsite_.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepo extends JpaRepository<Inquiry, Long> {
}
