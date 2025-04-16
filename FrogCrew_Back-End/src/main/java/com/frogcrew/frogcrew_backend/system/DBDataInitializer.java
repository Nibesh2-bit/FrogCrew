package com.frogcrew.frogcrew_backend.system;


import com.frogcrew.frogcrew_backend.crewmember.CrewMemberUser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class  DBDataInitializer implements CommandLineRunner {
    private final CrewMemberUser crewMemberUser;

    public DBDataInitializer(CrewMemberUser crewMemberUser) {
        this.crewMemberUser = crewMemberUser;
    }

    @Override
    public void run(String... args) throws Exception{

    }

}
