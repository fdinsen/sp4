package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import dto.PersonDTO;
import entities.Person;
import exceptions.GenericExceptionMapper;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import exceptions.PersonNotFoundExceptionMapper;
import utils.EMF_Creator;
import facades.PersonFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Todo Remove or change relevant parts before ACTUAL use
@Path("person")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    //An alternative way to get the EntityManagerFactory, whithout having to type the details all over the code
    //EMF = EMF_Creator.createEntityManagerFactory(DbSelector.DEV, Strategy.CREATE);
    private static final PersonFacade FACADE = PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersonById(@PathParam("id") int id) throws PersonNotFoundException {
        return Response.ok().entity(GSON.toJson(FACADE.getPerson(id))).build();

    }

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersons() {
        return Response.ok().entity(GSON.toJson(FACADE.getAllPersons())).build();
    }

    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPerson(String person) throws MissingInputException {
        PersonDTO personToAdd = GSON.fromJson(person, PersonDTO.class);
        PersonDTO addedPerson 
                = FACADE.addPerson(personToAdd.getfName(), personToAdd.getlName(), personToAdd.getPhone(), 
                        personToAdd.getStreet(), personToAdd.getZip(), personToAdd.getCity());
        return Response.ok(addedPerson).build();
    }

    //TODO change to PUT, take id as path parameter
    @Path("/edit/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editPerson(@PathParam("id") int id, String person) throws PersonNotFoundException {
        PersonDTO personToEdit = GSON.fromJson(person, PersonDTO.class);
        personToEdit.setId(id);
        return Response.ok(FACADE.editPerson(personToEdit)).build();

    }

    @Path("/delete/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deletePerson(@PathParam("id") int id) throws PersonNotFoundException {
        PersonDTO deleted = FACADE.deletePerson(id);
        return "{\"status\": \"removed\"}";

    }
}
