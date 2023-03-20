package com.flights.interfaces;

import com.flights.interfaces.implementations.PlaneCalculationImpl;
import com.flights.models.Airplane;
import com.flights.models.Flight;
import com.flights.models.TemporaryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaneCalculationStrategy {
    //There we should choose witch implementation of PlaneCalculation to Use
    //Main target will be to reduce length of way from point-to-point
    //Some Flying vehicle could fly more flexible
    //e: in case of fast rotation we could do circle-like trajectory, but in case of
    //low rotation speed we cant
    //Also There we can provide type of trajectory like if we need, we could make machine
    //fly on big curve, but in city this could make trouble
    //otherwise we won't let drone fly by circle in countryside area

    @Autowired
    PlaneCalculation planeCalculationImpl = new PlaneCalculationImpl();

    public List<TemporaryPoint> startCalculation(Airplane airplane, Flight flight) {
        boolean someStatement = true;
        if (someStatement) {
            return planeCalculationImpl.calculateRoute(airplane, flight);
        }
        return null;
        //else { use another implementation}
    }

}
