import com.frogcrew.frogcrew_backend.crewmember.CrewMemberUser;
import com.frogcrew.frogcrew_backend.crewmember.MyUserPrincipal;
import com.frogcrew.frogcrew_backend.crewmember.converter.UserToUserDtoConverter;
import com.frogcrew.frogcrew_backend.crewmember.dto.UserDto;
import com.frogcrew.frogcrew_backend.security.JwtProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 🛡️ AuthService is the core service responsible for creating login responses
 * after a user is successfully authenticated (via Basic Auth).
 *
 * This service is called by `AuthController` and:
 * - Generates a JWT token
 * - Converts user data into a DTO
 * - Stores the token temporarily in Redis to manage session lifespan (whitelisting)
 */
@Service
public class AuthService {

    //  Responsible for generating JWT tokens
    private final JwtProvider jwtProvider;

    //  Converts a domain object (HogwartsUser) into a DTO (UserDto) for API responses
    private final UserToUserDtoConverter userToUserDtoConverter;

    // Handles interaction with Redis for caching session tokens
    private final RedisCacheClient redisCacheClient;

    //  Inject dependencies through constructor
    public AuthService(JwtProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter, RedisCacheClient redisCacheClient) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.redisCacheClient = redisCacheClient;
    }

    /**
     * This method is triggered post-authentication and builds the login response.
     *
     * @param authentication — provided by Spring Security when a user logs in successfully.
     * @return Map with 2 keys:
     *         - "userInfo": UserDto (safe public-facing user info)
     *         - "token": JWT string to be used in Authorization headers for future API requests.
     */
    public Map<String, Object> createLoginInfo(Authentication authentication) {
        // Step 1: Extract the user (MyUserPrincipal is your custom wrapper for UserDetails)
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        CrewMemberUser hogwartsUser = principal.getCrewMemberUser();

        // Step 2: Convert domain user to a DTO for safe return
        UserDto userDto = this.userToUserDtoConverter.convert(hogwartsUser);

        //  Step 3: Generate JWT token for this user
        String token = this.jwtProvider.createToken(authentication);

        //  Step 4: Cache token in Redis so you can later validate if the token is still active (whitelist pattern)
        // Key format: "whitelist:{userId}", expires in 2 hours.
        this.redisCacheClient.set("whitelist:" + hogwartsUser.getId(), token, 2, TimeUnit.HOURS);

        // Step 5: Prepare the response object
        Map<String, Object> loginResultMap = new HashMap<>();
        loginResultMap.put("userInfo", userDto); // Contains safe user info (no password, etc.)
        loginResultMap.put("token", token);      // JWT token to use for authorization

        return loginResultMap;
    }
}
