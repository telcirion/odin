
package odin.infrastructure;

/*-
 * -
 * odin-framework
 * 
 * Copyright (C) 2019 - 2024 Peter Jansen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -
 */

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import odin.common.Message;
import odin.common.MessageHandler;
import odin.common.TestMessage;

class SimplePubSubTest implements MessageHandler {
    private boolean msgHandled = false;

    @Test
    void verySimplePubSubTest() {
        var sut = new SimplePubSub();
        sut.subscribe(this);
        sut.send(new TestMessage());
        // noinspection StatementWithEmptyBody
        while (!isMsgHandled()) {
            // just wait;
        }
        sut.stop();
        assertTrue(msgHandled);
    }

    @Override
    public MessageHandler handle(Message msg) {
        synchronized (this) {
            msgHandled = true;
        }
        return null;
    }

    public boolean isMsgHandled() {
        synchronized (this) {
            return msgHandled;
        }
    }
}
