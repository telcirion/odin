package cqrs.test.domain.events;

import cqrs.framework.AbstractDomainEvent;

public class PersoonAangemeld extends AbstractDomainEvent {
	
	private final String ssn;
	private final String naam;
	public PersoonAangemeld(String ssn, String naam){
		this.ssn=ssn;
		this.naam = naam;
	}

	public String getNaam() {
		return naam;
	}
	public String getSsn() {
		return ssn;
	}

}
