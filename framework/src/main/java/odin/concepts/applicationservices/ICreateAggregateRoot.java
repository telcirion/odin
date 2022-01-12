package odin.concepts.applicationservices;

import odin.concepts.domainmodel.IAggregateRoot;

@FunctionalInterface
public interface ICreateAggregateRoot<T extends IAggregateRoot> {
    T createAggregateRoot();
}
