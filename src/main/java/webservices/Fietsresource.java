package webservices;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;
import model.Fiets;
import model.Producten;

import javax.annotation.security.RolesAllowed;
import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;


@Path("/fiets")

public class Fietsresource {
    private static final String STORAGE_CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=ipassopslag;AccountKey=H5hFbFzP/AtKqOvv9km+SHoiSKp1cFaDHfWaCbJYnYxKnqFnu8VnUsPKMrlm8kdmaVmi2HNP0y28+AStDNL20g==;EndpointSuffix=core.windows.net";
    private static final String CONTAINER_NAME = "blobipass";
    private static final String BLOB_NAME = "fiets.json";


    @GET
    @Produces("application/json")
    public String getFietsen() {
        Producten producten = Producten.getProduct();
        List<Fiets> fietsen = producten.getAllProducts();
        fietsen.clear();
        producten.loadProductsFromBlob();
        return alleFietsen(fietsen).toString();
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
    @Path("type/{type}")
    @Produces("application/json")
    public String fietsByType(@PathParam("type") String fietsType) {
        Producten producten = Producten.getProduct();
        List<Fiets> fietsen = producten.getFietsByType(fietsType);
        return alleFietsen(fietsen).toString();
    }

    private JsonArray alleFietsen(List<Fiets> fietsen) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Fiets fiets : fietsen) {
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
    @POST
    @RolesAllowed({"admin","winkeleigenaar"})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addFiets(String fietsData) {
        try {
            JsonObject jsonData = Json.createReader(new StringReader(fietsData)).readObject();
            String fietsMerk = jsonData.getString("merk");
            String fietsType = jsonData.getString("type");
            String fietsPrijs = jsonData.getString("prijs");
            String fietsGewicht = jsonData.getString("gewicht");
            String fietsVersnellingen = jsonData.getString("versnellingen");
            String fietsRemmen = jsonData.getString("remmen");
            String fietsBeschrijving = jsonData.getString("beschrijving");
            String fietsAfbeelding = jsonData.getString("afbeelding");
            String fietsWielmaat = jsonData.getString("wielmaat");
            String fietsFramemaat = jsonData.getString("framemaat");
            String fietsMateriaalFrame = jsonData.getString("materiaalFrame");
            String fietsVoorvork = jsonData.getString("voorvork");
            String fietsVerlichting = jsonData.getString("verlichting");
            String fietsBagagedrager = jsonData.getString("bagagedrager");
            String fietsSlot = jsonData.getString("slot");
            String fietsLink = jsonData.getString("link");

            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(STORAGE_CONNECTION_STRING).buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
            BlobClient blobClient = containerClient.getBlobClient(BLOB_NAME);

            JsonReader jsonReader = Json.createReader(blobClient.openInputStream());
            JsonArray bestaandeFietsen = jsonReader.readArray();
            jsonReader.close();

            String fietsId;
            if (bestaandeFietsen.isEmpty()) {
                fietsId = "1";
            } else {
                fietsId = String.valueOf(bestaandeFietsen.size() + 1);
            }

            JsonObject nieuweFiets = Json.createObjectBuilder()
                    .add("id",fietsId)
                    .add("merk", fietsMerk)
                    .add("type", fietsType)
                    .add("prijs", fietsPrijs)
                    .add("gewicht", fietsGewicht)
                    .add("versnellingen", fietsVersnellingen)
                    .add("remmen", fietsRemmen)
                    .add("beschrijving", fietsBeschrijving)
                    .add("afbeelding", fietsAfbeelding)
                    .add("wielmaat", fietsWielmaat)
                    .add("framemaat", fietsFramemaat)
                    .add("materiaalFrame", fietsMateriaalFrame)
                    .add("voorvork", fietsVoorvork)
                    .add("verlichting", fietsVerlichting)
                    .add("bagagedrager", fietsBagagedrager)
                    .add("slot", fietsSlot)
                    .add("link", fietsLink)
                    .build();

            JsonArrayBuilder fietsenBuilder = Json.createArrayBuilder();
            fietsenBuilder.add(nieuweFiets);
            for (JsonValue bestaandeFiets : bestaandeFietsen) {
                fietsenBuilder.add(bestaandeFiets);
            }

            JsonArray updatedFietsen = fietsenBuilder.build();

            ByteArrayOutputStream updatedStream = new ByteArrayOutputStream();
            JsonWriter jsonWriter = Json.createWriter(updatedStream);
            jsonWriter.writeArray(updatedFietsen);
            jsonWriter.close();

            byte[] updatedData = updatedStream.toByteArray();
            updatedStream.close();

            blobClient.upload(BinaryData.fromBytes(updatedData), true);

            return Response.ok(updatedFietsen.toString())
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "POST")
                    .header("Access-Control-Allow-Headers", "Content-Type")
                    .build();
        } catch (JsonException e) {
            e.printStackTrace();
            return Response.status(400).build();
        } catch (BlobStorageException e) {
            e.printStackTrace();
            return Response.status(500).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @PUT
    @RolesAllowed({"admin","winkeleigenaar"})
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateFiets(@PathParam("id") String id1, String fietsData) {
        System.out.println(id1);
        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(STORAGE_CONNECTION_STRING)
                    .buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
            BlobClient blobClient = containerClient.getBlobClient(BLOB_NAME);

            if (blobClient.exists()) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                blobClient.download(outputStream);
                byte[] existingData = outputStream.toByteArray();

                JsonReaderFactory factory = Json.createReaderFactory(null);
                JsonReader jsonReader = factory.createReader(new ByteArrayInputStream(existingData));
                JsonArray bestaandeFietsen = jsonReader.readArray();
                jsonReader.close();

                JsonArrayBuilder updatedFietsBuilder = Json.createArrayBuilder();

                for (JsonValue bestaandeFietsValue : bestaandeFietsen) {
                    JsonObject fietsObject = (JsonObject) bestaandeFietsValue;
                    String fietsId = fietsObject.getString("id");
                    if (fietsId.equals(id1)) {
                        JsonObject updatedFiets = Json.createReader(new StringReader(fietsData)).readObject();
                        JsonObjectBuilder geupdateFietsBuilder = Json.createObjectBuilder();
                        updatedFiets.forEach((key, value) -> geupdateFietsBuilder.add(key, value));
                        geupdateFietsBuilder.add("id", fietsId);
                        JsonObject geupdateFietsMetId = geupdateFietsBuilder.build();
                        updatedFietsBuilder.add(geupdateFietsMetId);
                    } else {
                        updatedFietsBuilder.add(fietsObject);
                    }
                }

                JsonArray updatedFiets = updatedFietsBuilder.build();
                String updatedJson = updatedFiets.toString();

                blobClient.upload(BinaryData.fromString(updatedJson), true);

                return Response.ok().header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "PUT")
                        .header("Access-Control-Allow-Headers", "Content-Type")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (BlobStorageException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DELETE
    @RolesAllowed("admin")
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFiets(@PathParam("id") String id) {
        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(STORAGE_CONNECTION_STRING)
                    .buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
            BlobClient blobClient = containerClient.getBlobClient(BLOB_NAME);

            if (blobClient.exists()) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                blobClient.download(outputStream);
                byte[] existingData = outputStream.toByteArray();

                JsonReaderFactory factory = Json.createReaderFactory(null);
                JsonReader jsonReader = factory.createReader(new ByteArrayInputStream(existingData));
                JsonArray bestaandeFietsen = jsonReader.readArray();
                jsonReader.close();

                JsonArrayBuilder updateFietsBuilder = Json.createArrayBuilder();

                for (JsonValue bestaandeFiets : bestaandeFietsen) {
                    JsonObject fietsObject = (JsonObject) bestaandeFiets;
                    String fietsId = fietsObject.getString("id");
                    if (fietsId.equals(id)) {
                        Producten producten = Producten.getProduct();
                        producten.removeFiets(fietsId);
                    } else {
                        updateFietsBuilder.add(fietsObject);
                    }
                }
                JsonArray updatedFietsen = updateFietsBuilder.build();
                String updatedJson = updatedFietsen.toString();


                blobClient.upload(BinaryData.fromString(updatedJson), true);

                Producten producten = Producten.getProduct();
                List<Fiets> fietsen = producten.getAllProducts();

                return Response.ok(alleFietsen(fietsen).toString())
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "DELETE")
                        .header("Access-Control-Allow-Headers", "Content-Type")
                        .build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (BlobStorageException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }



}




