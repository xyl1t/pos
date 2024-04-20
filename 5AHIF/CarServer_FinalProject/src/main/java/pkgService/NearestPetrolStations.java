package pkgService;

import org.bson.types.ObjectId;
import pkgData.Database;
import pkgData.PetrolStation;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;

@Path("nearestPetrolStations")
@RequestScoped
public class NearestPetrolStations {

    @Context
    private UriInfo context;

    public NearestPetrolStations() {

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("")
    public Response nearestPetrolStations(@QueryParam("carId") String carIdStr, @QueryParam("maxDistance") String maxDistanceStr) {
        Response.ResponseBuilder res = Response.status(Response.Status.OK);
        System.out.println("################################ GET NEAREST");

        double numMaxDistance = Double.parseDouble(maxDistanceStr);
        if (!ObjectId.isValid(carIdStr)) {
            res = Response.status(Response.Status.BAD_REQUEST);
            res.entity("Invalid carId");
            return res.build();
        }
        ObjectId carId = new ObjectId(carIdStr);

        try {
            Database db = Database.getInstance();

            ArrayList<PetrolStation> petrolStations = db.getNearestPetrolStations(carId, numMaxDistance);

            res.entity(db.getGson().toJson(petrolStations));
        } catch (Exception e) {
            e.printStackTrace();
            res = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        }

        return res.build();
    }
}
