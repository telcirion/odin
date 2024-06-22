
package odin.applicationservices;

import java.util.UUID;

import odin.domainmodel.Aggregate;
import odin.domainmodel.AggregateRoot;

public interface Repository<T extends AggregateRoot> {
    public Aggregate<T> load(UUID id, CreateAggregateRoot<T> creator);

    public void save(Aggregate<T> obj);
}
