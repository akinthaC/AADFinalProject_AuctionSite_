package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import lk.ijse.aadfinalproject_auctionsite_.dto.DeliveryDto;
import lk.ijse.aadfinalproject_auctionsite_.entity.Delivery;
import lk.ijse.aadfinalproject_auctionsite_.entity.Purchase;
import lk.ijse.aadfinalproject_auctionsite_.repo.DeliveryRepo;
import lk.ijse.aadfinalproject_auctionsite_.repo.PurchaseRepo;
import lk.ijse.aadfinalproject_auctionsite_.service.DeliveryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private DeliveryRepo deliveryRepository;


    @Autowired
    private PurchaseRepo purchaseRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Delivery saveDelivery(DeliveryDto dto) {
        Purchase purchase = purchaseRepository.findById(String.valueOf(dto.getPurchaseId()))
                .orElseThrow(() -> new RuntimeException("❌ Purchase not found with ID: " + dto.getPurchaseId()));

        Delivery delivery = new Delivery();
        delivery.setPurchase(purchase);
        delivery.setAddress1(dto.getAddress1());
        delivery.setAddress2(dto.getAddress2());
        delivery.setContactNumber(dto.getContactNumber());
        delivery.setPostalCode(dto.getPostalCode());
        delivery.setStatus("Pending");
        delivery.setDeliveryAssignedDate(LocalDate.now());
        delivery.setDateCount("0"); // default
        delivery.setPackageImage(null); // default

        return deliveryRepository.save(delivery);
    }

    @Override
    public Delivery findByPurchaseId(String id) {
        return deliveryRepository.findByPurchaseId(id);
    }

    @Override
    public void save(Delivery delivery) {

        deliveryRepository.save(delivery);
    }

    @Override
    public void updateById(String orderId) {
        Delivery delivery = findByPurchaseId(orderId);

        delivery.setStatus("Delivered");
        deliveryRepository.save(delivery);
    }
}
