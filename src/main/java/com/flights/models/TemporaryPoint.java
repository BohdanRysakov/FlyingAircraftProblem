package com.flights.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TemporaryPoint extends WayPoint {
    @NonNull
    @Min(0)
    @Min(360)
    private double courseDegree;

    public TemporaryPoint(double latitude, double longitude, double flightHeight, double flightSpeed, double courseDegree) {
        super(latitude, longitude, flightHeight, flightSpeed);
        this.courseDegree = courseDegree;
    }

    public TemporaryPoint createTemporaryPointWithSameParams() {

        TemporaryPoint newTP = new TemporaryPoint();
        newTP.setLatitude(this.getLatitude());
        newTP.setLongitude(this.getLongitude());
        newTP.setFlightHeight(this.getFlightHeight());
        newTP.setCourseDegree(this.getCourseDegree());
        return newTP;

    }
}
