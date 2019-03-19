package cqrs.test.domain.events;

import cqrs.framework.AbstractDomainEvent;
import java.util.UUID;

public class PersoonGeregistreerd extends AbstractDomainEvent {
	private final String ssn;
	private final String naam;


	public PersoonGeregistreerd(UUID aggregateId, String ssn,String name){
		super(aggregateId);
		this.ssn=ssn;
		this.naam =name;
	}

	public String getSsn() {
		return ssn;
	}

	public String getNaam() {
		return naam;
	}
}

