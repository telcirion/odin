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

import java.util.HashMap;
import java.util.Map;

public class PersonList {
    private final Map<String, Person> persons = new HashMap<>();

    public void add(Person person) {
        synchronized (this) {
            this.persons.put(person.id().toString(), person);
        }
    }

    public void updateName(Person person) {
        synchronized (this) {

            Person oldPerson = this.persons.get(person.id().toString());
            Person newPerson = new Person(person.id(), person.name(), oldPerson.ssn());
            this.persons.replace(person.id().toString(), newPerson);
        }
    }

    public Person findPerson(String name) {
        synchronized (this) {

            for (Person p : persons.values()) {
                if (p.name().equals(name)) {
                    return p;
                }
            }
            return null;
        }
    }

}
