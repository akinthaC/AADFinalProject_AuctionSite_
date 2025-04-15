package lk.ijse.aadfinalproject_auctionsite_.service.impl;


import lk.ijse.aadfinalproject_auctionsite_.dto.UserDTO;
import lk.ijse.aadfinalproject_auctionsite_.entity.User;
import lk.ijse.aadfinalproject_auctionsite_.repo.UserRepository;
import lk.ijse.aadfinalproject_auctionsite_.service.UserService;
import lk.ijse.aadfinalproject_auctionsite_.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    public UserDTO loadUserDetailsByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        return modelMapper.map(user,UserDTO.class);
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return authorities;
    }

    @Override
    public UserDTO searchUser(String username) {
        if (userRepository.existsByEmail(username)) {
            User user=userRepository.findByEmail(username);
            return modelMapper.map(user,UserDTO.class);
        } else {
            return null;
        }
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public int saveUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return VarList.Not_Acceptable;
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//            userDTO.setRole("USER");
            userRepository.save(modelMapper.map(userDTO, User.class));
            return VarList.Created;
        }

    }
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return new UserDTO(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getPassword(),
                    user.getRole(),
                    user.getDescription(),
                    user.getAddressLine1(),
                    user.getAddressLine2(),
                    user.getLatitude(),
                    user.getLongitude(),
                    user.getStatus()
            );
        } else {
            return null; // Handle properly in the controller
        }
    }

    @Override
    public UserDTO getUserById(Long userId) {
        Optional<User> user = userRepository.findById(String.valueOf(userId));

        // Check if user exists, then map it to UserDTO
        if (user.isPresent()) {
            User u = user.get();
            return new UserDTO(
                    u.getId(),
                    u.getFirstName(),
                    u.getLastName(),
                    u.getEmail(),
                    u.getPhoneNumber(),
                    u.getPassword(),
                    u.getRole(),
                    u.getDescription(),
                    u.getAddressLine1(),
                    u.getAddressLine2(),
                    u.getLatitude(),
                    u.getLongitude(),
                    u.getStatus()
            );
        } else {
            return null; // Handle properly in the controller
        }

    }

    @Override
    public List<UserDTO> getUsersByRoles(String role) {
        // Fetching users with the "seller" role
        List<User> users = userRepository.findByRole(role);

        // Map the list of User entities to UserDTOs using ModelMapper
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class)) // Convert User entity to UserDTO
                .collect(Collectors.toList());
    }

    @Override
    public void updateUserStatus(Long userId, String newStatus) {
        User user = userRepository.findById(String.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(newStatus);
        userRepository.save(user);
    }

    @Override
    public boolean updateUserProfile(String email, UserDTO dto) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email));

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setEmail(dto.getEmail());
            user.setAddressLine1(dto.getAddressLine1());
            user.setAddressLine2(dto.getAddressLine2());
            user.setPhoneNumber(dto.getPhoneNumber());

            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(dto.getPassword());
                user.setPassword(encodedPassword);
            }

            userRepository.save(user);
            return true;
        }

        return false;
    }





}
