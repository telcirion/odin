
package odin.domainmodel;

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
