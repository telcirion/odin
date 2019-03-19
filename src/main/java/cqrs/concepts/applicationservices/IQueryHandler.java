package cqrs.concepts.applicationservices;

public interface IQueryHandler<T extends IQuery, C extends IQueryResult> {
	C handle(T query);
}
