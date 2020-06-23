package odin.framework;

import odin.concepts.domainmodel.IAggregateRoot;
import odin.concepts.domainmodel.ICommand;
import odin.concepts.domainmodel.IDomainEvent;

public class TestAggregateRoot implements IAggregateRoot {
    private String testField;

    private IDomainEvent changeTestField(TestCommand command) {
        return new TestDomainEvent(command.getCommandInfo().getTargetId(), command.getTestValue());
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
    public IAggregateRoot source(IDomainEvent event) {
        return new Matcher<>(this).match(String.class, this::dummy, event)
                .match(TestDomainEvent.class, this::testFieldChanged, event).result();
    }

    @Override
    public IDomainEvent process(ICommand command) {
        var event = new Matcher<IDomainEvent>(null).match(TestCommand.class, this::changeTestField, command).result();
        source(event);
        return event;
    }

}