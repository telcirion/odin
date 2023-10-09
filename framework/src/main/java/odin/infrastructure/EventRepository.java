/* Copyright 2020 Peter Jansen
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

package odin.infrastructure;

import java.util.List;

import odin.applicationservices.CreateAggregateRoot;
import odin.applicationservices.Repository;
import odin.common.Identity;
import odin.common.SendMessage;
import odin.domainmodel.Aggregate;
import odin.domainmodel.AggregateRoot;
import odin.domainmodel.DomainEvent;

public class EventRepository<T extends AggregateRoot> implements Repository<T> {

    private final EventStore es;
    private final SendMessage eventBus;

    public EventRepository(final EventStore es, final SendMessage eventBus) {
        this.eventBus = eventBus;
        this.es = es;
    }

    @Override
    public Aggregate<T> load(Identity id, CreateAggregateRoot<T> creator) {
        var aggregate = creator.createAggregateRoot();
        final List<DomainEvent> resultSet = es.load(id);
        resultSet.forEach(aggregate::source);
        return new Aggregate<>(id, aggregate);
    }

    @Override
    public void save(final Aggregate<T> obj) {
        obj.getAddedEvents().forEach(e -> {
            es.save(e);
            eventBus.send(e);
        });
    }
}
