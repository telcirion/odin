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

package odin.example.readmodel;

import java.util.UUID;

public class Person {

    private final String name;
    private final String ssn;
    private final UUID id;

    public String getSsn() {
        return ssn;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public Person(UUID id, String name, String ssn) {
        this.id = id;
        this.name = name;
        this.ssn = ssn;
    }
}