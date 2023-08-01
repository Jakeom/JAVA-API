package com.softpuzzle.angkor.utility;

import org.springframework.stereotype.Service;

import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import lombok.extern.slf4j.Slf4j;

@Service("TokenUtil")
@Slf4j
public class TokenUtil {

	  // 암호화하기 위한 키
	  private static String SECRET_KEY = "SoftPuzzle-Ankor@#$12890SKUIM";
	  
	  // JWT 만료 시간 1시간
	  private static long tokenValidMilisecond = 1000L * 60 * 60;

	  
	 // 토큰 생성 함수
	  public static String createToken(String key) {
	    // Claims을 생성
	    var claims = Jwts.claims().setId(key);
	    // Payload 데이터 추가
	    claims.put("SoftPuzzle-Ankor", "SoftPuzzle-Ankor-AccessToekn#1245!");
	    // 현재 시간
	    Date now = new Date();
	    // JWT 토큰을 만드는데, Payload 정보와 생성시간, 만료시간, 알고리즘 종류와 암호화 키를 넣어 암호화 함.
	    return Jwts.builder()
	               .setClaims(claims)
	               .setIssuedAt(now)
	               .setExpiration(new Date(now.getTime() + tokenValidMilisecond))
	               //.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
	               .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
	               .compact();
	  }
	  
	  
	  // String으로 된 코드를 복호화한다.
	  public static Jws<Claims> getClaims(String jwt) {
	    try {
	      // 암호화 키로 복호화한다.
	      // 즉 암호화 키가 다르면 에러가 발생한다.
	      return Jwts.parser()
	                 .setSigningKey(SECRET_KEY)
	                 .parseClaimsJws(jwt);
	    }catch(SignatureException e) {
	      return null;
	    }
	  }
	  // 토큰 검증 함수
	  public static boolean validateToken(Jws<Claims> claims) {
	    // 토큰 만료 시간이 현재 시간을 지났는지 검증
	    return !claims.getBody()
	                  .getExpiration()
	                  .before(new Date());
	  }
	  // 토큰을 통해 Payload의 ID를 취득
	  public static String getKey(Jws<Claims> claims) {
	    // Id 취득
	    return claims.getBody()
	                 .getId();
	  }
	  // 토큰을 통해 Payload의 데이터를 취득
	  public static Object getClaims(Jws<Claims> claims, String key) {
	    // 데이터 취득
	    return claims.getBody()
	                 .get(key);
	  }

}
