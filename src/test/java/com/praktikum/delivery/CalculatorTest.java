package com.praktikum.delivery;

import com.praktikum.delivery.entities.Dimension;
import com.praktikum.delivery.entities.Workload;
import com.praktikum.delivery.error.IncorrectDeliveryException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class CalculatorTest {
    private Calculator calculator;

    @BeforeMethod(alwaysRun = true)
    public void calculatorTestSetup() {
        calculator = new Calculator();
    }

    @DataProvider(name="deliveryParameters")
    public Object[][] deliveryParameters() {
        return new Object[][] {
                // distances
                { 1, Dimension.small, true, Workload.standard, 450 },
                { 2, Dimension.small, true, Workload.standard, 450 },
                { 3, Dimension.small, true, Workload.standard, 500 },
                { 9, Dimension.small, true, Workload.standard, 500 },
                { 10, Dimension.small, true, Workload.standard, 500 },
                { 11, Dimension.small, true, Workload.standard, 600 },
                { 29, Dimension.small, true, Workload.standard, 600 },
                { 30, Dimension.small, true, Workload.standard, 600 },
                { 31, Dimension.small, false, Workload.standard, 400 },
                { 40, Dimension.small, false, Workload.standard, 400 },
                // dimensions
                { 2, Dimension.large, true, Workload.standard, 550 },
                { 30, Dimension.large, true, Workload.standard, 700 },
                // workloads
                { 29, Dimension.small, true, Workload.increased, 720 },
                { 29, Dimension.small, true, Workload.high, 840 },
                { 29, Dimension.small, true, Workload.very_high, 960 },
                { 31, Dimension.small, false, Workload.increased, 480 },
                { 31, Dimension.small, false, Workload.high, 560 },
                { 31, Dimension.small, false, Workload.very_high, 640 },
                // fragility
                { 29, Dimension.small, false, Workload.very_high, 480 },
                // min available price
                { 9, Dimension.small, false, Workload.standard, 400 },
                { 9, Dimension.small, false, Workload.high, 400 },
                { 29, Dimension.small, false, Workload.standard, 400 },
                { 29, Dimension.small, false, Workload.high, 420 }
        };
    }

    @Test(dataProvider = "deliveryParameters")
    public void checkDeliveryPrice(int distance, Dimension dimension, boolean isFragile, Workload workload,
                                   double expectedPrice) throws Exception {
        double actualPrice = calculator.countDelivery(distance, dimension, isFragile, workload);
        assertThat("Expected price =" + expectedPrice + ", but actual = " + actualPrice, expectedPrice == actualPrice);
    }

    @DataProvider(name="incorrectDeliveryParameters")
    public Object[][] incorrectDeliveryParameters() {
        return new Object[][] {
                // fragile things for long distances
                { 31, Dimension.large, true, Workload.high },
                { 31, Dimension.small, true, Workload.standard },
                { 39, Dimension.large, true, Workload.high },
                // incorrect distances
                { 0, Dimension.small, true, Workload.standard },
                { -1, Dimension.small, true, Workload.standard }
        };
    }


    @Test(expectedExceptions = IncorrectDeliveryException.class, dataProvider = "incorrectDeliveryParameters")
    public void exceptionShouldBeThrownWithIncorrectParameters(int distance, Dimension dimension, boolean isFragile,
                                                               Workload workload) throws Exception {
        calculator.countDelivery(distance, dimension, isFragile, workload);
    }


}
