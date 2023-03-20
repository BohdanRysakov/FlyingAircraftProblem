package com.flights.controller;

import com.flights.flightscalculation.Scheduler;
import com.flights.models.*;
import com.flights.repositories.AirplaneRepository;
import com.flights.service.JSONOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
public class AirplaneController {

    @Autowired
    private AirplaneRepository airplaneRepository;
    @Autowired
    private JSONOperator jsonOperator;
    @Autowired
    private Scheduler scheduler;

    @GetMapping("/planes")
    public List<Airplane> getPlanes() {
        return airplaneRepository.findAll();
    }

    @GetMapping("/createTestAirplane")
    public String createTestAirPlane() {
        List<WayPoint> tmplist = new ArrayList<>();
        WayPoint wp1 = new WayPoint();
        wp1.setLatitude(100);
        wp1.setLongitude(100);
        wp1.setFlightSpeed(150);
        wp1.setFlightHeight(100);
        WayPoint wp2 = new WayPoint();
        wp2.setLatitude(130);
        wp2.setLongitude(400);
        wp2.setFlightSpeed(300);
        wp2.setFlightHeight(100);
        WayPoint wp3 = new WayPoint();
        wp3.setLatitude(-500);
        wp3.setLongitude(-800);
        wp3.setFlightSpeed(50);
        wp3.setFlightHeight(300);
        tmplist.add(wp1);
        tmplist.add(wp2);
        tmplist.add(wp3);
        Flight flight = new Flight();
        flight.setNumber(1L);
        flight.setWayPointList(tmplist);
        flight.setPassedPoints(new ArrayList<>());
        @Valid
        Airplane airplane = buildAirplane(1L, new AirplaneCharacteristics(300, 50, 5, 45),
                new TemporaryPoint(0, 0, 100, 0, 0), List.of(flight));
        airplaneRepository.save(airplane);
        log.info("Test Airplane was created ");
        return "test data created";
    }

    @PostMapping("/addPlane")
    public String addPlane(long id, AirplaneCharacteristics airplaneCharacteristics, TemporaryPoint temporaryPoint,
            List<Flight> flightList) {
        Airplane airplane = buildAirplane(id, airplaneCharacteristics, temporaryPoint, flightList);
        airplaneRepository.save(airplane);
        log.info("Plane " + airplane.getId() + " was saved");
        return "Airplane created";
    }

    @GetMapping("/startflights")
    public List<TemporaryPoint> startFlights() {
        log.info("Trying to initiate all actual flights");
        List<Airplane> airplanes = airplaneRepository.findAll();
        if (!airplanes.isEmpty()) {
            airplanes.forEach(airplane -> scheduler.initiateFlights(airplane));
        } else {
            log.error("Failed to start flights: No Airplanes found");
        }

        return scheduler.getAirplaneTemporaryPointMap().get(1L);
    }

    @GetMapping("/testJsonWriter")
    public void testJsonWriter() {
        List<Airplane> airplaneList = airplaneRepository.findAll();
        List<Flight> flightList = new ArrayList<>();
        for (Airplane airplane : airplaneList) {
            flightList.addAll(airplane.getFlights());
        }
        List<WayPoint> wayPoints = new ArrayList<>();
        for (Flight flight : flightList) {
            wayPoints.addAll(flight.getWayPointList());
        }
        List<TemporaryPoint> passedPoints = new ArrayList<>();
        for (Flight flight : flightList) {
            passedPoints.addAll(flight.getPassedPoints());
        }
        jsonOperator.writeListOfAirplanesInJson(new File("src/main/resources/airplanes.json"),airplaneList);
        jsonOperator.writeListOfFlightsInJson(new File("src/main/resources/flights.json"),flightList);
        jsonOperator.writeTemporaryPointListInJson(new File("src/main/resources/passedPoints.json"),passedPoints);
        jsonOperator.writeListOfWayPointsInJson(new File("src/main/resources/wayPoints.json"),wayPoints);
    }

    // Exception handler
    private TemporaryPoint buildTempPoint(@Valid TemporaryPoint temporaryPoint) {
        return temporaryPoint;
    }

    private AirplaneCharacteristics buildCharacteristics(@Valid AirplaneCharacteristics airplaneCharacteristics) {
        //Validation
        return airplaneCharacteristics;
    }

    private WayPoint buildWaypoint(@Valid WayPoint wayPoint) {
        return wayPoint;
    }

    private Flight buildFlight(@Valid Flight flight) {
        return flight;
    }

    private Airplane buildAirplane(@Valid Airplane airplane) {
        return airplane;
    }

    private Airplane buildAirplane(long id, @Valid AirplaneCharacteristics airplaneCharacteristics, @Valid TemporaryPoint temporaryPoint,
            List<Flight> flightList) {
        Airplane airplane = new Airplane();
        airplane.setId(id);
        airplane.setPosition(temporaryPoint);
        airplane.setAirplaneCharacteristics(airplaneCharacteristics);
        airplane.setFlights(flightList);

        log.info("Plane " + airplane.getId() + " was build");
        return buildAirplane(airplane);
    }
}
