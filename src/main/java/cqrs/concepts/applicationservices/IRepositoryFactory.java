package cqrs.concepts.applicationservices;

import cqrs.concepts.domainmodel.IAggregateRoot;

public interface IRepositoryFactory {
	<T extends IAggregateRoot<T>> IRepository<T> getRepository();
}
