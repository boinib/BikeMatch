package model;

public class Winkeleigenaar extends Gebruiker {
    private String bedrijfsnaam;

    public Winkeleigenaar(String naam, String email, String telefoonnummer, String wachtwoord, String role, String bedrijfsnaam) {
        super(naam, email, telefoonnummer, wachtwoord, role);
        this.bedrijfsnaam = bedrijfsnaam;
    }

    public String getBedrijfsnaam() {
        return bedrijfsnaam;
    }

    public void setBedrijfsnaam(String bedrijfsnaam) {
        this.bedrijfsnaam = bedrijfsnaam;
    }
}
