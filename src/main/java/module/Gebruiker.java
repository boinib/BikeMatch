package module;

import java.util.ArrayList;
import java.util.List;

public class Gebruiker {
    private String naam;
    private String email;
    private String telefoonnummer;
    private static String wachtwoord;
    private static String role;

    private static List<Gebruiker> alleGebruikers = new ArrayList<>();

    static{
        alleGebruikers =  new ArrayList<>();
        alleGebruikers.add(new Gebruiker("amin","amin@hoofd.nl","112","geheim","admin"));
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

    public static String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        Gebruiker.wachtwoord = wachtwoord;
    }

    public static String getRole() {
        return role;
    }

    public void setRole(String role) {
        Gebruiker.role = role;
    }

    public static String validateLogin(String email, String wachtwoord) {
        for (Gebruiker gebruiker : alleGebruikers) {
            if (gebruiker.getEmail().equals(email) && gebruiker.getWachtwoord().equals(wachtwoord)) {
                return gebruiker.getRole();
            }
        }
        return null; // Inloggegevens ongeldig
    }

    @Override
    public String toString() {
        return "Gebruiker [naam=" + naam + ", email=" + email + ", telefoon=" + telefoonnummer + "]";
    }
}
