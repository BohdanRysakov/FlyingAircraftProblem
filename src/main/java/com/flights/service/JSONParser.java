package com.flights.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flights.models.Airplane;
import com.flights.models.Flight;
import com.flights.models.TemporaryPoint;
import com.flights.models.WayPoint;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class JSONParser {
    private final ObjectMapper objectMapper = new ObjectMapper();
    //This class could be used in case we have file with many objects(Like 10000 Way Points)

    public Airplane getAirplaneFromJson(File file) throws IOException {
        Airplane airplane = null;
        airplane = objectMapper.readValue(file, Airplane.class);
        return airplane;
    }

    public List<Airplane> getListOfAirplanesFromJson(File file) throws IOException {
        List<Airplane> list;

        list = objectMapper.readValue(file, new TypeReference<>() {
        });

        return list;
    }

    public WayPoint getWayPointFromJson(File file) throws IOException {
        return objectMapper.readValue(file, WayPoint.class);
    }

    public List<WayPoint> getListOfWayPointsFromJson(File file) throws IOException {
        List<WayPoint> list;
        list = objectMapper.readValue(file, new TypeReference<>() {
        });
        return list;
    }

    public Flight getFlightFromJson(File file) throws IOException {
        Flight flight = null;

        flight = objectMapper.readValue(file, Flight.class);

        return flight;

    }

    public List<Flight> getListOfFlightsFromJson(File file) throws IOException {
        List<Flight> list;

        list = objectMapper.readValue(file, new TypeReference<>() {
        });

        return list;
    }

    public void writeTemporaryPointInJson(File file, TemporaryPoint temporaryPoint) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            String json = objectMapper.writeValueAsString(temporaryPoint);
            writer.write(json);
        }
    }

    public void writeTemporaryPointListInJson(File file, List<TemporaryPoint> temporaryPointList) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            String json = objectMapper.writeValueAsString(temporaryPointList);
            writer.write(json);
        }
    }


    public void writeAirplaneInJson(File file, Airplane airplane) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            String json = objectMapper.writeValueAsString(airplane);
            writer.write(json);
        }
    }

    public void writeListOfAirplanesInJson(File file, List<Airplane> airplaneList) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            String json = objectMapper.writeValueAsString(airplaneList);
            writer.write(json);
        }
    }

    public void writeWayPointInJson(File file, WayPoint wayPoint) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            String json = objectMapper.writeValueAsString(wayPoint);
            writer.write(json);
        }
    }

    public void witeListOfWayPointsInJson(File file, List<WayPoint> wayPointList) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            String json = objectMapper.writeValueAsString(wayPointList);
            writer.write(json);
        }
    }

    public void writeFlightInJson(File file, Flight flight) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            String json = objectMapper.writeValueAsString(flight);
            writer.write(json);
        }

    }

    public void writeListOfFlightsInJson(File file, List<Flight> flightList) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            String json = objectMapper.writeValueAsString(flightList);
            writer.write(json);
        }
    }


}
