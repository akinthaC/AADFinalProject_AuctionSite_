package lk.ijse.aadfinalproject_auctionsite_.service;


import lk.ijse.aadfinalproject_auctionsite_.dto.UserDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.User;

import java.util.List;

public interface UserService {
    int saveUser(UserDTO userDTO);
    UserDTO searchUser(String username);

    User findByEmail(String email);

    UserDTO getUserByEmail(String email);


    UserDTO getUserById(Long userId) ;


    List<UserDTO> getUsersByRoles(String seller);


    void updateUserStatus(Long id, String newStatus);

    boolean updateUserProfile(String email, UserDTO updatedUser);
}