/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqrs.test.applicationservices.commands;

import cqrs.framework.AbstractCommand;

import java.util.UUID;

public class RegistreerPersoon extends AbstractCommand {

	private final String naam;
	private final String ssn;

    public RegistreerPersoon(UUID targetId, String ssn, String naam){
		super (targetId, null);
    	this.naam=naam;
		this.ssn=ssn;

    }

	public String getSsn() {
		return ssn;
	}
	public String getNaam() {
		return naam;
	}
}
