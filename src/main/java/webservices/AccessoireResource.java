package webservices;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;
import module.Accessoires;
import module.Producten;

import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;


@Path("/accessoires")

public class AccessoireResource {

    private static final String STORAGE_CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=ipassopslag;AccountKey=H5hFbFzP/AtKqOvv9km+SHoiSKp1cFaDHfWaCbJYnYxKnqFnu8VnUsPKMrlm8kdmaVmi2HNP0y28+AStDNL20g==;EndpointSuffix=core.windows.net";
    private static final String CONTAINER_NAME = "blobipass";
    private static final String BLOB_NAME = "accessoire.json";
    @GET
    @Produces("application/json")
    public String allAccessoires() {
        Producten producten = Producten.getProduct();
        List<Accessoires> countries = producten.getAllAccessoires();
        return alleAccessoires(countries).toString();
    }
    @GET
    @Path("/{code}")
    @Produces("application/json")
    public String accessoiresByID(@PathParam("code") String accId) {
        Producten producten = Producten.getProduct();
        Accessoires acc = producten.getAccessoireById(accId);
        return accessoireById(acc).toString();
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
    @POST
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
                        .add("voorrraad", accessoireVoorraad)
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
                                .add("voorrraad", accessoireVoorraad)
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
}
