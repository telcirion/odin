/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package odin.example.infrastructure;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import odin.common.Identity;
import odin.domainmodel.DomainEvent;
import odin.infrastructure.EventStore;

@Component
public class SpringEventStore implements EventStore {
    @Autowired
    PersistableDomainEventRepo repo;

    @Override
    public void save(DomainEvent domainEvent) {
        PersistableDomainEvent persistableDomainEvent = new PersistableDomainEvent(domainEvent);
        repo.save(persistableDomainEvent);
    }

    @Override
    public List<DomainEvent> load(Identity id) {
        List<PersistableDomainEvent> persistableDomainEvents = repo.findByAggregateIdOrderByTimestampAsc(id);
        List<DomainEvent> domainEvents = new ArrayList<>();
        persistableDomainEvents.forEach(e -> domainEvents.add(e.unwrap()));
        return domainEvents;
    }

}
