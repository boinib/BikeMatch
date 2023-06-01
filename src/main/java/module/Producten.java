package module;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Producten {
    private List<Fiets> alleProducten = new ArrayList<>();
    private List<Accessoires> accessoires = new ArrayList<>();

    private static Producten mijnproducten = new Producten();

    public static Producten getProduct() {
        return mijnproducten;
    }

    public static void setProduct(Producten producten) {
        mijnproducten = producten;
    }

    private Producten() {
        loadAccessoiresFromJson("C:\\Users\\ielma\\IdeaProjects\\Ipasss\\src\\main\\java\\accessoires.json");
        loadProductsFromJson("C:\\Users\\ielma\\IdeaProjects\\Ipasss\\src\\main\\java\\fietsen.json");
    }

    private void loadAccessoiresFromJson(String bestand) {
        try {
            File file = new File(bestand);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jn = objectMapper.readTree(file);
            if (jn.isArray()) {
                for (JsonNode accessoirejson : jn) {
                    Accessoires accessoire = createAccessoire(accessoirejson);
                    accessoires.add(accessoire);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Accessoires createAccessoire(JsonNode jn) {
        String id = getStringValue(jn, "id");
        String naam = getStringValue(jn, "naam");
        String afbeelding = getStringValue(jn, "afbeelding");
        String prijs = getStringValue(jn, "prijs");
        String beschrijving = getStringValue(jn, "beschrijving");
        String voorraad = getStringValue(jn, "voorraad");
        return new Accessoires(id,naam, afbeelding, prijs, beschrijving, voorraad);
    }

    private void loadProductsFromJson(String bestand) {
        try {
            File file = new File(bestand);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jn = objectMapper.readTree(file);
            if (jn.isArray()) {
                for (JsonNode fietsen : jn) {
                    Fiets fiets = createFiets(fietsen);
                    alleProducten.add(fiets);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Fiets createFiets(JsonNode jn) {
        String id = getStringValue(jn, "id");
        String merk = getStringValue(jn, "merk");
        String type = getStringValue(jn, "type");
        String prijs = getStringValue(jn, "prijs");
        String gewicht = getStringValue(jn, "gewicht");
        String versnellingen = getStringValue(jn, "versnellingen");
        String remmen = getStringValue(jn, "remmen");
        String beschrijving = getStringValue(jn, "beschrijving");
        String afbeelding = getStringValue(jn, "afbeelding");
        String wielmaat = getStringValue(jn, "wielmaat");
        String framemaat = getStringValue(jn, "framemaat");
        String materiaalFrame = getStringValue(jn, "materiaalFrame");
        String voorvork = getStringValue(jn, "voorvork");
        String verlichting = getStringValue(jn, "verlichting");
        String bagagedrager = getStringValue(jn, "bagagedrager");
        String slot = getStringValue(jn, "slot");
        String link = getStringValue(jn, "link");
        return new Fiets(id, merk, type, prijs, gewicht, versnellingen, remmen, beschrijving, afbeelding,
                wielmaat, framemaat, materiaalFrame, voorvork, verlichting, bagagedrager, slot, link);
    }

    private String getStringValue(JsonNode jn, String key) {
        JsonNode propertyNode = jn.findValue(key);
        if (propertyNode != null && !propertyNode.isNull()) {
            return propertyNode.asText();
        }
        return "";
    }

    public List<Fiets> getAllProducts() {
        return alleProducten;
    }

    public Fiets getFietsById(String code) {
        for (Fiets fiets : alleProducten) {
            if (fiets.getId().equalsIgnoreCase(code)) {
                return fiets;
            }
        }
        return null;
    }
    public List<Fiets> getFietsByType(String type) {
        List<Fiets> fietsen = new ArrayList<>();
        if (type.equalsIgnoreCase("Overige")) {
            for (Fiets fiets : alleProducten) {
                if (!fiets.getType().equalsIgnoreCase("Stadsfiets")
                        && !fiets.getType().equalsIgnoreCase("Mountainbike")
                        && !fiets.getType().equalsIgnoreCase("Racefiets")
                        && !fiets.getType().equalsIgnoreCase("E-bike")) {
                    fietsen.add(fiets);
                }
            }
        } else {
            for (Fiets fiets : alleProducten) {
                if (fiets.getType().equalsIgnoreCase(type)) {
                    fietsen.add(fiets);
                }
            }
        }
        return fietsen;
    }
    public List<Accessoires> getAllAccessoires() {
        return accessoires;
    }
    public Accessoires getAccessoireById(String id) {
        for (Accessoires accessoire : accessoires) {
            if (accessoire.getId().equalsIgnoreCase(id)) {
                return accessoire;
            }
        }
        return null;
    }


}
