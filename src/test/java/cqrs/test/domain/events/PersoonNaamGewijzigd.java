package cqrs.test.domain.events;

import cqrs.framework.AbstractDomainEvent;

import java.util.UUID;

public class PersoonNaamGewijzigd extends AbstractDomainEvent {

    private final String naam;

    public PersoonNaamGewijzigd(UUID aggregateId, String name) {
        super(aggregateId);
        this.naam = name;
    }

    public String getNaam() {
        return naam;
    }
}
