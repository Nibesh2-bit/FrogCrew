package com.frogcrew.frogcrew_backend.crewmember.converter;


import com.frogcrew.frogcrew_backend.crewmember.CrewMemberUser;
import com.frogcrew.frogcrew_backend.crewmember.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, CrewMemberUser> {
    @Override
    public CrewMemberUser convert(UserDto userDto) {
        CrewMemberUser crewMemberUser = new CrewMemberUser();
        crewMemberUser.setFirstName(userDto.firstName());
        crewMemberUser.setLastName(userDto.lastName());
        crewMemberUser.setEmail(userDto.email());

        crewMemberUser.setEmail(userDto.email());
        return crewMemberUser;

    }

}
