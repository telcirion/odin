package odin.example.infrastructure.openapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import odin.example.domain.commands.ChangePersonName;
import odin.example.domain.commands.RegisterPerson;
import odin.example.domain.state.Person;

@Path("/persons")
public class PersonResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Person hello() {
        return new Person();
    }

    @POST
    @Path("/registrations")
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "202")
    public Response register(RegisterPerson rp) {
        // for future expansions
        return Response.accepted().build();
    }

    @POST
    @Path("/namechanges")
    @Consumes(MediaType.APPLICATION_JSON)
    @APIResponse(responseCode = "202")
    public Response changeName(ChangePersonName rp) {
        // for future expansions

        return Response.accepted().build();
    }

}