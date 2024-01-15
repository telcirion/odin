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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import odin.common.Identity;
import odin.common.Message;
import odin.common.SendMessage;
import odin.domainmodel.Aggregate;
import odin.domainmodel.TestAggregateRoot;
import odin.domainmodel.TestCommand;

class EventRepositoryTest {
    @Test
    void eventRepositoryTest() {
        InMemoryEventStore eventStore = new InMemoryEventStore();
        var sut = new EventRepository<TestAggregateRoot>(eventStore, new TestBus());

        var id = new Identity();
        var saveAggregate = new Aggregate<>(id, new TestAggregateRoot());
        saveAggregate.process(new TestCommand(id, null, "value 1"));
        assertNotNull(saveAggregate.getAddedEvents().get(0).getMessageInfo().timestamp());
        sut.save(saveAggregate);
        var loadAggregate = sut.load(id, TestAggregateRoot::new);
        assertEquals(saveAggregate.getAggregateRoot().getTestField(), loadAggregate.getAggregateRoot().getTestField());
    }

    private class TestBus implements SendMessage {

        @Override
        public void send(Message m) {
        }

    }
}