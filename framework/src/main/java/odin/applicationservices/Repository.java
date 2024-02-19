
package odin.applicationservices;

import odin.common.Identity;
import odin.domainmodel.Aggregate;
import odin.domainmodel.AggregateRoot;

public interface Repository<T extends AggregateRoot> {
    public Aggregate<T> load(Identity id, CreateAggregateRoot<T> creator);

    public void save(Aggregate<T> obj);
}
