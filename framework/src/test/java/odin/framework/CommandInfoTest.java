package odin.framework;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import odin.concepts.common.Identity;
import odin.concepts.domainmodel.ICommandInfo;

class CommandInfoTest {


    @Test
    public void testAllOfAbstractCommand() {
        Identity targetId = new Identity();
        UUID targetVersion = UUID.randomUUID();
        ICommandInfo sut = new CommandInfo(targetId, targetVersion);
        assertEquals(targetId, sut.getTargetId());
        assertEquals(targetVersion, sut.getTargetVersion());
    }

}