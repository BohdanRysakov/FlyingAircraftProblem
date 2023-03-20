package com.flights.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WayPoint {
    @NonNull
    private double latitude;
    @NonNull
    private double longitude;
    @NonNull
    private double flightHeight;

    @NonNull
    @Min(1)
    private double flightSpeed;
}
