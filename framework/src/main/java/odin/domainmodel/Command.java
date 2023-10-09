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

package odin.domainmodel;

import java.time.LocalDateTime;

import odin.common.Identity;
import odin.common.Message;
import odin.common.MessageInfo;
import odin.common.MessageInfoRecord;
import odin.common.Version;

public abstract class Command implements Message {
    private MessageInfoRecord messageInfo;

    public Command(Identity id, Version targetVersion) {
        this.messageInfo = new MessageInfoRecord(new Identity(), LocalDateTime.now(), id, targetVersion);
    }

    @Override
    public MessageInfo getMessageInfo() {
        return messageInfo;
    }
}
