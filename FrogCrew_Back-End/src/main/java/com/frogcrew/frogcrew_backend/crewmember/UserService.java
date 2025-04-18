package com.frogcrew.frogcrew_backend.crewmember;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }




    public CrewMemberUser save(CrewMemberUser crewMemberUser) {

        if(userRepository.findByEmail(crewMemberUser.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        crewMemberUser.setPassword(passwordEncoder.encode(crewMemberUser.getPassword()));
        userRepository.save(crewMemberUser);
        return crewMemberUser;

    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
