package webservices;



import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import module.*;

import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.List;


@Path("/fiets")

public class Fietsresource {

    // TODO: Realiseer hier de gevraagde webservices!

    @GET
    @Produces("application/json")
    public String alleFietsen() {
        Producten producten = Producten.getProduct();
        System.out.println("a");
        List<Fiets> countries = producten.getAllProducts();
        return allcountries(countries).toString();
    }

    @GET
    @Path("/accessoires")
    @Produces("application/json")
    public String allAccessoires() {
        Producten producten = Producten.getProduct();
        List<Accessoires> countries = producten.getAllAccessoires();
        return alleAccessoires(countries).toString();
    }

    @GET
    @Path("/largestpopulations")
    @Produces("application/json")
    public String goedkoopsteFietsen() {
        return null;
    }

    @GET
    @Path("/{code}")
    @Produces("application/json")
    public String fietsByID(@PathParam("code") String fietsId) {
        Producten producten = Producten.getProduct();
        Fiets fiets = producten.getFietsById(fietsId);
        return fietsById(fiets).toString();
    }
    @GET
    @Path("accessoires/{code}")
    @Produces("application/json")
    public String accessoiresByID(@PathParam("code") String accId) {
        Producten producten = Producten.getProduct();
        Accessoires acc = producten.getAccessoireById(accId);
        return accessoireById(acc).toString();
    }
    @GET
    @Path("type/{type}")
    @Produces("application/json")
    public String fietsByType(@PathParam("type") String fietsType) {
        Producten producten = Producten.getProduct();
        List<Fiets> fietsen = producten.getFietsByType(fietsType);
        return allcountries(fietsen).toString();
    }

    private static final String STORAGE_CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=ipassopslag;AccountKey=H5hFbFzP/AtKqOvv9km+SHoiSKp1cFaDHfWaCbJYnYxKnqFnu8VnUsPKMrlm8kdmaVmi2HNP0y28+AStDNL20g==;EndpointSuffix=core.windows.net";
    private static final String CONTAINER_NAME = "blobipass";
    private static final String BLOB_NAME = "afspraak.json";

    @POST
    @Path("/nieuweAfspraak")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAfspraak(String afspraakData) {
        try {
            JsonObject jsonData = Json.createReader(new StringReader(afspraakData)).readObject();
            String afspraakNaam = jsonData.getString("name");
            String afspraakEmail = jsonData.getString("email");
            String afspraakTelefoon = jsonData.getString("phone");
            String datum = jsonData.getString("date");
            String tijd = jsonData.getString("time");
            String opmerking = jsonData.getString("notes");

            Afspraak afspraak = Afspraak.addAfspraak(afspraakNaam, afspraakEmail, afspraakTelefoon, datum, tijd, opmerking);
            System.out.println(afspraak);

            if (afspraak == null) {
                var error = new AbstractMap.SimpleEntry<>("error", "customer already exists");
                return Response.status(409).entity(error).build();
            }

            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(STORAGE_CONNECTION_STRING).buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
            BlobClient blobClient = containerClient.getBlobClient(BLOB_NAME);

            if (blobClient.exists()) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                blobClient.download(outputStream);
                byte[] existingData = outputStream.toByteArray();

                JsonReader jsonReader = Json.createReader(new ByteArrayInputStream(existingData));
                JsonArray bestaandeAfspraken = jsonReader.readArray();
                System.out.println(bestaandeAfspraken);
                jsonReader.close();

                JsonObject nieuweAfspraak = Json.createObjectBuilder()
                        .add("naam", afspraakNaam)
                        .add("email", afspraakEmail)
                        .add("telefoon", afspraakTelefoon)
                        .add("datum", datum)
                        .add("tijd", tijd)
                        .add("opmerking", opmerking)
                        .build();

                JsonArrayBuilder afsprakenBuilder = Json.createArrayBuilder();
                afsprakenBuilder.add(nieuweAfspraak);
                for (JsonValue bestaandeAfspraak : bestaandeAfspraken) {
                    afsprakenBuilder.add(bestaandeAfspraak);
                }

                JsonArray bijgewerkteAfspraken = afsprakenBuilder.build();

                String bijgewerkteJson = bijgewerkteAfspraken.toString();
                System.out.println(bijgewerkteJson);

                blobClient.upload(BinaryData.fromStream(new ByteArrayInputStream(bijgewerkteJson.getBytes(StandardCharsets.UTF_8))));

            } else {
                JsonArray afspraken = Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("naam", afspraakNaam)
                                .add("email", afspraakEmail)
                                .add("telefoon", afspraakTelefoon)
                                .add("datum", datum)
                                .add("tijd", tijd)
                                .add("opmerking", opmerking)
                                .build())
                        .build();

                String afsprakenJson = afspraken.toString();

                blobClient.upload(BinaryData.fromStream(new ByteArrayInputStream(afsprakenJson.getBytes(StandardCharsets.UTF_8))));

            }

        } catch (JsonException e) {
            e.printStackTrace();
            return Response.status(400).build();
        } catch (BlobStorageException e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST")
                .header("Access-Control-Allow-Headers", "Content-Type")
                .build();
    }






    private JsonArray allcountries(List<Fiets> countries) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Fiets country : countries) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("id", country.getId())
                    .add("merk", country.getMerk())
                    .add("type", country.getType())
                    .add("prijs", country.getPrijs())
                    .add("gewicht", country.getGewicht())
                    .add("versnellingen", country.getVersnellingen())
                    .add("remmen", country.getRemmen())
                    .add("beschrijving", country.getBeschrijving())
                    .add("afbeelding", country.getAfbeelding())
                    .add("wielmaat", country.getWielmaat())
                    .add("framemaat", country.getFramemaat())
                    .add("materiaalFrame", country.getMateriaalframe())
                    .add("voorvork", country.getVoorvork())
                    .add("verlichting", country.getVerlichting())
                    .add("bagagedrager", country.getBagagedrager())
                    .add("slot", country.getSlot())
                    .add("link", country.getLink());
            jsonArrayBuilder.add(job.build());
        }
        return jsonArrayBuilder.build();
    }


    private String fietsById(Fiets fiets) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", fiets.getId())
                .add("merk", fiets.getMerk())
                .add("type", fiets.getType())
                .add("prijs", fiets.getPrijs())
                .add("gewicht", fiets.getGewicht())
                .add("versnellingen", fiets.getVersnellingen())
                .add("remmen", fiets.getRemmen())
                .add("beschrijving", fiets.getBeschrijving())
                .add("afbeelding", fiets.getAfbeelding())
                .add("wielmaat", fiets.getWielmaat())
                .add("framemaat", fiets.getFramemaat())
                .add("materiaalFrame", fiets.getMateriaalframe())
                .add("voorvork", fiets.getVoorvork())
                .add("verlichting", fiets.getVerlichting())
                .add("bagagedrager", fiets.getBagagedrager())
                .add("slot", fiets.getSlot())
                .add("link", fiets.getLink());

        return job.build().toString();
    }

    private JsonArray alleAccessoires(List<Accessoires> countries) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Accessoires country : countries) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("id", country.getId())
                    .add("naam", country.getNaam())
                    .add("afbeelding", country.getAfbeelding())
                    .add("prijs", country.getPrijs())
                    .add("beschrijving", country.getBeschrijving())
                    .add("voorraad", country.getVoorraad());

            jsonArrayBuilder.add(job.build());
        }
        return jsonArrayBuilder.build();
    }

    private String accessoireById(Accessoires accessoires) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", accessoires.getId())
                .add("naam", accessoires.getNaam())
                .add("afbeelding", accessoires.getAfbeelding())
                .add("prijs", accessoires.getPrijs())
                .add("beschrijving", accessoires.getBeschrijving())
                .add("voorraad", accessoires.getVoorraad());

        return job.build().toString();
    }

}




