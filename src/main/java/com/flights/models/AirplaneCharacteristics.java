package com.flights.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;


@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirplaneCharacteristics {

    @NonNull
    @Min(1)
    double maxSpeed;

    @NonNull
    @Min(1)
    double deltaSpeed;

    @NonNull
    @Min(1)
    double deltaHigh;

    @NonNull
    @Min(1)
    double deltaCourseDegree;
}
