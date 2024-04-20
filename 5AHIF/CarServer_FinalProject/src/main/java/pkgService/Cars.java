package pkgService;


import com.google.gson.Gson;
import org.bson.types.ObjectId;
import pkgData.Car;
import pkgData.Database;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;

@Path("cars")
@RequestScoped
public class Cars {

    @Context
    private UriInfo context;
    public Cars() {

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("")
    public Response getCars(@DefaultValue("") @QueryParam("type") String type) throws Exception {
        Database db = Database.getInstance();
        Response.ResponseBuilder res = Response.status(Response.Status.OK);

        ArrayList<Car> cars;

        switch (type) {
            case "names":
                cars = db.getAllCarsName();
                break;
            default:
                cars = db.getAllCars();
                break;
        }

        try {
            System.out.println("Request: GET /cars");
            res.entity(db.getGson().toJson(cars));
        } catch(Exception e) {
            res.status(Response.Status.NOT_FOUND);
            System.out.println(e.getMessage());
        }

        return res.build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{carid}")
    public Response getCar(@PathParam("carid") String id, @DefaultValue("false") @QueryParam("listPositions") String listPositions) throws Exception {
        Database db = Database.getInstance();
        Response.ResponseBuilder res = Response.status(Response.Status.OK);

        try {
            System.out.println("Request: GET /cars/" + id);

            Car retCar;

            if (listPositions.equals("true")) {
                retCar = db.getCarWithPositions(new ObjectId(id));
            } else {
                retCar = db.getCarById(new ObjectId(id));
            }

            if (retCar == null) {
                res.status(Response.Status.NOT_FOUND);
            } else {
                res.entity(db.getGson().toJson(retCar));
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
    public Response newCar(String strCar) throws Exception {
        Response.ResponseBuilder response = Response.status(Response.Status.CREATED);
        Database db = Database.getInstance();
        try {
            Car car = db.getGson().fromJson(strCar, Car.class);
            db.insertCar(car);
            response.entity(car.toString());
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
    @Path("{carid}")
    public Response deleteCar(@PathParam("carid") String id) throws Exception {
        Response.ResponseBuilder response = Response.status(Response.Status.OK);
        Database db = Database.getInstance();

        try {
            Car retCar = db.deleteCar(new ObjectId(id));
            response.entity(retCar.toString());
        } catch (Exception e) {
            response.status(Response.Status.BAD_REQUEST);
            response.entity("[ERROR] " + e.getMessage());
        }

        return response.build();
    }

    @PUT
    @Path("{carid}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response patchCar(String strCar, @PathParam("carid") String id) throws Exception {
        return updateCar(strCar, id);
    }

    private Response updateCar(String strCar, String carid) throws Exception {
        Database db = Database.getInstance();
        Response.ResponseBuilder response = Response.status(Response.Status.OK);
        try {
            Car newCar = db.getGson().fromJson(strCar, Car.class);
            Car car = db.getCarById(new ObjectId(carid));
            car.setCarName(newCar.getCarName());
            car.setPositions(newCar.getPositions());
            db.updateCar(car);
            response.entity(car.toString());
        } catch (Exception e) {
            response = Response.status(Response.Status.BAD_REQUEST);
            response.entity("[ERROR] " + e.getMessage());
        }

        return response.build();
    }
}
