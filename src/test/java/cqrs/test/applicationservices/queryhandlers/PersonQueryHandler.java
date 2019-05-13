package cqrs.test.applicationservices.queryhandlers;

import cqrs.concepts.applicationservices.IQueryHandler;
import cqrs.test.applicationservices.queries.PersonByNameQuery;
import cqrs.test.applicationservices.queryresults.PersonQueryResult;
import cqrs.test.domain.state.Person;

import java.util.UUID;

public class PersonQueryHandler implements IQueryHandler {
    public PersonQueryResult query(PersonByNameQuery query){
        Person p=new Person (UUID.randomUUID());
        return new PersonQueryResult(p.registerPerson("123",query.getName()+ " (not really found)"));
    }
}
