package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import lk.ijse.aadfinalproject_auctionsite_.entity.Inquiry;
import lk.ijse.aadfinalproject_auctionsite_.repo.InquiryRepo;
import lk.ijse.aadfinalproject_auctionsite_.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InquiryServiceImpl implements InquiryService {

    @Autowired
    private InquiryRepo inquiryRepo;
    @Override
    public void save(Inquiry inquiry) {
        inquiryRepo.save(inquiry);
    }
}
