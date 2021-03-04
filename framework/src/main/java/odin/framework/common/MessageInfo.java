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

package odin.framework.common;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import odin.concepts.common.IMessageInfo;
import odin.concepts.common.Identity;
import odin.concepts.common.Version;

public class MessageInfo implements IMessageInfo {

    private static final long serialVersionUID = 1L;
    private final Identity messageId = new Identity();
    private final Identity subjectId;
    private final LocalDateTime timestamp;
    private final Version subjectVersion;

    
    public MessageInfo(Identity subjectId, Version subjectVersion) {
        this.subjectId = subjectId;
        this.subjectVersion = subjectVersion;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public Identity getMessageId() {
        return messageId;
    }

    @Override
    public Identity getSubjectId() {
        return subjectId;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public Version getSubjectVersion() {
        return subjectVersion;
    }
}
