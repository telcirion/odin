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

package odin.domainmodel;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import odin.common.Identity;

class AggregateTest {

    private static Identity aggregateId;
    private static AggregateRoot aggregateRoot;
    private static Aggregate<AggregateRoot> sut;

    @BeforeAll
    static void setUp() {
        aggregateId = new Identity();
        aggregateRoot = new TestAggregateRoot();
        sut = new Aggregate<>(aggregateId, aggregateRoot);
    }

    @Test
    void getAggregateRoot() {
        assertEquals(aggregateRoot, sut.getAggregateRoot());
    }

    @Test
    void processAndgetAddedEvents() {
        sut.process(new TestCommand(aggregateId, null, "value 1"));
        sut.process(new TestCommand(aggregateId, null, "value 2"));

        assertEquals(2, sut.getAddedEvents().size());
    }

    @Test
    void getId() {
        assertEquals(aggregateId, sut.getId());
    }

}