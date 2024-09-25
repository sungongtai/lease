package com.atguigu.lease.common.utils;

import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static SecretKey secretKey = Keys.hmacShaKeyFor("eX6>gK5/lC1:hB6[iY3=wF7}bU9=iA0@".getBytes());

    public static String createToken(long userId, String username) {
        String jwt = Jwts.builder().
                setExpiration(new Date(System.currentTimeMillis() + 3600000)).
                setSubject("LOGIN_USER").
                claim("userId", userId).
                claim("userName", username).
                signWith(secretKey, SignatureAlgorithm.HS256).
                compact();
        return jwt;
    }

    public static Claims parseToken(String token){
        if(token == null){
            throw new LeaseException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        try {
            JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
            return claimsJws.getBody();
        } catch (ExpiredJwtException e) {
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
        }catch (JwtException e){
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }
    }

    public static void main(String[] args) {
        System.out.println(createToken(8L, "15653248360"));
    }
}
