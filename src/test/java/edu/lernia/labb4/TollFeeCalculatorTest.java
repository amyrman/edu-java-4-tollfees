package edu.lernia.labb4;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TollFeeCalculatorTest {

    @BeforeEach
    void setUp() {
        new TollFeeCalculator();
    }

    @Test
    void whenThereIsNoDates_thenThrowException() {
        // Test case 1: no dates
        LocalDateTime[] dates1 = {};
        int expected1 = 0;
        int actual1 = TollFeeCalculator.getTotalFeeCost(dates1);
        assertThat(actual1).isEqualTo(expected1);
    }

    @Test
    void whenThereIsOneDate_thenAddCorrectFee() {
        // Test case 2: one date
        LocalDateTime[] dates2 = { LocalDateTime.of(2023, 5, 18, 6, 15) };
        int expected2 = 8;
        int actual2 = TollFeeCalculator.getTotalFeeCost(dates2);
        assertThat(actual2).isEqualTo(expected2);
    }

    @Test
    void whenTwoDatesAreWithinOneHour_thenAddHighestFee() {
        // Test case 3: two dates within one hour
        LocalDateTime[] dates3 = {
                LocalDateTime.of(2023, 5, 18, 6, 15),
                LocalDateTime.of(2023, 5, 18, 6, 45)
            };
        int expected3 = 13;
        int actual3 = TollFeeCalculator.getTotalFeeCost(dates3);
        assertThat(actual3).isEqualTo(expected3);
    }

    @Test
    void whenTwoDatesAreMoreThanOneHourApart_thenAddSumOfBothFees() {
        // Test case 4: two dates more than one hour apart
        LocalDateTime[] dates4 = {
                LocalDateTime.of(2023, 5, 18, 6, 15),
                LocalDateTime.of(2023, 5, 18, 7, 30)
            };
        int expected4 = 26;
        int actual4 = TollFeeCalculator.getTotalFeeCost(dates4);
        assertThat(actual4).isEqualTo(expected4);
    }

    @Test
    void whenThereIsMultipleDates_thenAddCorrectFees() {
    // Test case 5: multiple dates with different fees
    LocalDateTime[] dates5 = {
            LocalDateTime.of(2023, 5, 18, 6, 15),   // 8  -- lower
            LocalDateTime.of(2023, 5, 18, 6, 30),   // 13 -- lower
            LocalDateTime.of(2023, 5, 18, 7, 15),   // 18 -- highest - add this
            LocalDateTime.of(2023, 5, 18, 10, 0),   // 8 -- both 8 - add either accord to Math.max logic
            LocalDateTime.of(2023, 5, 18, 10, 45),  // 8 -- both 8 - add either accord to Math.max logic
            LocalDateTime.of(2023, 5, 18, 15, 15),  // 13 -- add this
            LocalDateTime.of(2023, 5, 18, 16, 45),  // 18 <- highest
            LocalDateTime.of(2023, 5, 18, 17, 45),  // 13 <- lower
            LocalDateTime.of(2023, 5, 18, 18, 31)   // 0
    };
    int expected5 = 57;
    int actual5 = TollFeeCalculator.getTotalFeeCost(dates5);
    assertThat(actual5).isEqualTo(expected5);
}

    @ParameterizedTest
    @CsvSource({
            "2023-05-18T06:15, 8",
            "2023-05-18T06:45, 13",
            "2023-05-18T07:30, 18",
            "2023-05-18T08:15, 13",
            "2023-05-18T10:00, 8",
            "2023-05-18T15:15, 13",
            "2023-05-18T16:45, 18",
            "2023-05-18T17:30, 13",
            "2023-05-18T18:15, 8",
            "2023-05-19T00:00, 0"
    })
    void testGetTollFeePerPassing(LocalDateTime dateTime, int expectedFee) {
        assertThat(TollFeeCalculator.getTollFeePerPassing(dateTime)).isEqualTo(expectedFee);
    }

    @Test
    void testIsTollFreeDate() {

        LocalDateTime[] tollFreeDates = {
                LocalDateTime.of(2023, 5, 13, 10, 0), // Saturday
                LocalDateTime.of(2023, 5, 14, 10, 0), // Sunday
                LocalDateTime.of(2023, 7, 1, 10, 0) // July
        };

        LocalDateTime[] nonTollFreeDates = {
                LocalDateTime.of(2023, 5, 15, 10, 0), // Monday
                LocalDateTime.of(2023, 6, 1, 10, 0), // June
                LocalDateTime.of(2023, 8, 1, 10, 0) // August
        };

        for (LocalDateTime date : tollFreeDates) {
            assertThat(TollFeeCalculator.isTollFreeDate(date)).isTrue();
        }

        for (LocalDateTime date : nonTollFreeDates) {
            assertThat(TollFeeCalculator.isTollFreeDate(date)).isFalse();
        }
    }

    @Test
    void testMain() {

    }
}
