package com.flights.service;

import com.flights.models.Airplane;
import com.flights.models.Flight;
import com.flights.models.TemporaryPoint;
import com.flights.models.WayPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class JSONOperator {
    @Autowired
    private JSONParser jsonParser;


    public Airplane getAirplaneFromJson(File file) {
        Airplane airplane = null;
        try {
            airplane = jsonParser.getAirplaneFromJson(file);
            if (airplane == null) {
                log.error("File :" + file.getName() + " is empty or have incorrect data");
                throw new IllegalArgumentException("File :" + file.getName() + " is empty or have incorrect data");
            }
        } catch (IOException e) {
            log.error("File :" + file.getName() + " is empty or have incorrect data" + "\n" +
                    "Could not get List Of AirPlanes from  file: " + file.getName());
        }
        return airplane;
    }

    public List<Airplane> getListOfAirplanesFromJson(File file) {
        List<Airplane> airplanes = null;
        try {
            airplanes = jsonParser.getListOfAirplanesFromJson(file);
            if (airplanes == null) {
                log.error("File :" + file.getPath() + " is empty or have incorrect data");
                throw new IllegalArgumentException("File :" + file.getPath() + " is empty or have incorrect data");
            }
        } catch (IOException e) {
            log.error("File :" + file.getPath() + " is empty or have incorrect data");
            log.error("Could not get List Of AirPlanes from  file: " + file.getName());
        }
        return airplanes;
    }

    public WayPoint getWayPointFromJson(File file) {
        WayPoint wayPoint = null;
        try {
            wayPoint = jsonParser.getWayPointFromJson(file);
            if (wayPoint == null) {
                log.error("File :" + file.getPath() + " is empty or have incorrect data");
                throw new IllegalArgumentException("File :" + file.getPath() + " is empty or have incorrect data");
            }
        } catch (IOException e) {
            log.error("File :" + file.getPath() + " is empty or have incorrect data");
            log.error("Could not get WayPoint from file: " + file.getName());
        }
        return wayPoint;
    }

    public List<WayPoint> getListOfWayPointsFromJson(File file) {
        List<WayPoint> wayPoints = null;
        try {
            wayPoints = jsonParser.getListOfWayPointsFromJson(file);
            log.error("File :" + file.getPath() + " is empty or have incorrect data");

        } catch (IOException e) {
            log.error("File :" + file.getPath() + " is empty or have incorrect data");
            log.error("Could not get List Of WayPoints from  file: " + file.getName());
        }
        return wayPoints;
    }

    public Flight getFlightFromJson(File file) {
        Flight flight = null;
        try {
            flight = jsonParser.getFlightFromJson(file);
            if (flight == null) {
                log.error("File :" + file.getPath() + " is empty or have incorrect data");
                throw new IllegalArgumentException("File :" + file.getPath() + " is empty or have incorrect data");
            }
        } catch (IOException e) {
            log.error("Could not get Flight from  file: " + file.getName());
        }
        return flight;

    }

    public List<Flight> getListOfFlightsFromJson(File file) {
        List<Flight> flightList = null;
        try {
            flightList = jsonParser.getListOfFlightsFromJson(file);
            if (flightList == null) {
                log.error("File :" + file.getPath() + " is empty or have incorrect data");
                throw new IllegalArgumentException("File :" + file.getPath() + " is empty or have incorrect data");
            }
        } catch (IOException e) {
            log.error("Could not get List Of Flights from  file: " + file.getName());
        }
        return flightList;
    }

    public void writeTemporaryPointInJson(File file, TemporaryPoint temporaryPoint) {
        try {
            jsonParser.writeTemporaryPointInJson(file, temporaryPoint);
        } catch (IOException e) {
            log.error("Could not write Temporary Point from  file: " + file.getName());
        }
    }

    public void writeTemporaryPointListInJson(File file, List<TemporaryPoint> temporaryPointList) {
        try {
            jsonParser.writeTemporaryPointListInJson(file, temporaryPointList);
        } catch (IOException e) {
            log.error("Could not write List of Temporary Point from  file: " + file.getName());
        }
    }


    public void writeAirplaneInJson(File file, Airplane airplane) {
        try {
            jsonParser.writeAirplaneInJson(file, airplane);
        } catch (IOException e) {
            log.error("Could not write Airplane from  file: " + file.getName());
        }
    }

    public void writeListOfAirplanesInJson(File file, List<Airplane> airplaneList) {
        try {
            jsonParser.writeListOfAirplanesInJson(file, airplaneList);
        } catch (IOException e) {
            log.error("Could not write List of Airplanes from  file: " + file.getName());
        }
    }

    public void writeWayPointInJson(File file, WayPoint wayPoint) {
        try {
            jsonParser.writeWayPointInJson(file, wayPoint);
        } catch (IOException e) {
            log.error("Could not write Way Point from  file: " + file.getName());
        }
    }

    public void writeListOfWayPointsInJson(File file, List<WayPoint> wayPointList) {
        try {
            jsonParser.witeListOfWayPointsInJson(file, wayPointList);
        } catch (IOException e) {
            log.error("Could not write List of Way Points from  file: " + file.getName());
        }
    }

    public void writeFlightInJson(File file, Flight flight) {
        try {
            jsonParser.writeFlightInJson(file, flight);
        } catch (IOException e) {
            log.error("Could not write Flight from  file: " + file.getName());
        }
    }

    public void writeListOfFlightsInJson(File file, List<Flight> flightList) {
        try {
            jsonParser.writeListOfFlightsInJson(file, flightList);
        } catch (IOException e) {
            log.error("Could not write List of Flights from  file: " + file.getName());
        }
    }

}
