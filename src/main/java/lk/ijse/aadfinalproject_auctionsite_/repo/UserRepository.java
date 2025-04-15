package lk.ijse.aadfinalproject_auctionsite_.repo;


import lk.ijse.aadfinalproject_auctionsite_.dto.UserDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    User findByEmail(String userName);

    boolean existsByEmail(String userName);

    int deleteByEmail(String userName);

    User findByPhoneNumber(String userName);


    long countByRole(String role);

    List<User> findByRole(String role);
}