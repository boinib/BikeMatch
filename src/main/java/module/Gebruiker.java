package module;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class Gebruiker implements Principal {
    private String naam;
    private String email;
    private String telefoonnummer;
    private String wachtwoord;
    private String role;

    private static List<Gebruiker> alleGebruikers;

    static {
        alleGebruikers = new ArrayList<>();
        alleGebruikers.add(new Gebruiker("amin", "amin@hoofd.nl", "112", "geheim", "admin"));
        alleGebruikers.add(new Gebruiker("reda", "reda@hoofd.nl", "112", "geheim1", "klant"));

    }

    public Gebruiker(String naam, String email, String telefoonnummer, String wachtwoord, String role) {
        this.naam = naam;
        this.email = email;
        this.telefoonnummer = telefoonnummer;
        this.wachtwoord = wachtwoord;
        this.role = role;
    }

    public Gebruiker() {
    }

    public static Gebruiker getUserByName(String naam) {
        for (Gebruiker gebruiker : alleGebruikers) {
            if (gebruiker.naam.equals(naam)) {
                return gebruiker;
            }
        }
        return null;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefoonnummer() {
        return telefoonnummer;
    }

    public void setTelefoonnummer(String telefoonnummer) {
        this.telefoonnummer = telefoonnummer;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static String validateLogin(String email, String wachtwoord) {
        for (Gebruiker gebruiker : alleGebruikers) {
            if (gebruiker.getEmail().equals(email) && gebruiker.getWachtwoord().equals(wachtwoord)) {
                return gebruiker.getRole();
            }
        }
        return null; // Invalid login credentials
    }

    @Override
    public String toString() {
        return "Gebruiker [naam=" + naam + ", email=" + email + ", telefoon=" + telefoonnummer + "]";
    }

    @Override
    public String getName() {
        return naam;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
