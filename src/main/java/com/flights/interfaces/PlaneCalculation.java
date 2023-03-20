package com.flights.interfaces;

import com.flights.models.*;

import java.util.List;

public interface PlaneCalculation {

    List<TemporaryPoint> calculateRoute(AirplaneCharacteristics characteristics, List<WayPoint> wayPoints);

    List<TemporaryPoint> calculateRoute(Airplane airplane, Flight flight);


}
