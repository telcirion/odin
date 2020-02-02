package odin.framework;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class AbstractCommandTest {
    private class ConcreteCommand extends AbstractCommand {

        private static final long serialVersionUID = 1L;

        protected ConcreteCommand(UUID targetId, UUID targetVersion) {
            super(targetId, targetVersion);
        }
    }

    @Test
    public void testAllOfAbstractCommand() {
        UUID targetId = UUID.randomUUID();
        UUID targetVersion = UUID.randomUUID();
        ConcreteCommand sut = new ConcreteCommand(targetId, targetVersion);
        assertEquals(targetId, sut.getTargetId());
        assertEquals(targetVersion, sut.getTargetVersion());
    }

}