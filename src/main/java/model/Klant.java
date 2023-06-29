package model;

public class Klant extends Gebruiker {
    private String adres;

    public Klant(String naam, String email, String telefoonnummer, String wachtwoord, String role, String adres) {
        super(naam, email, telefoonnummer, wachtwoord, role);
        this.adres = adres;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }
}
