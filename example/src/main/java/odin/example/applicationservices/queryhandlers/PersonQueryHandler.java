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

package odin.example.applicationservices.queryhandlers;

import odin.concepts.applicationservices.IQueryHandler;
import odin.example.applicationservices.queries.PersonByNameQuery;
import odin.example.applicationservices.queryresults.PersonQueryResult;
import odin.example.readmodel.PersonList;

public class PersonQueryHandler implements IQueryHandler {
    private PersonList personList;

    public PersonQueryHandler(PersonList personList) {
        this.personList = personList;
    }

    public PersonQueryResult query(PersonByNameQuery query) {
        return new PersonQueryResult(personList.findPerson(query.name()));
    }
}
