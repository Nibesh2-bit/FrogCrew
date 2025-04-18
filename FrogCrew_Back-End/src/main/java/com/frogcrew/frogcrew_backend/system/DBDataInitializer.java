package com.frogcrew.frogcrew_backend.system;


import com.frogcrew.frogcrew_backend.crewmember.CrewMemberUser;
import com.frogcrew.frogcrew_backend.crewmember.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class  DBDataInitializer implements CommandLineRunner {
    private final UserService userService;

    public DBDataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception{

    }

}
