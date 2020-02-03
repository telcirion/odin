package odin.framework;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import odin.concepts.common.IMessageHandler;

class AbstractAggregateRootTest {
    private static ConcreteAggregateRoot sut;
    private static UUID aggregateId;
    private static final String testSSn = "1";
    private static final String firstTestName = "first";
    private static final String secondTestName = "second";

    @BeforeAll
    static void setUp() {
        aggregateId = UUID.randomUUID();
        sut = new ConcreteAggregateRoot(aggregateId);
        sut.register(testSSn, firstTestName);
        sut.changeName(secondTestName);
    }

    private static class PersonRegistered extends AbstractDomainEvent {
        private static final long serialVersionUID = 1L;
        private final String ssn;
        private final String name;

        protected PersonRegistered(UUID aggregateId, String ssn, String name) {
            super(aggregateId);
            this.ssn = ssn;
            this.name = name;
        }

        public String getSsn() {
            return ssn;
        }

        public String getName() {
            return name;
        }
    }

    private static class PersonNameChanged extends AbstractDomainEvent {

        private static final long serialVersionUID = 1L;
        private final String name;

        public PersonNameChanged(UUID aggregateId, String name) {
            super(aggregateId);
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static class ConcreteAggregateRoot extends AbstractAggregateRoot {

        private String name;
        private String ssn;

        public String getSsn() {
            return ssn;
        }

        public String getName() {
            return name;
        }

        public ConcreteAggregateRoot(final UUID id) {
            super(id);
            this.name = null;
            this.ssn = null;
        }

        private ConcreteAggregateRoot registered(final PersonRegistered event) {
            this.name = event.getName();
            this.ssn = event.getSsn();
            return this;
        }

        private ConcreteAggregateRoot changedName(final PersonNameChanged event) {
            this.name = event.getName();
            return this;
        }

        public void register(final String ssn, final String name) {
            this.applyEvent(new PersonRegistered(getId(), ssn, name));
        }

        public void changeName(final String name) {
            this.applyEvent(new PersonNameChanged(getId(), name));
        }

        @Override
        public <T> IMessageHandler dispatch(final T msg) {
            return match(PersonRegistered.class, this::registered, msg).match(PersonNameChanged.class,
                    this::changedName, msg);

        }
    }

    @Test
    void getAddedEvents() {
        assertEquals(2, sut.getAddedEvents().size());
    }

    @Test
    void getId() {
        assertEquals(aggregateId,sut.getId());
    }

    @Test
    void checkAttributeValues() {
        assertEquals(testSSn, sut.getSsn());
        assertEquals(secondTestName, sut.getName());
    }
}