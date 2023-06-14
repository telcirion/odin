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

package odin.framework.infrastructure;

import java.util.ArrayList;
import java.util.List;

import odin.concepts.applicationservices.EventStore;
import odin.concepts.common.Identity;
import odin.concepts.domainmodel.DomainEvent;

public class InMemoryEventStore implements EventStore {

    List<DomainEvent> eventStore = new ArrayList<>();

    @Override
    public void save(DomainEvent domainEvents) {
        eventStore.add(domainEvents);
    }

    @Override
    public List<DomainEvent> load(Identity id) {
        List<DomainEvent> filteredEvents = new ArrayList<>();

        filteredEvents.addAll(
                eventStore.stream().filter(c -> c.getMessageInfo().subjectId().getId().equals(id.getId())).toList());
        return filteredEvents;
    }

}
