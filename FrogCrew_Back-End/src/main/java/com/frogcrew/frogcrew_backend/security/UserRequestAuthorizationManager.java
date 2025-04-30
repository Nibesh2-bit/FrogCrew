package com.frogcrew.frogcrew_backend.security;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

import java.util.Map;
import java.util.function.Supplier;

/**
 * This custom AuthorizationManager is used to protect endpoints like /users/{userId}.
 *
 * Its job is to authorize requests based on:
 * - The role of the user (does the user have ROLE_user or ROLE_admin?)
 * - The user ID in the JWT vs. the user ID in the URL
 ** Key Updates:
 *  * - BR-13: Crew Members (ROLE_user) can access directory information of other Crew Members.
 *  * - BR-37: Only Admins (ROLE_admin) can access sensitive data.
 *  * Updated to allow users to view other profiles, but restrict sensitive data to Admins.
 *  *
**
 *
 *
 * This is plugged into Spring Security config using `.access(new UserRequestAuthorizationManager())`.
 */
@Component
public class UserRequestAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    // Define a URI pattern with a path variable {userId}
    private static final UriTemplate USER_URI_TEMPLATE = new UriTemplate("/crewMember/{userId}");

    /**
     * Core authorization logic.
     * This method is called whenever a protected URL is accessed.
     *
     * @param authenticationSupplier supplies the Authentication object (e.g. a Jwt-based principal)
     * @param context holds the request, including the URL
     * @return AuthorizationDecision - grants or denies access
     */
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {

        // 1. Extract userId from the actual URI the client hit
        // e.g. /users/42 -> userIdFromRequestUri = "42"
        Map<String, String> uriVariables = USER_URI_TEMPLATE.match(context.getRequest().getRequestURI());
        String userIdFromRequestUri = uriVariables.get("userId");

        // 2. Get Authentication object from Spring Security context
        Authentication authentication = authenticationSupplier.get();

        // 3. Extract the userId from the JWT (set during login)
        // We assume the JWT has a claim named "userId"
        String userIdFromJwt = ((Jwt) authentication.getPrincipal()).getClaim("userId").toString();

        // 4. Check if the user has ROLE_User
        boolean hasUserRole = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_User"));

        // 5. Check if the user has ROLE_Admin
        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_Admin"));


        //6.Authorization Logic:
        boolean isAuthorized = false;

        //case1: Admins can access any Crew Member's Profile
        if(hasAdminRole){
            isAuthorized = true;
        }
        //case2: Crew Members can accecess any Crew Member's directory info

        else if (hasUserRole && userIdFromRequestUri != null){
            isAuthorized = true;
        }


        //7: Return the decision

        return new AuthorizationDecision(isAuthorized);
    }
}
