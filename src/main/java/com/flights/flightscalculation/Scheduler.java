package com.flights.flightscalculation;

import com.flights.interfaces.PlaneCalculationStrategy;
import com.flights.models.Airplane;
import com.flights.models.Flight;
import com.flights.models.TemporaryPoint;
import com.flights.repositories.AirplaneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Scheduler {
    @Autowired
    private PlaneCalculationStrategy planeCalculationStrategy;
    private Map<Long, List<TemporaryPoint>> airplaneTemporaryPointMap;
    @Autowired
    private AirplaneRepository airplaneRepository;

    public void initiateFlights(Airplane airplane) {
        Long airplaneId = airplane.getId();
        initAirplaneTemporaryPointsMap(airplaneId);
        airplane.getFlights().forEach(flight ->
                airplaneTemporaryPointMap.get(airplaneId).addAll(startFlight(airplane, flight))
        );
        airplaneRepository.save(airplane);
    }

    private List<TemporaryPoint> startFlight(Airplane airplane, Flight flight) {

        if (flight.getWayPointList() == null || flight.getWayPointList().size() == 0) {
            return new ArrayList<>();
        }
        return planeCalculationStrategy.startCalculation(airplane, flight);
    }

    private void initAirplaneTemporaryPointsMap(Long airplaneId) {
        airplaneTemporaryPointMap = new HashMap<>();
        airplaneTemporaryPointMap.put(airplaneId, new ArrayList<>());
    }

    public Map<Long, List<TemporaryPoint>> getAirplaneTemporaryPointMap() {
        return airplaneTemporaryPointMap;
    }
}
