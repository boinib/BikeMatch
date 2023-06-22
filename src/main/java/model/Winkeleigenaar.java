package model;

public class Winkeleigenaar extends Gebruiker {
    private String bedrijfsnaam;

    public Winkeleigenaar(String bedrijfsnaam) {
        super();
        this.bedrijfsnaam = bedrijfsnaam;
    }

    public String getBedrijfsnaam() {
        return bedrijfsnaam;
    }

    public void setBedrijfsnaam(String bedrijfsnaam) {
        this.bedrijfsnaam = bedrijfsnaam;
    }
}
