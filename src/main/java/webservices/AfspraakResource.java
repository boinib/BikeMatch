package webservices;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobStorageException;
import module.Afspraak;

import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;


@Path("/afspraak")

public class AfspraakResource {
    @GET
    @Produces("application/json")
    public String alleAfspraken() {
        return "f";
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

                blobClient.upload(BinaryData.fromStream(new ByteArrayInputStream(bijgewerkteJson.getBytes(StandardCharsets.UTF_8)), (long) existingData.length), true);

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

}
