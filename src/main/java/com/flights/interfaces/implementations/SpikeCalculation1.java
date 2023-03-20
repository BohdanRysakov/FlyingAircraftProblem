package com.flights.interfaces.implementations;

import com.flights.interfaces.PlaneCalculation;
import com.flights.models.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SpikeCalculation1 implements PlaneCalculation {
    private Airplane airplane;
    private Flight flight;

    public List<TemporaryPoint> calculateRoute(Airplane airplane, Flight flight) {
        this.airplane = airplane;
        this.flight = flight;

        return calculateRoute(airplane.getAirplaneCharacteristics(), flight.getWayPointList());
    }

    @Override
    public List<TemporaryPoint> calculateRoute(AirplaneCharacteristics characteristics, List<WayPoint> wayPoints) {
        List<TemporaryPoint> temporaryPointList = new ArrayList<>();
        Iterator<WayPoint> wayPointIterator = wayPoints.iterator();

        while (wayPointIterator.hasNext()) {
            WayPoint wayPoint = wayPointIterator.next();
            assert routeBetweenTwoPoints(airplane.getPosition(), wayPoint) != null;
            temporaryPointList.addAll(routeBetweenTwoPoints(airplane.getPosition(), wayPoint));
        }

        return temporaryPointList;
    }

    //Возвращает новую высоту
    private void changeHeight(TemporaryPoint temporaryPoint, double heightDestination, double deltaHeight) {
        if (temporaryPoint.getFlightHeight() > heightDestination) {
            if (temporaryPoint.getFlightHeight() - heightDestination <= deltaHeight) {
                temporaryPoint.setFlightHeight(heightDestination);
            }
            temporaryPoint.setFlightHeight(temporaryPoint.getFlightHeight() - deltaHeight);
        } else {
            if (heightDestination - temporaryPoint.getFlightHeight() <= deltaHeight) {
                temporaryPoint.setFlightHeight(heightDestination);
            }
            temporaryPoint.setFlightHeight(temporaryPoint.getFlightHeight() + deltaHeight);
        }
    }


    // Двигает координаты на единицу времени, меняет сам объект


    List<TemporaryPoint> rotateAndSpeedUp(TemporaryPoint temporaryPoint, WayPoint wayPoint) {
        List<TemporaryPoint> temporaryPointList = new ArrayList<>();
        double velacity = temporaryPoint.getFlightSpeed();
        double[] initialCords = new double[]{temporaryPoint.getLatitude(), temporaryPoint.getLongitude()};
        double[] cordsToReach = new double[]{wayPoint.getLatitude(), wayPoint.getLongitude()};

        double[] cordsLengthFarFromCords = getFurtherCords(temporaryPoint.getCourseDegree(), initialCords,
                pyfagorTheoreme(initialCords, cordsToReach));

        double expectedSpeed = wayPoint.getFlightSpeed();
        double acceleration = airplane.getAirplaneCharacteristics().getDeltaSpeed();
        if (temporaryPoint.getFlightSpeed() > wayPoint.getFlightSpeed()) {
            acceleration = -airplane.getAirplaneCharacteristics().getDeltaSpeed();
        }
        double deltaRotation = airplane.getAirplaneCharacteristics().getDeltaCourseDegree();

        double lenght = 0;
        double expectedTime = (Math.abs(expectedSpeed - velacity)) / acceleration;
        //Равноускоренное движение
        double expectedLength = (Math.abs(Math.pow(expectedSpeed, 2) - Math.pow(velacity, 2))) / (2 * acceleration);
        double deltaTime = deltaRotation;
        double delayer = 1;// increases ten times amount of steps
        double timeStep = 1;
        double counter = expectedTime * delayer;
        while (counter > 0) {

            if (Math.abs(lenght - expectedLength) > 1) {
                //change 1
                lenght += velacity * (timeStep / delayer) + (acceleration * Math.pow((timeStep / delayer), 2)) / 2;
            } else {
                return temporaryPointList;
            }
            counter--;
            double[] tmp = new double[]{cordsLengthFarFromCords[0], cordsLengthFarFromCords[1]};

            cordsLengthFarFromCords = getRotatedCords(initialCords, deltaRotation / delayer, tmp);

            double[] newCOrds = findNewCords(initialCords, cordsLengthFarFromCords, lenght);
            temporaryPoint.setLatitude(newCOrds[0]);
            temporaryPoint.setLongitude(newCOrds[1]);
            temporaryPoint.setFlightSpeed(temporaryPoint.getFlightSpeed() + acceleration / delayer);
            changeHeight(temporaryPoint, wayPoint.getFlightHeight(), airplane.getAirplaneCharacteristics().getDeltaHigh());
            temporaryPointList.add(temporaryPoint);

            if (pyfagorTheoreme(newCOrds, cordsToReach) < 1) {
                return temporaryPointList;
            }
        }
        return temporaryPointList;
    }


    private List<TemporaryPoint> routeBetweenTwoPoints(TemporaryPoint temporaryPoint, WayPoint wayPoint) {
        List<TemporaryPoint> temporaryPointList = new ArrayList<>();
        //time to change speed
        double expectedTimeToReachNeededSpeed = (Math.abs(wayPoint.getFlightSpeed() - temporaryPoint.getFlightSpeed()))
                / airplane.getAirplaneCharacteristics().getDeltaSpeed();
        double acceleration = ((Math.abs(-temporaryPoint.getFlightSpeed() + wayPoint.getFlightSpeed()))
                / (-temporaryPoint.getFlightSpeed() + wayPoint.getFlightSpeed())) * airplane.getAirplaneCharacteristics().getDeltaSpeed();
        double expectedLength = ((temporaryPoint.getFlightSpeed() + wayPoint.getFlightSpeed())
                * expectedTimeToReachNeededSpeed) / 2;
        double distance = pyfagorTheoreme(new double[]{temporaryPoint.getLatitude(), temporaryPoint.getLongitude()}
                , new double[]{wayPoint.getLatitude(), wayPoint.getLongitude()});
        double secondDistance = distance - expectedLength;
        double timeToPassSecondDistance = secondDistance / wayPoint.getFlightSpeed();
        //1
        if (distance > expectedLength) {

            WayPoint wayPoint1 = new WayPoint();
            wayPoint1.setFlightHeight(wayPoint.getFlightHeight());
            wayPoint1.setFlightSpeed(wayPoint.getFlightSpeed());

            double[] newCords = findNewCords(new double[]{temporaryPoint.getLatitude(), temporaryPoint.getLongitude()},
                    new double[]{wayPoint.getLatitude(), wayPoint.getLongitude()}, expectedLength);
            wayPoint1.setLatitude(newCords[0]);
            wayPoint1.setLongitude(newCords[1]);
            temporaryPointList.addAll(rotateAndSpeedUp(temporaryPoint, wayPoint1)); // after this. Temporary point should
            // point to WayPoint Straight with needed speed
            assert (temporaryPoint.getFlightSpeed() == wayPoint.getFlightSpeed());

            temporaryPointList.addAll(moveForward(temporaryPoint, (int) timeToPassSecondDistance, wayPoint, temporaryPoint.getFlightSpeed()));
        } else if (distance == expectedLength) {
            return rotateAndSpeedUp(temporaryPoint, wayPoint);
        } else if (expectedLength > distance) {
            double distance1 = expectedLength - distance;
            double timeForDistance1 = 2 * distance1 / (temporaryPoint.getFlightSpeed() + wayPoint.getFlightSpeed());
            temporaryPointList.addAll(moveForwardWithAcceleration(temporaryPoint, (int) timeForDistance1, wayPoint
                    , temporaryPoint.getFlightSpeed(), airplane.getAirplaneCharacteristics().getDeltaSpeed()));
            temporaryPointList.addAll(rotateAndSpeedUp(temporaryPoint, wayPoint));
        }
        return rotateAndSpeedUp(temporaryPoint, wayPoint);
    }

    private double[] getFurtherCords(double angle, double[] initialCords, double length) {
        double[] furtherCords = new double[2];

        if (angle == 0) {
            furtherCords[0] = initialCords[0];
            furtherCords[1] = initialCords[1] + length;
        } else if (angle < 90) {
            furtherCords[0] = (length * Math.sin(Math.toRadians(angle)) + initialCords[0]);
            furtherCords[1] = (length * Math.cos(Math.toRadians(angle)) + initialCords[1]);
        } else if (angle == 90) {
            furtherCords[0] = initialCords[0] + length;
            furtherCords[1] = initialCords[1];
        } else if (angle < 180) {
            furtherCords[0] = (length * Math.sin(Math.toRadians(180 - angle)) + initialCords[0]);
            furtherCords[1] = (-length * Math.cos(Math.toRadians(180 - angle)) + initialCords[1]);
        } else if (angle == 180) {

            furtherCords[0] = initialCords[0];
            furtherCords[1] = initialCords[1] - length;

        } else if (angle < 270) {
            furtherCords[0] = (-length * Math.sin(Math.toRadians(angle - 180)) + initialCords[0]);
            furtherCords[1] = (-length * Math.cos(Math.toRadians(angle - 180)) + initialCords[1]);

        } else if (angle == 270) {

            furtherCords[0] = initialCords[0] - length;
            furtherCords[1] = initialCords[1];

        } else if (angle < 360) {
            furtherCords[0] = (-length * Math.sin(Math.toRadians(360 - angle)) + initialCords[0]);
            furtherCords[1] = (length * Math.cos(Math.toRadians(360 - angle)) + initialCords[1]);
        }

        return furtherCords;
    }

    private double getAngleToRotate(double[] initialCords, double k, double[] cordsToRotate) {


        double x = initialCords[0];
        double y = initialCords[1];
        double x1 = cordsToRotate[0];
        double y1 = cordsToRotate[1];
        double delta = 0;
        String rotation;
        double kx = Math.abs(Math.atan(Math.abs(y - y1) / Math.abs(x - x1)) * 180 / Math.PI);
        boolean secondOrThirdQuater = false;
        boolean thirdOrFourthQuater = false;
        if (x > x1) {
            // 2 or 3
            secondOrThirdQuater = true;
        }
        if (y > y1) {
            //3 or 4
            thirdOrFourthQuater = true;
        }
        if (!secondOrThirdQuater && !thirdOrFourthQuater) {
            //1
            kx = 90 - kx;
        } else if (!secondOrThirdQuater) {
            //4
            kx += 90;
        } else if (!thirdOrFourthQuater) {
            //2
            kx += 270;
        } else {
            //3
            kx = 270 - kx;
        }
        if (Math.abs(k - kx) < 180) {
            if (k > kx) {
                rotation = "counterclockwise";
            } else {
                rotation = "clockwise";
            }
            delta = Math.abs(k - kx);
        } else {
            if (k > kx) {
                rotation = "clockwise";
            } else {
                rotation = "counterclockwise";
            }
            delta = 360 - Math.max(kx, k) + Math.min(kx, k);
        }
        if (rotation.equals("counterclockwise")) {
            delta *= -1;
        }
        return Math.abs(delta);


    }


    List<TemporaryPoint> moveForwardWithAcceleration(TemporaryPoint temporaryPoint, int time,
            WayPoint wayPoint, double velocity, double acceleration) {
        double[] initialCords;
        double[] cordsLengthFarFromCords;
        ArrayList<TemporaryPoint> array = new ArrayList<>();
        if (temporaryPoint.getFlightSpeed() < wayPoint.getFlightSpeed()) {
            acceleration = Math.abs(acceleration);
        } else {
            acceleration = -Math.abs(acceleration);
        }

        double length = 0;
        while (time != 0) {
            initialCords = new double[]{temporaryPoint.getLatitude(), temporaryPoint.getLongitude()};
            cordsLengthFarFromCords = new double[]{wayPoint.getLatitude(), wayPoint.getLongitude()};
            length += velocity + acceleration;
            double[] newCords = findNewCords(initialCords, cordsLengthFarFromCords, length);
            temporaryPoint.setLatitude(newCords[0]);
            temporaryPoint.setLatitude(newCords[1]);
            array.add(temporaryPoint);
            time--;

        }
        return array;
    }

    List<TemporaryPoint> moveForward(TemporaryPoint temporaryPoint, int time,
            WayPoint wayPoint, double velocity) {
        double[] initialCords;
        double[] cordsLengthFarFromCords;
        ArrayList<TemporaryPoint> array = new ArrayList<>();
        double length = 0;
        while (time != 0) {
            initialCords = new double[]{temporaryPoint.getLatitude(), temporaryPoint.getLongitude()};
            cordsLengthFarFromCords = new double[]{wayPoint.getLatitude(), wayPoint.getLongitude()};
            length += velocity;
            double[] newCords = findNewCords(initialCords, cordsLengthFarFromCords, length);
            temporaryPoint.setLatitude(newCords[0]);
            temporaryPoint.setLatitude(newCords[1]);
            array.add(temporaryPoint);
            time--;

        }
        return array;

    }


    private double[] getRotatedCords(double[] rotationCenter, double angle, double[] cordsToRotate) {
        double[] rotatedCords = new double[2];
        double a = rotationCenter[0];
        double b = rotationCenter[1];
        double x = cordsToRotate[0];
        double y = cordsToRotate[1];
        rotatedCords[0] = (a + (x - a) * Math.cos(Math.toRadians(angle)) - (y - b) * Math.sin(Math.toRadians(angle)));

        rotatedCords[1] = (b + (y - b) * Math.cos(Math.toRadians(angle))
                + (x - a) * Math.sin(Math.toRadians(angle)));
        return rotatedCords;
    }

    private double[] findNewCords(double[] cords1, double[] cords2, double length) {
        double alpha = length / (pyfagorTheoreme(cords1, cords2) - length);
        double[] newCords = new double[2];
        newCords[0] = (cords1[0] + alpha * cords2[0]) / (1 + alpha);
        newCords[1] = (cords1[1] + alpha * cords2[1]) / (1 + alpha);
        return newCords;

    }

    private double pyfagorTheoreme(double[] cords1, double[] cords2) {
        return Math.sqrt(Math.pow(Math.abs((cords2[0] - cords1[0])), 2) +
                Math.pow(Math.abs((cords2[1] - cords1[1])), 2));
    }

    //проверка, что 3 точки лежат на 1 прямой
    private boolean isStraightLine(double[] a, double[] b, double[] c) {
        return ((a[0] - b[0]) * (c[1] - b[1]) - (c[0] - b[0]) * (a[1] - b[1])) < 0.001;


    }
}
