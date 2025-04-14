package com.frogcrew.frogcrew_backend.system;

import com.frogcrew.frogcrew_backend.crewmember.CrewMember;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class  DBDataInitializer implements CommandLineRunner {
    private final CrewMember crewMember;

    public DBDataInitializer(CrewMember crewMember) {
        this.crewMember = crewMember;
    }

    @Override
    public void run(String... args) throws Exception{

    }

}
