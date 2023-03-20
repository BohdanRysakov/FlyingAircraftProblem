package com.flights.interfaces.implementations;

import com.flights.interfaces.PlaneCalculation;
import com.flights.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.flights.utils.CalculationUtil.*;

@Component
@Slf4j
public class PlaneCalculationImpl implements PlaneCalculation {


    @Override
    public List<TemporaryPoint> calculateRoute(AirplaneCharacteristics characteristics, List<WayPoint> wayPoints) {
        return null;
    }

    @Override
    public List<TemporaryPoint> calculateRoute(Airplane airplane, Flight flight) {
        List<TemporaryPoint> temporaryPointList = new ArrayList<>();
        for (WayPoint wayPoint : flight.getWayPointList()) {
            temporaryPointList.addAll(calculateRoute(airplane, wayPoint));
        }
        List<TemporaryPoint> passedTmp = flight.getPassedPoints();
        passedTmp.addAll(temporaryPointList);
        flight.setPassedPoints(passedTmp);
        return temporaryPointList;
    }

    //Припущення Ми завжди наберемо висоту та швидкість
    private List<TemporaryPoint> calculateRoute(Airplane airplane, WayPoint wayPoint) {
        List<TemporaryPoint> temporaryPointList = new ArrayList<>();
        TemporaryPoint temporaryPoint = airplane.getPosition();

        temporaryPointList.addAll(rotation(airplane, wayPoint));// зараз дивимося прямо на точку

        double distance = calculateDistance(temporaryPoint, wayPoint);
        double currentSpeed = temporaryPoint.getFlightSpeed();
        double desiredSpeed = wayPoint.getFlightSpeed();
        double timeToReachDistance = getTimeToReachPoint(distance, currentSpeed, desiredSpeed);
        double acceleration = getAccelerationByTimeAndSpeed(timeToReachDistance, currentSpeed, wayPoint.getFlightSpeed());
        double timeToReachHeight = getTimeToChangeHeight(airplane, wayPoint);
        double timeToReachPoint = Math.min(timeToReachDistance, timeToReachHeight); //We should take minimum time from given 
        //and increase speed of another movement

        if (timeToReachPoint == 0 && timeToReachHeight == 0) {
            timeToReachPoint = timeToReachDistance;
        }

        temporaryPointList.addAll(movingToPoint(temporaryPoint, wayPoint, timeToReachPoint, acceleration, airplane.getId()));

        return temporaryPointList;
    }

    private List<TemporaryPoint> movingToPoint(TemporaryPoint temporaryPoint, WayPoint wayPoint,
            double timeToReachPoint, double acceleration, long id) {
        List<TemporaryPoint> temporaryPointList = new ArrayList<>();
        double initialSpeed = temporaryPoint.getFlightSpeed();
        double currentSpeed = initialSpeed;
        double timeSpent = 1;
        double distanceToMoveInOneSec;
        double travelTimeTmp = timeToReachPoint;

        while (timeToReachPoint > 0) {
            if (timeToReachPoint < 1) {
                distanceToMoveInOneSec = getDistanceInAcceleratedMotion(initialSpeed, travelTimeTmp, acceleration)
                        - getDistanceInAcceleratedMotion(initialSpeed,
                        travelTimeTmp - timeToReachPoint, acceleration);
                moveForwardOnDefiniteDistance(temporaryPoint, wayPoint, distanceToMoveInOneSec);
                currentSpeed += acceleration * timeToReachPoint;
                timeToReachPoint = 0;
            } else {
                distanceToMoveInOneSec = getDistanceInAcceleratedMotion(initialSpeed, timeSpent, acceleration) - getDistanceInAcceleratedMotion(initialSpeed, timeSpent - 1, acceleration);
                moveForwardOnDefiniteDistance(temporaryPoint, wayPoint, distanceToMoveInOneSec);
                currentSpeed += acceleration;
            }
            temporaryPoint.setFlightSpeed(currentSpeed);
            timeToReachPoint--;
            temporaryPointList.add(temporaryPoint.createTemporaryPointWithSameParams());
            timeSpent++;
        }
        log.info(id + " was on " + temporaryPoint.getLatitude() + "m :" + temporaryPoint.getLongitude() + " m, "
                + temporaryPoint.getFlightHeight() + "m Height " + temporaryPoint.getCourseDegree() + " degree");

        return temporaryPointList;
    }


    //Rotate airplane to new point direction
    private List<TemporaryPoint> rotation(Airplane airplane, WayPoint wayPoint) {
        List<TemporaryPoint> temporaryPointList = new ArrayList<>();
        TemporaryPoint temporaryPoint = airplane.getPosition();
        double angleToRotate = getAngleToRotate(airplane.getPosition(), wayPoint);
        double deltaAngle = airplane.getAirplaneCharacteristics().getDeltaCourseDegree();

        while (angleToRotate != 0) {
            double factor = 1;
            if (angleToRotate > 0) {
                factor = -1;
            }
            if (Math.abs(angleToRotate) < deltaAngle) {
                temporaryPoint.setCourseDegree(temporaryPoint.getCourseDegree() + angleToRotate);
                angleToRotate = 0;
            } else {
                angleToRotate += factor * airplane.getAirplaneCharacteristics().getDeltaCourseDegree();
                temporaryPoint.setCourseDegree(temporaryPoint.getCourseDegree() + deltaAngle);
            }
            temporaryPointList.add(temporaryPoint.createTemporaryPointWithSameParams());
        }
        log.info(airplane.getId() + " was on " + temporaryPoint.getLatitude() + "m :" + temporaryPoint.getLongitude() + " m, "
                + temporaryPoint.getFlightHeight() + "m Height" + temporaryPoint.getCourseDegree() + " degree");
        return temporaryPointList;
    }

    //On straight line: moves forward on <distance>
    private void moveForwardOnDefiniteDistance(TemporaryPoint temporaryPoint, WayPoint wayPoint, double distance) {
        double[] cordsToWhomMove = new double[]{wayPoint.getLatitude(), wayPoint.getLongitude(), wayPoint.getFlightHeight()};
        double[] cords1 = new double[]{temporaryPoint.getLatitude(), temporaryPoint.getLongitude(), temporaryPoint.getFlightHeight()};
        double alpha = distance / (calculateDistance(temporaryPoint, wayPoint) - distance);
        double[] newCords = new double[3];
        newCords[0] = (cords1[0] + alpha * cordsToWhomMove[0]) / (1 + alpha);
        newCords[1] = (cords1[1] + alpha * cordsToWhomMove[1]) / (1 + alpha);
        newCords[2] = (cords1[2] + alpha * cordsToWhomMove[2]) / (1 + alpha);
        if (calculateDistance(temporaryPoint, wayPoint) == distance) {
            newCords[0] = cordsToWhomMove[0];
            newCords[1] = cordsToWhomMove[1];
            newCords[2] = cordsToWhomMove[2];
        }
        temporaryPoint.setLatitude(newCords[0]);
        temporaryPoint.setLongitude(newCords[1]);
        temporaryPoint.setFlightHeight(newCords[2]);
    }
}
