package odin.framework;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import odin.concepts.common.Identity;

class AbstractCommandTest {
    private class ConcreteCommand extends AbstractCommand {

        private static final long serialVersionUID = 1L;

        protected ConcreteCommand(Identity targetId, UUID targetVersion) {
            super(targetId, targetVersion);
        }
    }

    @Test
    public void testAllOfAbstractCommand() {
        Identity targetId = new Identity();
        UUID targetVersion = UUID.randomUUID();
        ConcreteCommand sut = new ConcreteCommand(targetId, targetVersion);
        assertEquals(targetId, sut.getTargetId());
        assertEquals(targetVersion, sut.getTargetVersion());
    }

}