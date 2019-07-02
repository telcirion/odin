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

import odin.concepts.domainmodel.IAggregateRoot;
import odin.concepts.domainmodel.IDomainEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractAggregateRoot<T extends IAggregateRoot<T>> implements IAggregateRoot<T> {

    private final IAggregateRoot<T> previousState;
    private final UUID version;

    private final IDomainEvent appliedDomainEvent;
    private final UUID id;

    protected AbstractAggregateRoot(UUID id) {
        this.id = id;
        this.previousState = null;
        this.appliedDomainEvent = null;
        this.version = null;
    }

    // snapshot constructor.
    protected AbstractAggregateRoot(AbstractAggregateRoot<T> aggregateRoot) {
        this.id = aggregateRoot.id;
        this.previousState = null;
        this.appliedDomainEvent = null;
        this.version = aggregateRoot.version;
    }

    protected AbstractAggregateRoot(AbstractAggregateRoot<T> previousState, IDomainEvent appliedDomainEvent) {
        this.id = previousState.getId();
        this.previousState = previousState;
        this.appliedDomainEvent = appliedDomainEvent;
        this.version = appliedDomainEvent.getEventId();
    }

    @Override
    public List<IDomainEvent> getEvents() {
        if (previousState != null) {
            List<IDomainEvent> i = previousState.getEvents();
            i.add(appliedDomainEvent);
            return i;
        }
        return new ArrayList<>();
    }

    @Override
    public IAggregateRoot<T> applyEvent(IDomainEvent event) {
        return this.dispatch(event);
    }

    @Override
    public UUID getId() {
        return id;
    }
}
