package module;

import java.sql.Time;
import java.util.Date;

public class Afspraak {
    private Gebruiker gebruiker;
    private String datum;
    private String tijd;
    private String opmerking;

    public Afspraak(Gebruiker gebruiker,String datum, String tijd, String opmerking) {
        this.gebruiker = gebruiker;
        this.datum = datum;
        this.tijd = tijd;
        this.opmerking = opmerking;
    }

    public static Afspraak addAfspraak(String naam, String email, String telefoon, String datum, String tijd, String opmerking) {
        Gebruiker gebruiker = new Gebruiker(naam, email, telefoon);
        Afspraak afspraak = new Afspraak(gebruiker,datum, tijd, opmerking);
        System.out.println(afspraak);
        return afspraak;
    }
    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getTijd() {
        return tijd;
    }

    public void setTijd(String tijd) {
        this.tijd = tijd;
    }

    public String getOpmerking() {
        return opmerking;
    }

    public void setOpmerking(String opmerking) {
        this.opmerking = opmerking;
    }

    @Override
    public String toString() {
        return "Afspraak{" +
                "gebruiker=" + gebruiker +
                ", datum='" + datum + '\'' +
                ", tijd='" + tijd + '\'' +
                ", opmerking='" + opmerking + '\'' +
                '}';
    }
    public Gebruiker getGebruiker() {
        return gebruiker;
    }
}
