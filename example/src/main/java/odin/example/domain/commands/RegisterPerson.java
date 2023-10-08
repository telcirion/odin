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

package odin.example.domain.commands;

import java.time.LocalDateTime;

import odin.concepts.common.Identity;
import odin.concepts.common.MessageInfo;
import odin.concepts.domainmodel.Command;
import odin.framework.common.MessageInfoRecord;

public class RegisterPerson implements Command {

    private static final long serialVersionUID = 1L;
    private final MessageInfoRecord messageInfo;
    private final String firstName;
    private final String lastName;

    public RegisterPerson(Identity targetId, String lastName, String firstName) {
        messageInfo = new MessageInfoRecord(new Identity(), LocalDateTime.now(), targetId, null);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public MessageInfo getMessageInfo() {
        return messageInfo;
    }
}
