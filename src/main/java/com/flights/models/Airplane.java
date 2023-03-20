package com.flights.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Airplane {
    @MongoId
    private Long id;
    @NonNull
    private AirplaneCharacteristics airplaneCharacteristics;
    private TemporaryPoint position;
    private List<Flight> flights;

}
