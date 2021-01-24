package odin.framework.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import odin.concepts.common.IMessageInfo;
import odin.concepts.common.Identity;
import odin.concepts.common.Version;

class MessageInfoTest {
    private static IMessageInfo sut;
    private static Identity aggregateId;
    private static Version aggregateVersion;


    @BeforeAll
    static void setUp() {
        aggregateId = new Identity();
        aggregateVersion = new Version();
        sut = new MessageInfo(aggregateId, aggregateVersion);
    }


    @Test
    void getEventId() {
        assertNotNull(sut.getMessageId());
    }

    @Test
    void getSubjectId() {
        assertEquals(aggregateId, sut.getSubjectId());
    }

    @Test
    void getSubjectVersion() {
        assertEquals(aggregateVersion, sut.getSubjectVersion());
    }

    @Test
    void getTimestamp() {
        assertNotNull(sut.getTimestamp());
    }
}