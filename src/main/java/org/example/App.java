package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import module.Fiets;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String filePath = "fietsen.json";

        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

            StringBuilder jsonContent = new StringBuilder();
            while (scanner.hasNextLine()) {
                jsonContent.append(scanner.nextLine());
            }

            scanner.close();

            String jsonString = jsonContent.toString();

            // Verwerk de JSON-string
            processJson(jsonString);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void processJson(String jsonString) {
        try {
            // Maak een ObjectMapper-object aan
            ObjectMapper objectMapper = new ObjectMapper();

            // Converteer de JSON-string naar een JsonNode-object
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            if (jsonNode.isArray()) {
                // Itereer door de JSON-array
                for (JsonNode bikeNode : jsonNode) {
                    // Maak een nieuw Fiets-object met de vereiste parameters
                    Fiets fiets = createFiets(bikeNode);

                    // Druk alle gegevens van het Fiets-object af
                    printFietsData(fiets);
                    System.out.println("----------------------");
                }
            }
            // Maak een nieuw Fiets-object met de vereiste parameters
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Fiets createFiets(JsonNode jsonNode) {
        String id = getStringValue(jsonNode, "id");
        String merk = getStringValue(jsonNode, "merk");
        String type = getStringValue(jsonNode, "type");
        String prijs = getStringValue(jsonNode, "prijs");
        String gewicht = getStringValue(jsonNode, "gewicht");
        String versnellingen = getStringValue(jsonNode, "versnellingen");
        String remmen = getStringValue(jsonNode, "remmen");
        String beschrijving = getStringValue(jsonNode, "beschrijving");
        String afbeelding = getStringValue(jsonNode, "afbeelding");
        String wielmaat = getStringValue(jsonNode, "wielmaat");
        String framemaat = getStringValue(jsonNode, "framemaat");
        String materiaalFrame = getStringValue(jsonNode, "materiaalFrame");
        String voorvork = getStringValue(jsonNode, "voorvork");
        String verlichting = getStringValue(jsonNode, "verlichting");
        String bagagedrager = getStringValue(jsonNode, "bagagedrager");
        String slot = getStringValue(jsonNode, "slot");
        String link = getStringValue(jsonNode, "link");

        // Retourneer een nieuw Fiets-object met de verzamelde gegevens
        return new Fiets(id, merk, type, prijs, gewicht, versnellingen, remmen, beschrijving, afbeelding,
                wielmaat, framemaat, materiaalFrame, voorvork, verlichting, bagagedrager, slot, link);
    }

    private static String getStringValue(JsonNode jsonNode, String propertyName) {
        JsonNode propertyNode = jsonNode.findValue(propertyName);
        if (propertyNode != null && !propertyNode.isNull()) {
            return propertyNode.asText();
        }
        return "";
    }

    private static void printFietsData(Fiets fiets) {
        // Druk alle gegevens van het Fiets-object af
        System.out.println("ID: " + fiets.getId());
        System.out.println("Merk: " + fiets.getMerk());
        System.out.println("Type: " + fiets.getType());
        System.out.println("Prijs: " + fiets.getPrijs());
        System.out.println("Gewicht: " + fiets.getGewicht());
        System.out.println("Versnellingen: " + fiets.getVersnellingen());
        System.out.println("Remmen: " + fiets.getRemmen());
        System.out.println("Beschrijving: " + fiets.getBeschrijving());
        System.out.println("Afbeelding: " + fiets.getAfbeelding());
        System.out.println("Wielmaat: " + fiets.getWielmaat());
        System.out.println("Framemaat: " + fiets.getFramemaat());
        System.out.println("Materiaal frame: " + fiets.getMateriaalframe());
        System.out.println("Voorvork: " + fiets.getVoorvork());
        System.out.println("Verlichting: " + fiets.getVerlichting());
        System.out.println("Bagagedrager: " + fiets.getBagagedrager());
        System.out.println("Slot: " + fiets.getSlot());
        System.out.println("Link: " + fiets.getLink());
    }
}
