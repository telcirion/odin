package cqrs.framework;

import cqrs.concepts.applicationservices.ICommand;

import java.util.UUID;

public abstract class AbstractCommand implements ICommand {

    private final UUID targetId;
    private final UUID targetVersion;

    protected AbstractCommand(UUID targetId, UUID targetVersion){
        this.targetId=targetId;
        this.targetVersion=targetVersion;
    }

    @Override
    public UUID getTargetId() {
        return targetId;
    }
    @Override
    public UUID getTargetVersion() {
        return targetVersion;
    }
}
