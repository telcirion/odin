/* Copyright 2019 Peter Jansen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package odin.framework;

import odin.concepts.domainmodel.IDomainEvent;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class AbstractDomainEvent implements IDomainEvent {

	private final UUID id=UUID.randomUUID();
	private final UUID aggregateId;
	private final LocalDateTime timestamp;

	protected AbstractDomainEvent(){
		this.aggregateId=null;
		this.timestamp=LocalDateTime.now();
	}

	protected AbstractDomainEvent(UUID aggregateId) {
		this.aggregateId=aggregateId;
		this.timestamp=LocalDateTime.now();
	}

	@Override
	public UUID getEventId() {
		return id;
	}

	@Override
	public UUID getAggregateId() {
		return aggregateId;
	}

	@Override
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
}
