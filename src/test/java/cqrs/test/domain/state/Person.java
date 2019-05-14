// Copyright 2019 Peter Jansen
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


package cqrs.test.domain.state;


import java.util.UUID;

import cqrs.concepts.common.IDispatcher;
import cqrs.concepts.common.IMessageHandler;
import cqrs.framework.AbstractAggregateRoot;
import cqrs.framework.DispatcherBuilder;
import cqrs.test.applicationservices.commands.ChangePersonName;
import cqrs.test.applicationservices.commands.RegisterPerson;
import cqrs.test.domain.events.PersonRegistered;
import cqrs.test.domain.events.PersonNameChanged;

/**
 *
 * @author peter
 */

public class Person extends AbstractAggregateRoot<Person> {

	final private String name;
	final private String ssn;
	public String getSsn() {
		return ssn;
	}

	public String getName() {
		return name;
	}

	public Person(UUID id) {
		super(id);
		this.name = null;
		this.ssn=null;
	}

	private Person(Person person) {
		super(person);
		this.name = person.getName();
		this.ssn= person.getSsn();
	}

	private Person(Person previousState, PersonRegistered event) {
		super(previousState, event);
		this.name = event.getName();
		this.ssn= event.getSsn();
	}

	private Person(Person previousState, PersonNameChanged event) {
		super(previousState, event);
		this.name = event.getName();
		this.ssn=previousState.getSsn();
	}

	@Override
	public Person getSnapshot() {
		return new Person(this);
	}

	public Person registerPerson(String ssn, String name) {
		return (Person) this.applyEvent(new PersonRegistered(getId(), ssn, name));
	}

	public Person changeName(String name) {
		return (Person) this.applyEvent(new PersonNameChanged(getId(), name));
	}


	@Override
	public IDispatcher getDispatcher() {
		return new DispatcherBuilder()
				.dispatch(PersonRegistered.class, (m) -> new Person(this, m))
				.dispatch(PersonNameChanged.class, (p) -> new Person(this, p))
				.build();
	}

	@Override
	public <T,Z extends IMessageHandler> Z getDispatcher2(T msg) {
		return match(PersonRegistered.class, (m) -> new Person(this, m),msg)
				.match(PersonNameChanged.class, (p) -> new Person(this, p),msg);

	}
}


