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

package odin.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import odin.common.Identity;
import odin.domainmodel.TestDomainEvent;

class InMemoryEventStoreTest {
    @Test
    void eventStoreTest() {
        var eventStore = new InMemoryEventStore();

        Identity aggregateId1 = new Identity();
        Identity aggregateId2 = new Identity();

        var testDomainEvent1 = new TestDomainEvent(aggregateId1, "event 1");
        var testDomainEvent2 = new TestDomainEvent(aggregateId2, "event 2");
        var testDomainEvent3 = new TestDomainEvent(aggregateId1, "event 3");

        eventStore.save(testDomainEvent1);
        eventStore.save(testDomainEvent2);
        eventStore.save(testDomainEvent3);

        var loadedEvents1 = eventStore.load(aggregateId1);
        var loadedEvents2 = eventStore.load(aggregateId2);

        assertEquals(2, loadedEvents1.size());
        assertEquals(1, loadedEvents2.size());

        assertEquals(testDomainEvent1.getMessageInfo().messageId().getId().toString(),
                loadedEvents1.getFirst().getMessageInfo().messageId().getId().toString());
    }
}