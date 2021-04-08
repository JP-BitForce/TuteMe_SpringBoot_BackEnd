package com.bitforce.tuteme.configuration;


import com.bitforce.tuteme.model.UserAuth;
import com.bitforce.tuteme.repository.UserAuthRepository;
import com.bitforce.tuteme.repository.UserRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TutemeUserDetailsService implements UserDetailsService {
    private final UserAuthRepository userAuthRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuth userAuth = userAuthRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("Not found:" + username)
        );

        return TutemeUserDetails.create(userAuth);
    }

    @Transactional
    public UserDetails loadUserById(Long id) throws NotFoundException {
        UserAuth userAuth = userAuthRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found " + id)
        );

        return TutemeUserDetails.create(userAuth);
    }
}
