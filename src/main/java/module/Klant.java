package module;

public class Klant extends Gebruiker {
    private String adres;

    public Klant(String adres) {
        this.adres = adres;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }
}
