package com.frogcrew.frogcrew_backend.crewmember;

import com.frogcrew.frogcrew_backend.crewmember.converter.UserDtoToUserConverter;
import com.frogcrew.frogcrew_backend.crewmember.converter.UserToUserDtoConverter;
import com.frogcrew.frogcrew_backend.crewmember.dto.CrewMemberDto;
import com.frogcrew.frogcrew_backend.crewmember.dto.UserDto;
import com.frogcrew.frogcrew_backend.crewmember.invite.dto.EmailDto;
import com.frogcrew.frogcrew_backend.system.Result;
import com.frogcrew.frogcrew_backend.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}") // e.g., /api
public class UserController {

    private final UserService userService;
    private final UserToUserDtoConverter userToUserDtoConverter;

    public UserController(UserService userService,
                          UserDtoToUserConverter userDtoToUserConverter,
                          UserToUserDtoConverter userToUserDtoConverter) {
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
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
    public Result registerCrewMember(@RequestParam String token,
                                     @RequestBody @Valid CrewMemberDto dto) {
        CrewMemberUser savedUser = userService.addUser(token, dto);
        UserDto responseDto = userToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", responseDto);
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
     * Find user by Id
     */
    @GetMapping("/crewMember/{userId}")
    public Result findUserById(@PathVariable Integer userId) {
        CrewMemberUser foundUser = this.userService.findById(userId);
        UserDto responseDto = userToUserDtoConverter.convert(foundUser);
        return new Result(true, StatusCode.SUCCESS, "Find Success", responseDto);
    }



}
