/*
 * To change this license header, choose License Headers in Project Properties.s
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqrs.framework;

import cqrs.concepts.common.IDispatcher;
import cqrs.concepts.domainmodel.IAggregateRoot;
import cqrs.concepts.domainmodel.IDomainEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author peter
 */
public abstract class AbstractAggregateRoot<T extends IAggregateRoot<T>> implements IAggregateRoot<T> {

	private final IDispatcher dispatcher;
	private final IAggregateRoot<T> previousState;
	private final UUID version;

	private final IDomainEvent appliedDomainEvent;
	private final UUID id;

	protected AbstractAggregateRoot(UUID id){
		this.id=id;
		this.previousState=null;
		this.appliedDomainEvent=null;
		this.version=null;
		this.dispatcher =this.getDispatcher();
	}
	// snapshot constructor.
	protected AbstractAggregateRoot(AbstractAggregateRoot<T> aggregateRoot){
		this.id=aggregateRoot.id;
		this.previousState=null;
		this.appliedDomainEvent=null;
		this.dispatcher =this.getDispatcher();
		this.version=aggregateRoot.version;
	}

	protected AbstractAggregateRoot(AbstractAggregateRoot<T> previousState, IDomainEvent appliedDomainEvent){
		this.id=previousState.getId();
		this.previousState=previousState;
		this.appliedDomainEvent=appliedDomainEvent;
		this.dispatcher =this.getDispatcher();
		this.version=appliedDomainEvent.getEventId();
	}

	@Override
	public List<IDomainEvent> getEvents() {
		if (previousState!=null){
			 var i=previousState.getEvents();
			 i.add(appliedDomainEvent);
			 return i;
		}
		return new ArrayList<>();
	}

	@Override
	public IAggregateRoot<T>  applyEvent(IDomainEvent event) {
		return this.dispatcher.dispatch(event);
	}

	@Override
	public UUID getId() {
		return id;
	}
}
