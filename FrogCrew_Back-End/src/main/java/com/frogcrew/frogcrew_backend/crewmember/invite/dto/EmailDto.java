package com.frogcrew.frogcrew_backend.crewmember.invite.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class EmailDto {
    @NotEmpty
    private List<@Email String > emails;

    public String[] getEmails() {
        return emails.toArray(new String[emails.size()]);
    }

    public void setEmails(@NotEmpty List<@Email String> emails) {
        this.emails = emails;
    }
}
