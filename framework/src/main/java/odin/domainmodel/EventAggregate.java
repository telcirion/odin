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

package odin.domainmodel;

import java.util.ArrayList;
import java.util.List;

import odin.common.Identity;

public class EventAggregate<T extends AggregateRoot> implements Aggregate<T> {

    private final List<DomainEvent> addedEvents;
    private final Identity id;
    private final T aggregateRoot;

    public EventAggregate(final Identity id, final T aggregateRoot) {
        this.id = id;
        this.aggregateRoot = aggregateRoot;
        this.addedEvents = new ArrayList<>();
    }

    @Override
    public List<DomainEvent> getAddedEvents() {
        return addedEvents;
    }

    @Override
    public Identity getId() {
        return id;
    }

    @Override
    public T getAggregateRoot() {
        return aggregateRoot;
    }

    @Override
    public DomainEvent process(Command command) {
        var event = aggregateRoot.process(command);
        this.addedEvents.add(event);
        return event;
    }
}
