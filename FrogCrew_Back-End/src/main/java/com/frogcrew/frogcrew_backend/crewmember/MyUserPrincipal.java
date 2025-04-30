package com.frogcrew.frogcrew_backend.crewmember;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;

// This class is an adapter that turns CrewMemberUser into something Spring Security can use.
// Spring Security works with UserDetails, so we use this class to wrap CrewMemberUser and expose it in the way Spring expects.
public class MyUserPrincipal implements UserDetails {

    private final CrewMemberUser crewMemberUser;

    public MyUserPrincipal(CrewMemberUser crewMemberUser) {
        this.crewMemberUser = crewMemberUser;
    }

    @Override
    // Returns a collection of authorities (roles) for Spring Security.
    // If  roles are space-separated like "admin user", this splits them and adds "ROLE_" prefix as required.
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(StringUtils.tokenizeToStringArray(this.crewMemberUser.getRole(), " "))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
    }

    @Override
    //  Returns the user's hashed password, used by Spring Security for authentication.
    public String getPassword() {
        return this.crewMemberUser.getPassword();
    }

    @Override

    // It should return `crewMemberUser.getEmail()` or `getUsername()`
    public String getUsername() {
        return this.crewMemberUser.getEmail();
    }

    @Override
    // Indicates whether the user's account has expired.
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    // Indicates whether the user is locked. You haven't implemented locking logic, so this default is OK.
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    // Indicates whether the user's credentials (password) are expired. Default is fine unless enforcing password policies.
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    // Indicates if the user is enabled.
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    // These are convenience methods to access crew member properties directly from the principal.
    public String getFirstName() {
        return this.crewMemberUser.getFirstName();
    }

    public String getLastName() {
        return this.crewMemberUser.getLastName();
    }

    public String getEmail() {
        return this.crewMemberUser.getEmail();
    }

    public String getPhoneNumber() {
        return this.crewMemberUser.getPhoneNumber();
    }

    public String getRole() {
        return this.crewMemberUser.getRole();
    }

    public Integer getId() {
        return this.crewMemberUser.getId();
    }

    public CrewMemberUser getCrewMemberUser() {
        return crewMemberUser;
    }
}
