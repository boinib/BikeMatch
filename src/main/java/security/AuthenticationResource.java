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
        return Response.ok(new SimpleEntry<>("JWT", token)).build();
    }

    private String createToken(String username, String role) {
        Calendar expiration = Calendar.getInstance();
        expiration.add(Calendar.MINUTE, 30);

        Gebruiker gebruiker = Gebruiker.getUserByName(username);
        if (gebruiker == null) {
            throw new IllegalArgumentException("No user found");
        }

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expiration.getTime())
                .claim("role", role)
                .claim("username", username)
                .claim("naam", gebruiker.getNaam()) // Extra claim voor naam (gebruikersnaam)
                .claim("email", gebruiker.getEmail()) // Extra claim voor email
                .claim("telefoonnummer", gebruiker.getTelefoonnummer()) // Extra claim voor telefoonnummer
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    static SecretKey generateSecretKey() {
        try {
            // Generate a secret key
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            return keyGenerator.generateKey();
        } catch (Exception e) {
            // Handle any exception that occurs during key generation
            e.printStackTrace();
            return null;
        }
    }
}
