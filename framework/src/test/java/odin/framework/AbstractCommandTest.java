package odin.framework;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AbstractCommandTest {
    private static class ConcreteCommand extends AbstractCommand{

        protected ConcreteCommand(UUID targetId, UUID targetVersion) {
            super(targetId, targetVersion);
        }
        @Override
        public UUID getTargetId() {
            return super.getTargetId();
        }

        @Override
        public UUID getTargetVersion() {
            return super.getTargetVersion();
        }
    }
    @Test
    public void testAllOfAbstractCommand(){
        UUID targetId=UUID.randomUUID();
        UUID targetVersion=UUID.randomUUID();
        ConcreteCommand sut= new ConcreteCommand(targetId, targetVersion);
        assertEquals(targetId, sut.getTargetId());
        assertEquals(targetVersion, sut.getTargetVersion());
    }

}