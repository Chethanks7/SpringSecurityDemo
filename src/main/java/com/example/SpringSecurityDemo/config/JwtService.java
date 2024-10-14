package com.example.SpringSecurityDemo.config;

import com.example.SpringSecurityDemo.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "5f2c4e34308e707e4a1d1d9c91a93f60bb12e8580d83414b9f4e0bb927b74bde";

    //extracting a username from the jwt
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject) ;

    }


    //generating a token if not available for the user before
    public String generateToken(
            Map<String, Object> claims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    //validating whether token is setExpiration

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
         return username.equals(userDetails.getUsername()) &&  !isTokenExpired(token);
    }

    //checking an exp date from the token
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //extracting an exp date from the token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    //extracting an each claim from the claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractClaims(token) ;
        return claimsResolver.apply(claims) ;
    }


    //extracting all the claims present in the token
    public Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }
}
