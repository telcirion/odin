/* Copyright 2019 Peter Jansen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package odin.example.domain.events;

import java.time.LocalDateTime;

import odin.concepts.common.Identity;
import odin.concepts.common.MessageInfo;
import odin.concepts.domainmodel.DomainEvent;
import odin.framework.common.MessageInfoRecord;

public class PersonRegistered implements DomainEvent {
    private static final long serialVersionUID = 1L;

    private final String ssn;
    private final String name;
    private final MessageInfoRecord messageInfo;

    public PersonRegistered(Identity id, String ssn, String name) {
        messageInfo = new MessageInfoRecord(new Identity(), LocalDateTime.now(), id, null);
        this.ssn = ssn;
        this.name = name;
    }

    public String getSsn() {
        return ssn;
    }

    public String getName() {
        return name;
    }

    @Override
    public MessageInfo getMessageInfo() {
        return messageInfo;
    }
}
