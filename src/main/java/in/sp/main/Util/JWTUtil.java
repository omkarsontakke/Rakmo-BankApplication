package in.sp.main.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtil {


    private final long EXPIRATION_TIME = 1000*60*60;

    private final String SECRET = "longsecurejsonwebtokenkeyvalue2025";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateJWT(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) throws Exception {

        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) throws Exception{
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public boolean validateToken(String username, UserDetails userDetails, String token) throws Exception {

        //TODO - Check if username is same as the username in userDetails
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        //TODO - check if token is not expired

    }

    private boolean isTokenExpired(String token) throws Exception {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
