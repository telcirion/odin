package odin.example.infrastructure.openapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    @Consumes(MediaType.APPLICATION_JSON)
    public void register(RegisterPerson rp){
        // for future expansions
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void changeName(ChangePersonName rp){
        // for future expansions
    }

}