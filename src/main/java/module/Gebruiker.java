package module;

public class Gebruiker {
    private static String naam;
    private static String email;
    private static String telefoonnummer;

    public Gebruiker(String naam, String email,String telefoonnummer) {
        this.naam = naam;
        this.email = email;
        this.telefoonnummer = telefoonnummer;
    }

    public Gebruiker() {
    }

    public static String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public static String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static String getTelefoonnummer() {
        return telefoonnummer;
    }

    public void setTelefoonnummer(String telefoonnummer) {
        this.telefoonnummer = telefoonnummer;
    }

    @Override
    public String toString() {
        return "Gebruiker [naam=" + naam + ", email=" + email + ", telefoon=" + telefoonnummer + "]";
    }

}

