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

@Path("petrolstations")
@RequestScoped
public class PetrolStations {

    @Context
    private UriInfo context;
    public PetrolStations() {

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("createPositionIndex")
    public Response createPositionIndex() {
        Response.ResponseBuilder res = Response.status(Response.Status.OK);
        try {
            Database db = Database.getInstance();
            db.createPetrolStationPositionIndex();
        } catch (Exception e) {
            e.printStackTrace();
            res = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        }
        return res.build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("")
    public Response getPetrolStations(@DefaultValue("") @QueryParam("sortBy") String sortBy) throws Exception {
        Database db = Database.getInstance();
        Response.ResponseBuilder res = Response.status(Response.Status.OK);

        try {
            System.out.println("Request: GET /petrolStations");
            ArrayList<PetrolStation> allPetrolStations = db.getAllPetrolStations(sortBy);
            res.entity(db.getGson().toJson(allPetrolStations));
        } catch(Exception e) {
            res.status(Response.Status.NOT_FOUND);
            System.out.println(e.getMessage());
        }

        return res.build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{petrolstationid}")
    public Response getPetrolStation(@PathParam("petrolstationid") String id) throws Exception {
        Database db = Database.getInstance();
        Response.ResponseBuilder res = Response.status(Response.Status.OK);

        try {
            System.out.println("Request: GET /petrolstations/" + id);
            PetrolStation retPetrolStation = db.getPetrolStationById(new ObjectId(id));
            if (retPetrolStation == null) {
                res.status(Response.Status.NOT_FOUND);
            } else {
                res.entity(db.getGson().toJson(retPetrolStation));
            }
        } catch(Exception e) {
            res.status(Response.Status.INTERNAL_SERVER_ERROR);
            System.out.println(e.getMessage());
        }

        return res.build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response newPetrolStation(String strPetrolStation) throws Exception {
        Response.ResponseBuilder response = Response.status(Response.Status.CREATED);
        Database db = Database.getInstance();
        try {
            PetrolStation petrolStation = db.getGson().fromJson(strPetrolStation, PetrolStation.class);
            db.insertPetrolStation(petrolStation);
            response.entity(petrolStation.toString());
            System.out.println("======== webservice POST called");
        } catch(Exception e) {
            System.out.println("exception: " + e.getMessage());
            response.status(Response.Status.BAD_REQUEST);
            response.entity("[ERROR] " + e.getMessage());
        }
        return response.build();
    }

    @DELETE
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("{petrolstationid}")
    public Response deletePetrolStation(@PathParam("petrolstationid") String id) throws Exception {
        Response.ResponseBuilder response = Response.status(Response.Status.OK);
        Database db = Database.getInstance();

        try {
            PetrolStation retPetrolStation = db.deletePetrolStation(new ObjectId(id));
            response.entity(retPetrolStation.toString());
        } catch (Exception e) {
            response.status(Response.Status.BAD_REQUEST);
            response.entity("[ERROR] " + e.getMessage());
        }

        return response.build();
    }

    @PUT
    @Path("{petrolstationid}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response patchPetrolStation(String strPetrolStation, @PathParam("petrolstationid") String id) throws Exception {
        return updatePetrolStation(strPetrolStation, id);
    }

    private Response updatePetrolStation(String strPetrolStation, String petrolStationid) throws Exception {
        Database db = Database.getInstance();
        Response.ResponseBuilder response = Response.status(Response.Status.OK);
        try {
            PetrolStation newPetrolStation = db.getGson().fromJson(strPetrolStation, PetrolStation.class);
            PetrolStation petrolStation = db.getPetrolStationById(new ObjectId(petrolStationid));
            petrolStation.setStationName(newPetrolStation.getStationName());
            petrolStation.setPosition(newPetrolStation.getPosition());
            db.updatePetrolStation(petrolStation);
            response.entity(petrolStation.toString());
        } catch (Exception e) {
            response = Response.status(Response.Status.BAD_REQUEST);
            response.entity("[ERROR] " + e.getMessage());
        }

        return response.build();
    }
}
