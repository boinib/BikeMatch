package webservices;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;
import model.Accessoires;
import model.Producten;

import javax.annotation.security.RolesAllowed;
import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;




@Path("/accessoires")

public class AccessoireResource {

    private static final String STORAGE_CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=ipassopslag;AccountKey=H5hFbFzP/AtKqOvv9km+SHoiSKp1cFaDHfWaCbJYnYxKnqFnu8VnUsPKMrlm8kdmaVmi2HNP0y28+AStDNL20g==;EndpointSuffix=core.windows.net";
    private static final String CONTAINER_NAME = "blobipass";
    private static final String BLOB_NAME = "accessoire.json";

    @GET
    @Produces("application/json")
    public String allAccessoires() {
        Producten producten = Producten.getProduct();
        List<Accessoires> accessoires = producten.getAllAccessoires();
        return alleAccessoires(accessoires).toString();
    }

    @GET
    @Path("/{code}")
    @Produces("application/json")
    public String accessoiresByID(@PathParam("code") String accId) {
        Producten producten = Producten.getProduct();
        Accessoires acc = producten.getAccessoireById(accId);
        return accessoireById(acc).toString();
    }

    private JsonArray alleAccessoires(List<Accessoires> accessoires) {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Accessoires accessoire : accessoires) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("id", accessoire.getId())
                    .add("naam", accessoire.getNaam())
                    .add("afbeelding", accessoire.getAfbeelding())
                    .add("prijs", accessoire.getPrijs())
                    .add("beschrijving", accessoire.getBeschrijving())
                    .add("voorraad", accessoire.getVoorraad());

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

    @POST
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAccessoire(String accessoireData) {
        try {
            JsonObject jsonData = Json.createReader(new StringReader(accessoireData)).readObject();
            String accessoireNaam = jsonData.getString("naam");
            String accessoireAfbeelding = jsonData.getString("afbeelding");
            String accessoirePrijs = jsonData.getString("prijs");
            String accessoireBeschrijving = jsonData.getString("beschrijving");
            String accessoireVoorraad = jsonData.getString("voorraad");

            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(STORAGE_CONNECTION_STRING).buildClient();
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
            BlobClient blobClient = containerClient.getBlobClient(BLOB_NAME);

            if (blobClient.exists()) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                blobClient.download(outputStream);
                byte[] existingData = outputStream.toByteArray();
                outputStream.close();

                JsonReader jsonReader = Json.createReader(new ByteArrayInputStream(existingData));
                JsonArray bestaandeAccessoires = jsonReader.readArray();
                jsonReader.close();

                String accessoireId = String.valueOf(bestaandeAccessoires.size() + 1);

                JsonObject nieuweAccessoire = Json.createObjectBuilder()
                        .add("id", accessoireId)
                        .add("naam", accessoireNaam)
                        .add("afbeelding", accessoireAfbeelding)
                        .add("prijs", accessoirePrijs)
                        .add("beschrijving", accessoireBeschrijving)
                        .add("voorraad", accessoireVoorraad)
                        .build();

                JsonArrayBuilder AccessoireBuilder = Json.createArrayBuilder();
                AccessoireBuilder.add(nieuweAccessoire);
                for (JsonValue bestaandeAccessoire : bestaandeAccessoires) {
                    AccessoireBuilder.add(bestaandeAccessoire);
                }

                JsonArray updatedAccessoires = AccessoireBuilder.build();

                ByteArrayOutputStream updatedStream = new ByteArrayOutputStream();
                JsonWriter jsonWriter = Json.createWriter(updatedStream);
                jsonWriter.writeArray(updatedAccessoires);
                jsonWriter.close();

                byte[] updatedData = updatedStream.toByteArray();
                updatedStream.close();

                blobClient.upload(BinaryData.fromBytes(updatedData), true);
            } else {
                String accessoireId = "1";

                JsonArray accessoires = Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("id", accessoireId)
                                .add("naam", accessoireNaam)
                                .add("afbeelding", accessoireAfbeelding)
                                .add("prijs", accessoirePrijs)
                                .add("beschrijving", accessoireBeschrijving)
                                .add("voorraad", accessoireVoorraad)
                                .build())
                        .build();

                ByteArrayOutputStream accessoireStream = new ByteArrayOutputStream();
                JsonWriter jsonWriter = Json.createWriter(accessoireStream);
                jsonWriter.writeArray(accessoires);
                jsonWriter.close();

                byte[] accessoiresData = accessoireStream.toByteArray();
                accessoireStream.close();

                blobClient.upload(BinaryData.fromBytes(accessoiresData));
            }

        } catch (JsonException e) {
            e.printStackTrace();
            return Response.status(400).build();
        } catch (BlobStorageException e) {
            e.printStackTrace();
            return Response.status(500).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST")
                .header("Access-Control-Allow-Headers", "Content-Type")
                .build();
    }

    @PUT
    @RolesAllowed("admin")
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAccessoire(@PathParam("id") String id, String accessoireData) {
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
                JsonArray existingAccessoires = jsonReader.readArray();
                jsonReader.close();

                JsonArrayBuilder updatedAccessoiresBuilder = Json.createArrayBuilder();

                for (JsonValue existingAccessoireValue : existingAccessoires) {
                    JsonObject accessoireObject = (JsonObject) existingAccessoireValue;
                    String accessoireId = accessoireObject.getString("id");
                    if (accessoireId.equals(id)) {
                        JsonObject updatedAccessoire = Json.createReader(new StringReader(accessoireData)).readObject();
                        JsonObjectBuilder geupdateAccessoireBuilder = Json.createObjectBuilder();
                        updatedAccessoire.forEach((key, value) -> geupdateAccessoireBuilder.add(key, value));
                        geupdateAccessoireBuilder.add("id", accessoireId);
                        JsonObject geupdateAccessoireMetId = geupdateAccessoireBuilder.build();
                        updatedAccessoiresBuilder.add(geupdateAccessoireMetId);
                    } else {
                        updatedAccessoiresBuilder.add(accessoireObject);
                    }
                }

                JsonArray updatedAccessoires = updatedAccessoiresBuilder.build();
                String updatedJson = updatedAccessoires.toString();

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
    public Response deleteAccessoire(@PathParam("id") String id) {
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
                JsonArray bestaandeAccessoires = jsonReader.readArray();
                jsonReader.close();

                JsonArrayBuilder updatedAccessoiresBuilder = Json.createArrayBuilder();

                for (JsonValue bestaandeAccessoire : bestaandeAccessoires) {
                    JsonObject accessoireObject = (JsonObject) bestaandeAccessoire;
                    String accessoireId = accessoireObject.getString("id");
                    if (!accessoireId.equals(id)) {
                        updatedAccessoiresBuilder.add(accessoireObject);
                    }
                }

                JsonArray updatedAccessoires = updatedAccessoiresBuilder.build();
                String updatedJson = updatedAccessoires.toString();

                blobClient.upload(BinaryData.fromString(updatedJson), true);

                return Response.ok().header("Access-Control-Allow-Origin", "*")
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