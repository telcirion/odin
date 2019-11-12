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

import java.util.UUID;

import odin.concepts.domainmodel.IAggregateRoot;
import odin.concepts.domainmodel.IDomainEvent;
import odin.concepts.domainmodel.ISendDomainEvent;

public abstract class AbstractAggregateRoot<T> implements IAggregateRoot<T> {

    private final ISendDomainEvent eventSender;
    private final UUID version;
    private final UUID id;

    protected AbstractAggregateRoot(UUID id, ISendDomainEvent eventSender) {
        this.id = id;
        this.version = null;
        this.eventSender = eventSender;
    }

    // snapshot constructor.
    protected AbstractAggregateRoot(AbstractAggregateRoot<T> aggregateRoot) {
        this.id = aggregateRoot.id;
        this.version = aggregateRoot.version;
        this.eventSender = aggregateRoot.eventSender;
    }

    protected AbstractAggregateRoot(AbstractAggregateRoot<T> previousState, IDomainEvent appliedDomainEvent) {
        this.id = previousState.getId();
        this.version = appliedDomainEvent.getEventId();
        this.eventSender = previousState.eventSender;
    }

    @Override
    public IAggregateRoot<T> applyEvent(IDomainEvent event) {
        eventSender.send(event); // tell the world you've chaanged
        return this.dispatch(event);
    }

    @Override
    public UUID getId() {
        return id;
    }
}
