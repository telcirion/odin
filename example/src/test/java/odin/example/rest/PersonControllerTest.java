package odin.example.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import odin.common.Result;
import odin.domainmodel.DomainEvent;
import odin.example.domain.commands.ChangePersonName;
import odin.example.domain.commands.RegisterPerson;
import odin.example.domain.events.PersonRegistered;
import odin.example.readmodel.PersonReadModelUpdater;
import odin.infrastructure.EventStore;

class PersonControllerTest {

    @Mock
    private EventStore eventStore;

    @Mock
    private PersonReadModelUpdater personReadModelUpdater;

    private PersonController personController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        personController = new PersonController(eventStore, personReadModelUpdater);

    }

    @Test
    void testRegisterPerson() {
        RegisterPerson command = new RegisterPerson();
        Result expected = Result.OK;
        doNothing().when(eventStore).save(any(DomainEvent.class));
        Result actual = personController.postMethodName(command);

        assertEquals(expected, actual);
    }

    @Test
    void testChangePersonName() {
        UUID id = UUID.randomUUID();
        List<DomainEvent> domainEvents = new ArrayList<>();
        domainEvents.add(new PersonRegistered(id, "name", null));
        when(eventStore.load(any(UUID.class))).thenReturn(domainEvents);
        doNothing().when(eventStore).save(any(DomainEvent.class));
        Result expected = Result.OK;
        ChangePersonName command = new ChangePersonName("name", id, UUID.randomUUID());
        Result actual = personController.postMethodName(command);

        assertEquals(expected, actual);
    }

    @Test
    void sourceReturnsNotFoundWhenPersonDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(eventStore.load(id)).thenReturn(List.of());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> personController.source(id));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}