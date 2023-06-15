package security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import module.Gebruiker;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Key;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;

@Path("/login")
public class AuthenticationResource {
    // Change the key to a SecretKey object
    public static final Key key = generateSecretKey();

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticateUser(@FormParam("username") String username, @FormParam("password") String pass) {
        String role = Gebruiker.validateLogin(username, pass);
        if (role == null) throw new IllegalArgumentException("No user found");
        String token = createToken(username, role);
        if ("admin".equals(role)) {
            return Response.status(Response.Status.OK)
                    .entity(new SimpleEntry<>("adminCode", token))
                    .build();
        } else {
            return Response.ok(new SimpleEntry<>("abc", token)).build();
        }
    }
    private String createToken(String username, String role) {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.MINUTE, 30);

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expiration.getTime())
                .claim("username", username) // Voeg de gebruikersnaam toe aan de claims
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS256, key) // Use the provided key for signing
                .compact();
    }

    static SecretKey generateSecretKey() {
        try {
            // Generate a secret key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            System.out.println(keyGenerator);
            return keyGenerator.generateKey();
        } catch (Exception e) {
            // Handle any exception that occurs during key generation
            e.printStackTrace();
            return null;
        }
    }
}
