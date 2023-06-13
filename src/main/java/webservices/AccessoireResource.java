package webservices;

import module.Accessoires;
import module.Producten;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.*;
import java.util.List;


@Path("/accessoires")

public class AccessoireResource {
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
}
