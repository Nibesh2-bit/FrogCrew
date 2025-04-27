package com.frogcrew.frogcrew_backend.crewmember;

import com.frogcrew.frogcrew_backend.crewmember.converter.UserDtoToUserConverter;
import com.frogcrew.frogcrew_backend.crewmember.converter.UserToUserDtoConverter;
import com.frogcrew.frogcrew_backend.crewmember.dto.CrewMemberDto;
import com.frogcrew.frogcrew_backend.crewmember.dto.SimpleUserDto;
import com.frogcrew.frogcrew_backend.crewmember.dto.UserDto;
import com.frogcrew.frogcrew_backend.invite.InvitationService;
import com.frogcrew.frogcrew_backend.invite.dto.EmailDto;
import com.frogcrew.frogcrew_backend.system.Result;
import com.frogcrew.frogcrew_backend.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}") // e.g., /api
public class UserController {

    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final UserDtoToUserConverter userDtoToUserConverter;
    private final InvitationService invitationService;

    public UserController(UserService userService,
                          UserDtoToUserConverter userDtoToUserConverter,
                          UserToUserDtoConverter userToUserDtoConverter, InvitationService invitationService) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.userDtoToUserConverter = userDtoToUserConverter;
        this.invitationService = invitationService;
    }

    /**
     * POST /invite — Sends invitation emails to a list of users.
     */
    @PostMapping("/invite")
    public Result sendInvites(@RequestBody @Valid EmailDto dto) {
        userService.sendInvites(dto);
        return new Result(true, StatusCode.SUCCESS, "Invitation Success", dto.getEmails());
    }



    /**
     * GET /invite/{token} — Validates if a token is usable (not expired or used).
     */
    @GetMapping("/invite/{token}")
    public Result validateInvite(@PathVariable String token) {
        boolean valid = userService.validateToken(token);
        if (valid) {
            return new Result(true, StatusCode.SUCCESS, "Invitation valid", token);
        } else {
            return new Result(false, StatusCode.NOT_FOUND, "Invitation not valid", token);
        }
    }

    /**
     * POST /crewMember — Registers a new crew member from the provided data and token.
     */

    @PostMapping("/crewMember")
    public Result registerCrewMember(@RequestParam String email_token,
                                     @RequestBody @Valid CrewMemberDto dto) {

       try {
           CrewMemberUser savedUser = userService.addUser(email_token, dto);
           UserDto responseDto = userToUserDtoConverter.convert(savedUser);
           return new Result(true, StatusCode.SUCCESS, "Add Success", responseDto);
       }catch (Exception e){
           return new Result(false, StatusCode.NOT_FOUND, "Provided arguments are invalid, see data for details.", e.getMessage());
       }
    }

    /***
     * API DOC TESTS
     */

    /**
     * Find All Users*/

    @GetMapping("/crewMember")
    public Result findAllUsers(){
        List<CrewMemberUser> foundUsers = this.userService.findAll();
        List<UserDto> userDtos = foundUsers.stream().map(this.userToUserDtoConverter::convert).collect(Collectors.toList());

        //convert list to userDtos
        return new Result(true, StatusCode.SUCCESS, "Find All Success", userDtos);



    }

    /**
     * Endpoint for regular users to access directory-level details (SimpleDtoDTO).
     * This adheres to Business Rule BR-13.
     */

    @GetMapping("/crewMember/{userId}/user")
    public Result findUserByIdUserView(@PathVariable Integer userId) {
        SimpleUserDto foundUser = this.userService.getCrewMemberForUserView(userId);

        return new Result(true, StatusCode.SUCCESS, "Find Success", foundUser);
    }
    @PreAuthorize("hasAuthority('ROLE_Admin')")
    @GetMapping("/crewMember/{userId}/admin")
    public Result findUserByIdAdminView(@PathVariable Integer userId) {
        CrewMemberUser foundUser = this.userService.getCrewMemberAdminView(userId);
        return new Result(true, StatusCode.SUCCESS, "Find Success", foundUser);

    }






}
