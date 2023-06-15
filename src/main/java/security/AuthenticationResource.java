package security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import module.Gebruiker;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.AbstractMap;
import java.util.Calendar;

@Path("/login")
public class AuthenticationResource {

    private static final int KEY_SIZE_BYTES = 256 / 8; // 256 bits
    private static Key key;

    static {
        key = generateKey();
    }

    public static Key generateKey() {
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            byte[] keyBytes = new byte[KEY_SIZE_BYTES];
            secureRandom.nextBytes(keyBytes);
            return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate key", e);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUser(@FormParam("username") String username, @FormParam("password") String pass) {
        System.out.println("hello");
        String role = Gebruiker.validateLogin(username, pass);
        if (role == null) throw new IllegalArgumentException("No user found");

        String token = createToken(username, role);
        return Response.ok(new AbstractMap.SimpleEntry<>("JWT", token)).build();
    }

    private String createToken(String username, String role) {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.MINUTE, 30);

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expiration.getTime())
                .claim("role", role) // Use a custom claim name like "role" instead of the username
                .signWith(SignatureAlgorithm.HS256, key) // Use HS256 instead of HS512
                .compact();
    }
}
