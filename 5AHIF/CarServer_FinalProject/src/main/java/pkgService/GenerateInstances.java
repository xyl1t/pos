package pkgService;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import pkgData.Car;
import pkgData.Database;
import pkgData.GeoPosition;
import pkgData.PetrolStation;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static pkgMisc.Locations.*;

@Path("generateInstances")
@RequestScoped
public class GenerateInstances {

    @Context
    private UriInfo context;

    public GenerateInstances() {

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("")
    public Response generateInstances(@QueryParam("numCars") String numCarsStr, @QueryParam("numPetrolStations") String numPetrolStationsStr) {
        Response.ResponseBuilder res = Response.status(Response.Status.OK);

        int numCars = 10;
        int numPetrolStations = 3;

        try {
            if (numCarsStr != null && !numCarsStr.isEmpty()) {
                numCars = Integer.parseInt(numCarsStr);
            }
            if (numPetrolStationsStr != null && !numPetrolStationsStr.isEmpty()) {
                numPetrolStations = Integer.parseInt(numPetrolStationsStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = Response.status(Response.Status.BAD_REQUEST);
            return res.build();
        }

        try {
            Database db = Database.getInstance();

            for (int i = 0; i < numCars; i++) {
                db.insertCar(new Car(
                        "Villach Car" + i,
                        new ArrayList<>(Arrays.asList(new GeoPosition(VILLACH_LONG + (Math.random() * 2 - 1) / 50, VILLACH_LAT + (Math.random() * 2 - 1) / 50, LocalDateTime.now())))
                ));
            }
            for (int i = 0; i < numCars; i++) {
                db.insertCar(new Car(
                        "Klagenfurt Car" + i,
                        new ArrayList<>(Arrays.asList(new GeoPosition(KLAGENFURT_LONG + (Math.random() * 2 - 1) / 50, KLAGENFURT_LAT + (Math.random() * 2 - 1) / 50, LocalDateTime.now())))
                ));
            }
            for (int i = 0; i < numCars; i++) {
                db.insertCar(new Car(
                        "Wien Car" + i,
                        new ArrayList<>(Arrays.asList(new GeoPosition(WIEN_LONG + (Math.random() * 2 - 1) / 50, WIEN_LAT + (Math.random() * 2 - 1) / 50, LocalDateTime.now())))
                ));
            }

            for (int i = 0; i < numPetrolStations; i++) {
                db.insertPetrolStation(new PetrolStation(
                        "Villach Petrol Station" + i,
                        new Point(new Position(VILLACH_LONG + (Math.random() * 2 - 1) / 50 * 3, VILLACH_LAT + (Math.random() * 2 - 1) / 50 * 3))
                ));
            }
            for (int i = 0; i < numPetrolStations; i++) {
                db.insertPetrolStation(new PetrolStation(
                        "Klagenfurt Petrol Station" + i,
                        new Point(new Position(KLAGENFURT_LONG + (Math.random() * 2 - 1) / 50 * 3, KLAGENFURT_LAT + (Math.random() * 2 - 1) / 50 * 3))
                ));
            }
            for (int i = 0; i < numPetrolStations; i++) {
                db.insertPetrolStation(new PetrolStation(
                        "Wien Petrol Station" + i,
                        new Point(new Position(WIEN_LONG + (Math.random() * 2 - 1) / 50 * 3, WIEN_LAT + (Math.random() * 2 - 1) / 50 * 3))
                ));
            }

            res.entity("Instances generated");
        } catch (Exception e) {
            e.printStackTrace();
            res = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        }

        return res.build();
    }
}
