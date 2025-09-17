package com.ims.backend.util;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256); // 256-bit key
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("Your secure JWT secret key (Base64):");
        System.out.println(base64Key);
    }
}
