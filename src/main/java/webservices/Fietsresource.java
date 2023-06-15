package webservices;

import module.*;
import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
        return allcountries(fietsen).toString();
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
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response voegFietsToe(Fiets nieuweFiets) {
        Producten producten = Producten.getProduct();
        String fietsId = producten.addFiets(nieuweFiets);

        JsonObject responseJson = Json.createObjectBuilder()
                .add("id", fietsId)
                .build();

        return Response.status(Response.Status.CREATED)
                .entity(responseJson)
                .build();
    }
}




