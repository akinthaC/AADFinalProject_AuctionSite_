package lk.ijse.aadfinalproject_auctionsite_.service;

import lk.ijse.aadfinalproject_auctionsite_.dto.DeliveryDto;
import lk.ijse.aadfinalproject_auctionsite_.entity.Delivery;

public interface DeliveryService {


    Delivery saveDelivery(DeliveryDto dto);

    Delivery findByPurchaseId(String id);


    void save(Delivery delivery);

    void updateById(String orderId);
}
