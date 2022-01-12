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

import odin.concepts.common.IMessageInfo;
import odin.concepts.common.Identity;
import odin.concepts.common.Version;
import odin.concepts.domainmodel.ICommand;
import odin.framework.common.MessageInfo;

public class ChangePersonName implements ICommand {

    private static final long serialVersionUID = 1L;
    private final String name;
    private final MessageInfo messageInfo;

    public ChangePersonName(String name, Identity targetId, Version targetVersion) {
        messageInfo = new MessageInfo(new Identity(), LocalDateTime.now(), targetId, targetVersion);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public IMessageInfo getMessageInfo() {
        return messageInfo;
    }
}
