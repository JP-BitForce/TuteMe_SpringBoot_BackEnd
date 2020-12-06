package com.bitforce.tuteme.configuration;


import com.bitforce.tuteme.model.User;
import com.bitforce.tuteme.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TutemeUserDetailsService implements UserDetailsService {

    @Autowired
   private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                ()->new UsernameNotFoundException("Not found:"+username)
        );

        return TutemeUserDetails.create(user);
    }

    @Transactional
    public UserDetails loadUserById(int id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found "+id)
        );

        return TutemeUserDetails.create(user);
    }
}
