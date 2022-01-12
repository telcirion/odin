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

package odin.framework.applicationservices;

import java.util.List;

import odin.concepts.applicationservices.ICreateAggregateRoot;
import odin.concepts.applicationservices.IEventStore;
import odin.concepts.applicationservices.IRepository;
import odin.concepts.common.ISendMessage;
import odin.concepts.common.Identity;
import odin.concepts.domainmodel.IAggregate;
import odin.concepts.domainmodel.IAggregateRoot;
import odin.concepts.domainmodel.IDomainEvent;
import odin.framework.domainmodel.Aggregate;

public class EventRepository<T extends IAggregateRoot> implements IRepository<T> {

    private final IEventStore es;
    private final ISendMessage eventBus;

    public EventRepository(final IEventStore es, final ISendMessage eventBus) {
        this.eventBus = eventBus;
        this.es = es;
    }

    @Override
    public IAggregate<T> load(Identity id, ICreateAggregateRoot<T> creator) {
        var aggregate = creator.createAggregateRoot();
        final List<IDomainEvent> resultSet = es.load(id);
        resultSet.forEach(aggregate::source);
        return new Aggregate<>(id, (T) aggregate);
    }

    @Override
    public void save(final IAggregate<T> obj) {
        obj.getAddedEvents().forEach(e -> {
            es.save(e);
            eventBus.send(e);
        });
    }
}
