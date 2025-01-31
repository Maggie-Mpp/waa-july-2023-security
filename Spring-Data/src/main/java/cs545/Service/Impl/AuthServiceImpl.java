package cs545.Service.Impl;


import cs545.Domain.dto.UserRequest;
import cs545.Domain.dto.UserResponse;
import cs545.Service.AuthService;
import cs545.utility.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {


    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    @Override
    public UserResponse login(UserRequest loginRequest) {
        Authentication result = null;
        try {
            result = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(result.getName());

        final String accessToken = jwtUtil.generateToken(userDetails.getUsername());
//        final String refreshToken = jwtUtil.generateRefreshToken(loginRequest.getEmail());
        var userResponse = new UserResponse(accessToken, "refreshToken");
        return userResponse;
    }

//    @Override
//    public LoginResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
//        boolean isRefreshTokenValid = jwtUtil.validateToken(refreshTokenRequest.getRefreshToken());
//        if (isRefreshTokenValid) {
//            // TODO (check the expiration of the accessToken when request sent, if the is recent according to
//            //  issue Date, then accept the renewal)
//            var isAccessTokenExpired = jwtUtil.isTokenExpired(refreshTokenRequest.getAccessToken());
//            if(isAccessTokenExpired)
//                System.out.println("ACCESS TOKEN IS EXPIRED"); // TODO Renew is this case
//            else
//                System.out.println("ACCESS TOKEN IS NOT EXPIRED");
//            final String accessToken = jwtUtil.doGenerateToken(  jwtUtil.getSubject(refreshTokenRequest.getRefreshToken()));
//            var loginResponse = new LoginResponse(accessToken, refreshTokenRequest.getRefreshToken());
//            // TODO (OPTIONAL) When to renew the refresh token?
//            return loginResponse;
//        }
//        return new LoginResponse();
//    }
}
