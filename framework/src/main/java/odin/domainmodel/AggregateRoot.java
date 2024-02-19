
package odin.domainmodel;

public interface AggregateRoot {
    DomainEvent process(Command command);

    AggregateRoot source(DomainEvent event);
}
