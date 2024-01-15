
package odin.example.infrastructure;

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

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import odin.common.Identity;
import odin.domainmodel.DomainEvent;
import odin.example.ExampleApplication;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Event")
public class PersistableDomainEvent {
    private static final Logger log = LoggerFactory.getLogger(ExampleApplication.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Identity aggregateId;
    private LocalDateTime timestamp;
    private String eventJson;
    private String domainEventClassName;

    PersistableDomainEvent(DomainEvent event) {
        domainEventClassName = event.getClass().getCanonicalName();
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {

            eventJson = mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            log.error(getEventJson(), e);
        }
        this.timestamp = event.getMessageInfo().timestamp();
        this.aggregateId = event.getMessageInfo().subjectId();
    }

    DomainEvent unwrap() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            return (DomainEvent) mapper.readValue(eventJson, Class.forName(domainEventClassName));
        } catch (JsonProcessingException | ClassNotFoundException e) {
            log.error(getEventJson(), e);
        }
        return null;
    }
}
