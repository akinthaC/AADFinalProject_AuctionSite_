package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import lk.ijse.aadfinalproject_auctionsite_.dto.InquiryDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.Inquiry;
import lk.ijse.aadfinalproject_auctionsite_.repo.InquiryRepo;
import lk.ijse.aadfinalproject_auctionsite_.service.InquiryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InquiryServiceImpl implements InquiryService {

    @Autowired
    private InquiryRepo inquiryRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public void save(Inquiry inquiry) {
        inquiryRepo.save(inquiry);
    }

    @Override
    public List<InquiryDTO> getAllInquiries() {
        return inquiryRepo.findAll()
                .stream()
                .map(inquiry -> modelMapper.map(inquiry, InquiryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean markAsResponded(Long id) {
        Optional<Inquiry> optional = inquiryRepo.findById(id);
        if (optional.isPresent()) {
            Inquiry inquiry = optional.get();
            inquiry.setResponed("Yes");
            inquiryRepo.save(inquiry);
            return true;
        }
        return false;

    }
}
