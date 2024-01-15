/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package odin.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MessageInfoTest {
    private static MessageInfo sut;
    private static Identity aggregateId;
    private static Version aggregateVersion;

    @BeforeAll
    static void setUp() {
        aggregateId = new Identity();
        aggregateVersion = new Version();
        sut = new MessageInfoRecord(new Identity(), LocalDateTime.now(), aggregateId, aggregateVersion);
    }

    @Test
    void getEventId() {
        assertNotNull(sut.messageId());
    }

    @Test
    void getSubjectId() {
        assertEquals(aggregateId, sut.subjectId());
        assertEquals(aggregateId.getId(), sut.subjectId().getId());
    }

    @Test
    void getSubjectVersion() {
        assertEquals(aggregateVersion, sut.subjectVersion());
    }

    @Test
    void getTimestamp() {
        assertNotNull(sut.timestamp());
    }
}