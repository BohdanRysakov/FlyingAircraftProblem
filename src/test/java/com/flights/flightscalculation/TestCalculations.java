package com.flights.flightscalculation;

import com.flights.utils.CalculationUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Service;

@Service
public class TestCalculations {


    @Test
    public void testPyfagorTheoremSuccess() {
        double[] cordsA = new double[]{0, 0, 0};
        double[] cordsB = new double[]{3, 4, 0};
        double[] cordsC = new double[]{10, 10, 10};
        double[] cordsD = new double[]{-5, -12, -84};
        Assert.assertEquals(5.0, CalculationUtil.pyfagorTheoremImpl(cordsA, cordsB), 0.000001);
        Assert.assertEquals(10 * Math.sqrt(3), CalculationUtil.pyfagorTheoremImpl(cordsA, cordsC), 0.000001);
        Assert.assertEquals(85, CalculationUtil.pyfagorTheoremImpl(cordsA, cordsD), 0.000001);
    }

    @Test
    public void testPyfagorTheoremFailure() {
        double[] cordsA = new double[]{0, 0, 0};
        double[] cordsB = new double[]{3, 4, 0};
        double[] cordsC = new double[]{10, 10, 10};
        double[] cordsD = new double[]{-5, -12, -84};
        Assert.assertEquals(10, CalculationUtil.pyfagorTheoremImpl(cordsA, cordsB), 0.000001);
        Assert.assertEquals(50, CalculationUtil.pyfagorTheoremImpl(cordsA, cordsC), 0.000001);
        Assert.assertEquals(20, CalculationUtil.pyfagorTheoremImpl(cordsA, cordsD), 0.000001);
    }

    @Test
    public void testAccelerationByTimeAndSeedSuccess() {
        // V = v0 + a*t => a = (v-v0)/t
        Assert.assertEquals(-3, CalculationUtil.getAccelerationByTimeAndSpeed(10, 35, 5), 0.000001);
        Assert.assertEquals(1, CalculationUtil.getAccelerationByTimeAndSpeed(10, 10, 20), 0.000001);
        Assert.assertEquals(4 / 0.3, CalculationUtil.getAccelerationByTimeAndSpeed(0.3, 40, 44), 0.000001);
    }

    @Test
    public void testAccelerationByTimeAndSeedFailure() {
        // V = v0 + a*t => a = (v-v0)/t
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            CalculationUtil.getAccelerationByTimeAndSpeed(0, 0, 0);
        });

    }

    @Test
    public void testDistanceInAccelerationMotionSuccess() {
        // S = v0*t + a*t^2/2
        Assert.assertEquals(227.5, CalculationUtil.getDistanceInAcceleratedMotion(15, 7, 5), 0.000001);
        Assert.assertEquals(162500000, CalculationUtil.getDistanceInAcceleratedMotion(0, 500, 1300), 0.000001);
        Assert.assertEquals(1464, CalculationUtil.getDistanceInAcceleratedMotion(400, 4, -17), 0.000001);
    }

    @Test
    public void testDistanceInAccelerationMotionFailure() {
        // S = v0*t + a*t^2/2
        Assert.assertEquals(null, CalculationUtil.getDistanceInAcceleratedMotion(0, 0, 0), 0.000001);
        Assert.assertEquals(0, CalculationUtil.getDistanceInAcceleratedMotion(0, 0, 0), 0.000001);
        Assert.assertEquals(1464, CalculationUtil.getDistanceInAcceleratedMotion(400, 4, -17), 0.000001);
    }

    @Test
    public void testTimeToReachDistanceSuccess() {
        // S = (v0+v1)*t/2
        Assert.assertEquals(4, CalculationUtil.getTimeToReachPoint(300, 0, 150), 0.000001);
        Assert.assertEquals(0.47058823529, CalculationUtil.getTimeToReachPoint(400, 500, 1200), 0.000001);
        Assert.assertEquals(0.8, CalculationUtil.getTimeToReachPoint(4, 2, 8), 0.000001);
        Assert.assertEquals(0.8, CalculationUtil.getTimeToReachPoint(4, 2, 8), 0.000001);
    }

    @Test
    public void testTimeToReachDistanceFailure() {
        // S = (v0+v1)*t/2
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            CalculationUtil.getTimeToReachPoint(300, -150, 150);
        });
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            CalculationUtil.getTimeToReachPoint(300, 0, 0);
        });
    }

    @Test
    public void testTimeToChangeHeightSuccess() {
        // T = (hMax-hMin)/(dT)
        Assert.assertEquals(3, CalculationUtil.getTimeToChangeHeight(300, 150, 50), 0.000001);
        Assert.assertEquals(6.6043956044, CalculationUtil.getTimeToChangeHeight(400, 1001, 91), 0.000001);
        Assert.assertEquals(370, CalculationUtil.getTimeToChangeHeight(4926, 4186, 2), 0.000001);
    }

    @Test
    public void testTimeToChangeHeightFailure() {
        // T = (hMax-hMin)/(dT)
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            CalculationUtil.getTimeToChangeHeight(300, 150, 0);
        });
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            CalculationUtil.getTimeToChangeHeight(300, 150, -3);
        });

    }

    @Test
    public void testDefinitionOfAngleToRotateSuccess() {
        double[] cordsA = new double[]{2, 5};
        double kA = 98;
        double[] cordsB = new double[]{-3, 1};
        double kB = 171;
        double[] cordsC = new double[]{7, -2};
        double kC = 14;
        double[] cordsD = new double[]{0, -3};
        double kD = 243;
        Assert.assertEquals(-119.65, CalculationUtil.getAngleToRotate(cordsB, cordsA, kB), 0.01);
        Assert.assertEquals(133.34, CalculationUtil.getAngleToRotate(cordsA, cordsB, kA), 0.01);
        Assert.assertEquals(-112.13, CalculationUtil.getAngleToRotate(cordsC, cordsD, kC), 0.01);
        Assert.assertEquals(-161.13, CalculationUtil.getAngleToRotate(cordsD, cordsC, kD), 0.01);
    }

    @Test
    public void testDefinitionOfAngleToRotateFailure() {
        double[] cordsA = new double[]{2, 5};
        double kA = 98;
        double[] cordsB = new double[]{-3, 1};
        double kB = 171;
        double[] cordsC = new double[]{7, -2};
        double kC = 14;
        double[] cordsD = new double[]{0, -3};
        double kD = 243;
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            CalculationUtil.getAngleToRotate(cordsB, cordsA, -456);
        });
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            CalculationUtil.getAngleToRotate(cordsB, cordsA, 2345);
        });

    }


}
