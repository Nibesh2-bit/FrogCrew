package com.frogcrew.frogcrew_backend.crewmember.converter;


import com.frogcrew.frogcrew_backend.crewmember.CrewMemberUser;
import com.frogcrew.frogcrew_backend.crewmember.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<CrewMemberUser, UserDto> {
    @Override
    public UserDto convert(CrewMemberUser source) {
        final UserDto userDto = new UserDto(source.getId(), source.getUsername(), source.getPassword(), source.getEmail());
        return userDto;
    }




}
