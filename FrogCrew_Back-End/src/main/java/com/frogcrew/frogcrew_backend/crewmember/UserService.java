package com.frogcrew.frogcrew_backend.crewmember;

import com.frogcrew.frogcrew_backend.crewmember.dto.CrewMemberDto;
import com.frogcrew.frogcrew_backend.crewmember.invite.EmailService;
import com.frogcrew.frogcrew_backend.crewmember.invite.InvitationService;
import com.frogcrew.frogcrew_backend.crewmember.invite.InvitationToken;
import com.frogcrew.frogcrew_backend.crewmember.invite.InvitationRepository;
import com.frogcrew.frogcrew_backend.crewmember.invite.dto.EmailDto;
import com.frogcrew.frogcrew_backend.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final InvitationRepository invitationRepository;
    private final EmailService emailService;
    private final InvitationService invitationService;

    // Constructor injection of required dependencies

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       InvitationRepository invitationRepository,
                       EmailService emailService, InvitationService invitationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.invitationRepository = invitationRepository;
        this.emailService = emailService;
        this.invitationService = invitationService;
    }
    /**
     * Registers a user based on a valid invitation token and the submitted data.
     */
    public CrewMemberUser addUser(String token, @Valid CrewMemberDto dto) {
//        // 1. Check if the token exists
//        InvitationToken invite = invitationRepository.findByToken(token)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invitation not valid"));
//
//        // 2. Check if the token is used or expired
//        if (invite.isUsed() || invite.getExpiresAt().isBefore(LocalDateTime.now())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired or already used");
//        }
//
//        // 3. Check if email in the token matches the submitted one
//        if (!invite.getEmail().equals(dto.email())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email does not match invitation");
//        }

        InvitationToken invite = invitationService.validateToken(token, dto.email());

        // 4. Create and save the new user
        CrewMemberUser newUser = new CrewMemberUser();
        newUser.setEmail(dto.email());
        newUser.setFirstName(dto.firstName());
        newUser.setLastName(dto.lastName());
        newUser.setPassword(passwordEncoder.encode(dto.password()));
        newUser.setRole(dto.role());
        newUser.setPositions(dto.positions());
        newUser.setPhoneNumber(dto.phoneNumber());

        userRepository.save(newUser);

        // 5. Mark the invitation token as used and save
        invite.setUsed(true);
        invitationRepository.save(invite);

        // 6. Return the saved user
        return newUser;
    }

    /**
     * Sends one-time invitation links to the provided list of emails.
     */
    public void sendInvites(@Valid EmailDto dto) {
        for (String email : dto.getEmails()) {
            String token = UUID.randomUUID().toString();

            // Create and store the token
            InvitationToken invite = new InvitationToken(email, token, false, LocalDateTime.now().plusHours(24));
            invitationRepository.save(invite);

            // Send the invitation email with the link (printed to console or sent)
            String link = "http://localhost:8080/invite/" + token;
            emailService.send(email, "FrogCrew Invite", "Use this link to register: " + link);
        }
    }

    /**
     * Validates the token by checking usage and expiration.
     */
    public boolean validateToken(String token) {
        Optional<InvitationToken> optional = invitationRepository.findByToken(token);
        if (optional.isEmpty()) {
            return false;
        }

        InvitationToken invite = optional.get();
        return !invite.isUsed() && invite.getExpiresAt().isAfter(LocalDateTime.now());
    }

    /**
     * (Unimplemented) Used for Spring Security login support if needed.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CrewMemberUser user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new MyUserPrincipal(user); // Never return null here
    }

    /**
     * Find all users
     */

    public List<CrewMemberUser> findAll() {

        return userRepository.findAll();
    }
    /**
     * Find user by Id
     */


    public CrewMemberUser findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("CrewMember", id));
    }




}
