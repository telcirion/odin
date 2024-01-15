
package odin.domainmodel;

/*-
 * -
 * odin-framework
 * 
 * Copyright (C) 2019 - 2024 Peter Jansen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -
 */

import odin.common.MessageDispatcher;

public class TestAggregateRoot implements AggregateRoot {
    private String testField;

    private DomainEvent changeTestField(TestCommand command) {
        return new TestDomainEvent(command.getMessageInfo().subjectId(), command.getTestValue());
    }

    public TestAggregateRoot testFieldChanged(TestDomainEvent event) {
        this.testField = event.getEventData();
        return this;
    }

    public TestAggregateRoot dummy(String event) {
        return this;
    }

    public String getTestField() {
        // force code coverage
        dummy("dummy");
        return testField;
    }

    @Override
    public AggregateRoot source(DomainEvent event) {
        return new MessageDispatcher<>(this).match(String.class, this::dummy, event)
                .match(TestDomainEvent.class, this::testFieldChanged, event).result();
    }

    @Override
    public DomainEvent process(Command command) {
        var event = new MessageDispatcher<DomainEvent>(null).match(TestCommand.class, this::changeTestField, command)
                .result();
        source(event);
        return event;
    }

}
