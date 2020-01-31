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

package odin.example.applicationservices.commands;

import odin.framework.AbstractCommand;

import java.util.UUID;

public class ChangePersonName extends AbstractCommand {

    private static final long serialVersionUID = 1L;
    private final String name;

    public ChangePersonName(String name, UUID targetId, UUID targetVersion) {
        super(targetId, targetVersion);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
