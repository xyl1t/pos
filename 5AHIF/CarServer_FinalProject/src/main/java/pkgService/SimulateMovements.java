package pkgService;

import com.mongodb.client.model.geojson.Point;
import org.bson.types.ObjectId;
import pkgData.Car;
import pkgData.Database;
import pkgData.GeoPosition;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static pkgMisc.Locations.VILLACH_LONG;
import static pkgMisc.Locations.VILLACH_LAT;

@Path("simulateMovements")
@RequestScoped
public class SimulateMovements {

    @Context
    private UriInfo context;

    public SimulateMovements() {

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("")
    public Response simulateMovements(@DefaultValue("5") @QueryParam("numMovements") String numMovementsStr, @DefaultValue("") @QueryParam("carId") String carIdStr) {
        Response.ResponseBuilder res = Response.status(Response.Status.OK);

        int numMovements = Integer.parseInt(numMovementsStr);
        ObjectId carId = null;
        System.out.println("################################ GET SIMULATE MOVEMENTS");
        System.out.println("carIdStr: " + carIdStr + " isValid: " + ObjectId.isValid(carIdStr));
        if (ObjectId.isValid(carIdStr)) {
            carId = new ObjectId(carIdStr);
        }

        try {
            Database db = Database.getInstance();

            if (carId != null) {
                Car car = db.getCarById(carId);
                moveCar(numMovements, car);
                db.updateCar(car);
            } else {
                ArrayList<Car> cars = db.getAllCars();
                for (Car car : cars) {
                    moveCar(numMovements, car);
                    db.updateCar(car);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        }

        return res.build();
    }

    private void moveCar(int numMovements, Car car) {
        for (int i = 0; i < numMovements; i++) {
            GeoPosition lastPoint = car.getLastPosition();
            car.getPositions().add(new GeoPosition(
                    lastPoint.getLocation().getPosition().getValues().get(0) + (Math.random() * 2 - 1)/50,
                    lastPoint.getLocation().getPosition().getValues().get(1) + (Math.random() * 2 - 1)/50,
                    LocalDateTime.now().plusMinutes(i)));
        }
    }
}
