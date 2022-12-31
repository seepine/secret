package com.seepine.secret.test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.type.TypeReference;
import com.seepine.json.Json;
import com.seepine.json.JsonObject;
import com.seepine.secret.entity.AuthUser;

import java.sql.Date;
import java.time.Instant;
import java.util.Map;

public class JwtTest {
  public static void main(String[] args) {
    Algorithm algorithm = Algorithm.HMAC256("secret");

    Map<String, String> map =
        Json.convert(
            AuthUser.builder()
                .id(156156)
                .nickName("Tom")
                .avatarUrl("https://example.com/avatar.png")
                .build(),
            new TypeReference<>() {});

    JWTCreator.Builder jwtBuilder =
        JWT.create()
            .withIssuedAt(Date.from(Instant.now()))
            .withExpiresAt(Instant.now().plusSeconds(5))
            .withIssuer("secret");

    map.forEach(
        (key, value) -> {
          if (value != null) {
            jwtBuilder.withClaim(key, value);
          }
          System.out.println(key + "," + value);
        });
    String token = jwtBuilder.sign(algorithm);
    System.out.println(token);

    JWTVerifier verifier = JWT.require(algorithm).withIssuer("secret").build();

    DecodedJWT decodedJWT = verifier.verify(token);
    print(decodedJWT);
  }

  public static void print(DecodedJWT decodedJWT) {
    String str = "";
    str += "issuer:" + decodedJWT.getIssuer() + "\n";
    str += "issuedAt:" + decodedJWT.getIssuedAt() + "\n";
    str += "expiresAt:" + decodedJWT.getExpiresAt() + "\n";
    str += "claims:" + decodedJWT.getClaims().toString() + "\n";
    JsonObject json = new JsonObject();
    decodedJWT.getClaims().forEach((key, claim) -> json.set(key, claim.asString()));
    AuthUser user = Json.parse(json.toString(), AuthUser.class);
    System.out.println(user);
    System.out.println(str);
  }
}
