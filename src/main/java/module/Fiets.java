package module;

import net.minidev.json.annotate.JsonIgnore;

public class Fiets {
    private String id;
    private String merk;
    private String type;
    private String prijs;
    private String gewicht;
    private String versnellingen;
    private String remmen;
    private String beschrijving;
    private String afbeelding;
    private String wielmaat;
    private String framemaat;
    private String materiaalframe;
    private String voorvork;
    private String verlichting;
    private String bagagedrager;
    private String slot;
    private String link;

    @JsonIgnore
    public Fiets(String id, String merk, String type, String prijs, String gewicht, String versnellingen, String remmen, String beschrijving, String afbeelding, String wielmaat, String framemaat, String materiaalframe, String voorvork, String verlichting, String bagagedrager, String slot, String link) {
        this.id = id;
        this.merk = merk;
        this.type = type;
        this.prijs = prijs;
        this.gewicht = gewicht;
        this.versnellingen = versnellingen;
        this.remmen = remmen;
        this.beschrijving = beschrijving;
        this.afbeelding = afbeelding;
        this.wielmaat = wielmaat;
        this.framemaat = framemaat;
        this.materiaalframe = materiaalframe;
        this.voorvork = voorvork;
        this.verlichting = verlichting;
        this.bagagedrager = bagagedrager;
        this.slot = slot;
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrijs() {
        return prijs;
    }

    public void setPrijs(String prijs) {
        double prijsValue;
        try {
            prijsValue = Double.parseDouble(prijs);
        } catch (NumberFormatException e) {
            // Ongeldige prijswaarde, behoud de huidige prijs
            return;
        }

        if (prijsValue >= 0) {
            this.prijs = prijs;
        }
    }


    public String getGewicht() {
        return gewicht;
    }

    public void setGewicht(String gewicht) {
        this.gewicht = gewicht;
    }

    public String getVersnellingen() {
        return versnellingen;
    }

    public void setVersnellingen(String versnellingen) {
        this.versnellingen = versnellingen;
    }

    public String getRemmen() {
        return remmen;
    }

    public void setRemmen(String remmen) {
        this.remmen = remmen;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public String getAfbeelding() {
        return afbeelding;
    }

    public void setAfbeelding(String afbeelding) {
        this.afbeelding = afbeelding;
    }

    public String getWielmaat() {
        return wielmaat;
    }

    public void setWielmaat(String wielmaat) {
        this.wielmaat = wielmaat;
    }

    public String getFramemaat() {
        return framemaat;
    }

    public void setFramemaat(String framemaat) {
        this.framemaat = framemaat;
    }

    public String getMateriaalframe() {
        return materiaalframe;
    }

    public void setMateriaalframe(String materiaalframe) {
        this.materiaalframe = materiaalframe;
    }

    public String getVoorvork() {
        return voorvork;
    }

    public void setVoorvork(String voorvork) {
        this.voorvork = voorvork;
    }

    public String getVerlichting() {
        return verlichting;
    }

    public void setVerlichting(String verlichting) {
        this.verlichting = verlichting;
    }

    public String getBagagedrager() {
        return bagagedrager;
    }

    public void setBagagedrager(String bagagedrager) {
        this.bagagedrager = bagagedrager;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
}

