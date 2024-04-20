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
public class Book {

    @Context
    private UriInfo context;
    public Book() {

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("{bookid}")
    public Response getBook(@PathParam("bookid") String id) {
        Database db = Database.getInstance();
        Response.ResponseBuilder res = Response.status(Response.Status.OK);

        try {
            pkgData.Book retBook = db.getBook(Integer.parseInt(id));
            res.entity(new Gson().toJson(retBook));
        } catch(Exception e) {
            res.status(Response.Status.NOT_FOUND);
            System.out.println(e.getMessage());
        }

        return res.build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response newBook(String strBook) throws Exception {
        Response.ResponseBuilder response = Response.status(Response.Status.CREATED);
        Database db = Database.getInstance();
        try {
            pkgData.Book book = new Gson().fromJson(strBook, pkgData.Book.class);
            db.setBook(book);
            response.entity(book.toString());
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
    public Response deleteBook(@QueryParam("bookid") String id) throws Exception {
        Response.ResponseBuilder response = Response.status(Response.Status.OK);
        Database db = Database.getInstance();

        try {
            pkgData.Book retBook = db.deleteBook(Integer.valueOf(id));
            response.entity(retBook.toString());
        } catch (Exception e) {
            response.status(Response.Status.BAD_REQUEST);
            response.entity("[ERROR] " + e.getMessage());
        }

        return response.build();
    }

    @PATCH
    @Path("{bookid}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response patchBook(String strBook, @PathParam("bookid") String id) throws Exception {
        return updateBook(strBook, id);
    }

    private Response updateBook(String strBook, String bookid) throws Exception {
        Database db = Database.getInstance();
        Response.ResponseBuilder response = Response.status(Response.Status.OK);
        try {
            pkgData.Book newBook = new Gson().fromJson(strBook, pkgData.Book.class);
            pkgData.Book book = db.getBook(Integer.parseInt(bookid));
            book.setAuthor(newBook.getAuthor());
            book.setTitle(newBook.getTitle());
            book.setNameOfImage(newBook.getNameOfImage());
            response.entity(book.toString());
            System.out.println("======== webservice PUT/PATCH called");
        } catch (Exception e) {
            response = Response.status(Response.Status.BAD_REQUEST);
            response.entity("[ERROR] " + e.getMessage());
        }

        return response.build();
    }
}
