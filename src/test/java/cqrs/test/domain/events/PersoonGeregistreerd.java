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

