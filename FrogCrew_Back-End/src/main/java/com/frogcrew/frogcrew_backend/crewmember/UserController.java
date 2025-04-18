package com.frogcrew.frogcrew_backend.crewmember;



import com.frogcrew.frogcrew_backend.crewmember.converter.UserDtoToUserConverter;
import com.frogcrew.frogcrew_backend.crewmember.converter.UserToUserDtoConverter;
import com.frogcrew.frogcrew_backend.crewmember.dto.UserDto;
import com.frogcrew.frogcrew_backend.system.Result;
import com.frogcrew.frogcrew_backend.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}")
public class UserController {
    private final UserService userService;

    private final UserDtoToUserConverter userDtoToUserConverter; // Convert userDto to user.

    private final UserToUserDtoConverter userToUserDtoConverter; // Convert user to userDto.


    public UserController(UserService userService, UserDtoToUserConverter userDtoToUserConverter,
                          UserToUserDtoConverter userToUserDtoConverter) {
        this.userService = userService;
        this.userDtoToUserConverter = userDtoToUserConverter;
        this.userToUserDtoConverter = userToUserDtoConverter;

    }



    @PostMapping
    public Result addUser(@RequestBody @Valid CrewMemberUser newUser) {
        CrewMemberUser savedUser  = this.userService.save(newUser);

        UserDto userDto = this.userToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", userDto);
    }



}
