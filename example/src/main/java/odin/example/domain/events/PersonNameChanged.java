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

import odin.concepts.common.IMessageInfo;
import odin.concepts.common.Identity;
import odin.concepts.domainmodel.IDomainEvent;
import odin.framework.common.MessageInfo;

public class PersonNameChanged implements IDomainEvent {

    private static final long serialVersionUID = 1L;
    private final MessageInfo messageInfo;
    private final String name;

    public PersonNameChanged(Identity aggregateId, String name) {
        messageInfo = new MessageInfo(aggregateId, null);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public IMessageInfo getMessageInfo() {
        return messageInfo;
    }
}
