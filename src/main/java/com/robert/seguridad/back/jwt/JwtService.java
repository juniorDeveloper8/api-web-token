package com.robert.seguridad.back.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";

  public String getToken(UserDetails user) {
    return getToken(new HashMap<>(), user);
  }

  private String getToken(Map<String, Object> extraClaims, UserDetails user) {
    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(user.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
        .signWith(getkey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Key getkey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String getUsernameFromToken(String token) {
    return getClaims(token, Claims::getSubject);
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username=getUsernameFromToken(token);
    return (username.equals(userDetails.getUsername())&& isTokenExpiration(token));
  }

  private Claims getAllClass(String token){
    return Jwts
      .parserBuilder()
      .setSigningKey(getkey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  public <T> T getClaims(String token, Function<Claims,T> claimsResolver) {
    final Claims claims=getAllClass(token);
    return claimsResolver.apply(claims);
  }

  private Date getExperiration(String token) {
    return getClaims(token, Claims::getExpiration);
  }

  private boolean isTokenExpiration(String token){
    return getExperiration(token).before(new Date());
  }
}
