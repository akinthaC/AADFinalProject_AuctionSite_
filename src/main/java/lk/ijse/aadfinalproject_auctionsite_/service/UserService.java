package lk.ijse.aadfinalproject_auctionsite_.service;


import lk.ijse.aadfinalproject_auctionsite_.dto.UserDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.User;

public interface UserService {
    int saveUser(UserDTO userDTO);
    UserDTO searchUser(String username);

    User findByEmail(String email);

    UserDTO getUserByEmail(String email);


    UserDTO getUserById(Long userId) ;
}