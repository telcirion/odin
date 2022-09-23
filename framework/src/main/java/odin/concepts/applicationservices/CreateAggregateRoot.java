package odin.concepts.applicationservices;

import odin.concepts.domainmodel.AggregateRoot;

@FunctionalInterface
public interface CreateAggregateRoot<T extends AggregateRoot> {
    T createAggregateRoot();
}
