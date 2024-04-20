package pkgService;

import com.google.gson.Gson;
import pkgData.Database;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("books")
@RequestScoped
public class Books {
    @Context
    private UriInfo context;

    public Books() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks() {
        Response.ResponseBuilder response = Response.status(Response.Status.OK);
        try {
            Database db = Database.getInstance();
            response.entity(new Gson().toJson(db.getBooks()));
        } catch (Exception e) {
            response.status(Response.Status.INTERNAL_SERVER_ERROR);
            response.entity("[ERROR] " + e.getMessage());
        }
        System.out.println("=============== web service GET LIST called");
        return response.build();
    }
}