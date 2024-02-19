
package odin.applicationservices;

import odin.domainmodel.AggregateRoot;

@FunctionalInterface
public interface CreateAggregateRoot<T extends AggregateRoot> {
    T createAggregateRoot();
}
