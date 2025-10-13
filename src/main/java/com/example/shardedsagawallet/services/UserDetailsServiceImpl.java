package com.example.shardedsagawallet.services;

import com.example.shardedsagawallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.shardedsagawallet.entities.User;




@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            System.out.println("Attempting to load user: " + username);
            User user = userRepository.findByName(username);
            if(user != null){
                System.out.println("User found: " + user.getName() + ", password hash: " + user.getPassword());
                return org.springframework.security.core.userdetails.User.builder()
                        .username(user.getName())
                        .password(user.getPassword())
                        .authorities("USER") // Add default authority
                        .build();
            } else {
                System.out.println("User not found: " + username);
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        } catch (Exception e) {
            System.out.println("Error loading user: " + username + ", error: " + e.getMessage());
            throw new UsernameNotFoundException("Error loading user: " + username, e);
        }
    }
}




