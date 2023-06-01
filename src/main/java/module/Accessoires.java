package module;

public class Accessoires {
    private String id;
    private String naam;
    private String afbeelding;
    private String prijs;
    private String beschrijving;
    private String voorraad;

    public Accessoires(String id, String naam, String afbeelding, String prijs, String beschrijving, String voorraad) {
        this.id = id;
        this.naam = naam;
        this.afbeelding = afbeelding;
        this.prijs = prijs;
        this.beschrijving = beschrijving;
        this.voorraad = voorraad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getAfbeelding() {
        return afbeelding;
    }

    public void setAfbeelding(String afbeelding) {
        this.afbeelding = afbeelding;
    }

    public String getPrijs() {
        return prijs;
    }

    public void setPrijs(String prijs) {
        this.prijs = prijs;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public String getVoorraad() {
        return voorraad;
    }

    public void setVoorraad(String voorraad) {
        this.voorraad = voorraad;
    }
}
