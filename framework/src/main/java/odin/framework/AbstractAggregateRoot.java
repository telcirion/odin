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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import odin.concepts.domainmodel.IAggregateRoot;
import odin.concepts.domainmodel.IDomainEvent;

public abstract class AbstractAggregateRoot implements IAggregateRoot {

    private final List<IDomainEvent> addedEvents;
    private final UUID id;
    
    protected AbstractAggregateRoot(UUID id) {
        this.id = id;
        this.addedEvents = new ArrayList<>();
    }

    @Override
    public List<IDomainEvent> getAddedEvents() {
        return addedEvents;
    }

    @Override
    public void applyEvent(IDomainEvent event) {
        this.addedEvents.add(event);
        this.dispatch(event);
    }

    @Override
    public UUID getId() {
        return id;
    }
}
