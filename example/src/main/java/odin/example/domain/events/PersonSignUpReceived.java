
package odin.example.domain.events;

/*-
 * -
 * odin-example
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

import lombok.NoArgsConstructor;
import odin.common.Identity;
import odin.domainmodel.DomainEvent;

@NoArgsConstructor
public class PersonSignUpReceived extends DomainEvent {

    private static final long serialVersionUID = 1L;
    private String lastName;
    private String firstName;

    public PersonSignUpReceived(String lastName, String firstName) {
        super(new Identity());
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
