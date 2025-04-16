package lk.ijse.aadfinalproject_auctionsite_.service;

import lk.ijse.aadfinalproject_auctionsite_.dto.InquiryDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.Inquiry;

import java.util.List;

public interface InquiryService {
    void save(Inquiry inquiry);

    List<InquiryDTO> getAllInquiries();

    boolean markAsResponded(Long id);
}
