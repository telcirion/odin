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

import odin.common.Identity;

public class TestDomainEvent extends DomainEvent {

    private static final long serialVersionUID = 1L;

    private final String eventData;

    public TestDomainEvent(final Identity aggregateId, String eventData) {
        super(aggregateId);
        this.eventData = eventData;
    }

    public String getEventData() {
        return eventData;
    }

}