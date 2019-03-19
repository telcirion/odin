/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqrs.concepts.applicationservices;

import cqrs.concepts.domainmodel.IAggregateRoot;

/**
 *
 * @author peter
 */
public interface IRepository<T extends IAggregateRoot<T>> {
    void create(IAggregateRoot<T> obj);
    void update(IAggregateRoot<T> obj);
    T get(IAggregateRoot<T> obj);
}
