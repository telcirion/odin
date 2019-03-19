/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqrs.test.applicationservices.commands;

import cqrs.framework.AbstractCommand;

import java.util.UUID;

public class WijzigNaamPersoon extends AbstractCommand {

    private final String naam;

    public WijzigNaamPersoon(String naam, UUID targetId, UUID targetVersion) {
        super(targetId, targetVersion);
        this.naam = naam;
    }
    public String getNaam() {
        return this.naam;
    }
}
