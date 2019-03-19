/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqrs.test.domain.state;


import java.util.UUID;

import cqrs.concepts.common.IDispatcher;
import cqrs.framework.AbstractAggregateRoot;
import cqrs.framework.DispatcherBuilder;
import cqrs.test.domain.events.PersoonGeregistreerd;
import cqrs.test.domain.events.PersoonNaamGewijzigd;

/**
 *
 * @author peter
 */

public class Persoon extends AbstractAggregateRoot<Persoon> {

	final private String naam;
	final private String ssn;
	public String getSsn() {
		return ssn;
	}

	public String getNaam() {
		return naam;
	}

	public Persoon(UUID id) {
		super(id);
		this.naam = null;
		this.ssn=null;
	}

	private Persoon(Persoon persoon) {
		super(persoon);
		this.naam = persoon.getNaam();
		this.ssn=persoon.getSsn();
	}

	private Persoon(Persoon previousState, PersoonGeregistreerd event) {
		super(previousState, event);
		this.naam = event.getNaam();
		this.ssn= event.getSsn();
	}

	private Persoon(Persoon previousState, PersoonNaamGewijzigd event) {
		super(previousState, event);
		this.naam = event.getNaam();
		this.ssn=previousState.getSsn();
	}

	@Override
	public Persoon getSnapshot() {
		return new Persoon(this);
	}

	public Persoon RegistreerPersoon(String ssn, String naam) {
		return (Persoon) this.applyEvent(new PersoonGeregistreerd(getId(), ssn, naam));
	}

	public Persoon WijzigNaam(String naam) {
		return (Persoon) this.applyEvent(new PersoonNaamGewijzigd(getId(), naam));
	}


	@Override
	public IDispatcher getDispatcher() {
		return new DispatcherBuilder()
				.dispatch(PersoonGeregistreerd.class, (m) -> new Persoon(this, m))
				.dispatch(PersoonNaamGewijzigd.class, (p) -> new Persoon(this, p))
				.build();
	}

}


