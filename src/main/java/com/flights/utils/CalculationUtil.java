package com.flights.utils;

import com.flights.models.Airplane;
import com.flights.models.TemporaryPoint;
import com.flights.models.WayPoint;

public final class CalculationUtil {
    private CalculationUtil() {
    }

    //Returns distance between two points (Using pifagor theorem )
    public static double calculateDistance(TemporaryPoint temporaryPoint, WayPoint wayPoint) {
        double[] cords1 = new double[]{temporaryPoint.getLatitude(), temporaryPoint.getLongitude(), temporaryPoint.getFlightHeight()};
        double[] cords2 = new double[]{wayPoint.getLatitude(), wayPoint.getLongitude(), wayPoint.getFlightHeight()};
        return pyfagorTheoremImpl(cords1, cords2);
    }

    public static double pyfagorTheoremImpl(double[] cords1, double[] cords2) {
        return Math.sqrt(Math.pow(Math.abs((cords2[0] - cords1[0])), 2) +
                Math.pow(Math.abs((cords2[1] - cords1[1])), 2) + Math.pow(Math.abs((cords2[2] - cords1[2])), 2));
    }

    //Uses formula v1 = v0 + at
    public static double getAccelerationByTimeAndSpeed(double time, double initialSpeed, double desiredSpeed) {
        if (time == 0) {
            throw new IllegalArgumentException("dividing by 0 time =0");
        }
        return (desiredSpeed - initialSpeed) / time;
    }

    // Uses formula S = v0*t + a*t^2/2
    public static double getDistanceInAcceleratedMotion(double initialSpeed, double time, double acceleration) {
        if (time <= 0) {
            return 0;
        }
        return initialSpeed * time + (acceleration / 2) * Math.pow(time, 2);
    }

    //Using formula s = (v1+v2)*t/2 (Uniformly Accelerated Motion) finds t
    public static double getTimeToReachPoint(double distance, double initialSpeed, double desiredSpeed) {
        if (desiredSpeed + initialSpeed == 0) {
            throw new IllegalArgumentException("dividing by 0 initialSpeed + desiredSpeed = 0");
        }
        return 2 * distance / (desiredSpeed + initialSpeed);
    }

    //returns value of time needed to change current height on delta Height
    public static double getTimeToChangeHeight(Airplane airplane, WayPoint wayPoint) {
        return getTimeToChangeHeight(airplane.getPosition().getFlightHeight(), wayPoint.getFlightHeight(), airplane.getAirplaneCharacteristics().getDeltaHigh());
    }

    public static double getTimeToChangeHeight(double currentHeight, double desiredHeight, double deltaHeight) {
        if (deltaHeight <= 0) {
            throw new IllegalArgumentException("deltaHeight should be bigger than 0 " + "(" + deltaHeight + ")");
        }
        return (Math.abs(desiredHeight - currentHeight))
                / deltaHeight;
    }

    public static double getAngleToRotate(TemporaryPoint temporaryPoint, WayPoint wayPoint) {
        double[] initialCords = new double[]{temporaryPoint.getLatitude(), temporaryPoint.getLongitude()};
        double[] cordsToRotate = new double[]{wayPoint.getLatitude(), wayPoint.getLongitude()};
        double k = temporaryPoint.getCourseDegree();

        return getAngleToRotate(initialCords, cordsToRotate, k);

    }

    //Returns value of angle, on witch we need to rotate to watch direct on new point
    //      0
    // 270      90
    //     180
    public static double getAngleToRotate(double[] initialCords, double[] cordsToRotate, double k) {
        if (-360 > k || k > 360) {
            throw new IllegalArgumentException("Course degree is not in range [-360; 360] ");
        }
        double x = initialCords[0];
        double y = initialCords[1];
        double x1 = cordsToRotate[0];
        double y1 = cordsToRotate[1];
        double delta = 0;
        String rotation;
        double kx = Math.abs(Math.atan(Math.abs(y - y1) / Math.abs(x - x1)) * 180 / Math.PI);
        boolean secondOrThirdQuater = false;
        boolean thirdOrFourthQuater = false;

        //   2 | 1
        //  ------
        //   3 | 4

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
        return delta;
    }
}
