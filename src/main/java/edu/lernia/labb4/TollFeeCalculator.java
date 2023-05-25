package edu.lernia.labb4;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class TollFeeCalculator {

    public TollFeeCalculator(String inputFile) {
        try {
            Scanner sc = new Scanner(new File(inputFile));
            String[] dateStrings = sc.nextLine().split(", ");
            LocalDateTime[] dates = new LocalDateTime[dateStrings.length - 1];
            for (int i = 0; i < dates.length; i++) {
                dates[i] = LocalDateTime.parse(dateStrings[i], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
            System.out.println("The total fee for the inputfile is " + getTotalFeeCost(dates));
        } catch (IOException e) {
            System.err.println("Could not read file " + new File(inputFile).getAbsolutePath());
        }
    }

    public TollFeeCalculator() { // Add default constructor for initializing tests
    };

    public static int getTotalFeeCost(LocalDateTime[] dates) {
        if (dates == null || dates.length == 0) { // Add weird functionality for no dates
            return 0;
        }
    
        // Declarations
        int totalFee = 0;
        int highestFee = 0; // Add highestFee variable to store highest fee from several dates
        LocalDateTime intervalStart = dates[0];

        for (LocalDateTime date : dates) {
            System.out.println(date.toString());
            long diffInMinutes = intervalStart.until(date, ChronoUnit.MINUTES);

            if (diffInMinutes > 60) {
            totalFee += highestFee;
            intervalStart = date;
            highestFee = getTollFeePerPassing(date);
            } else {
            int fee = getTollFeePerPassing(date);
            highestFee = Math.max(highestFee, fee);
            }
        }
        totalFee += highestFee;
        return totalFee;
    }

    public static int getTollFeePerPassing(LocalDateTime date) {
        if (isTollFreeDate(date))
            return 0;
        int hour = date.getHour();
        int minute = date.getMinute();
        if (hour == 6 && minute >= 0 && minute <= 29)
            return 8;
        else if (hour == 6 && minute >= 30 && minute <= 59)
            return 13;
        else if (hour == 7 && minute >= 0 && minute <= 59)
            return 18;
        else if (hour == 8 && minute >= 0 && minute <= 29)
            return 13;
        // else if (hour >= 8 && hour <= 14 && minute >= 30 && minute <= 59) return 8; Bug
        else if ((hour == 8 && minute >= 30) || (hour > 8 && hour < 14) || (hour == 14 && minute <= 59))
            return 8;
        else if (hour == 15 && minute >= 0 && minute <= 29)
            return 13;
        else if (hour == 15 && minute >= 0 || hour == 16 && minute <= 59)
            return 18;
        else if (hour == 17 && minute >= 0 && minute <= 59)
            return 13;
        else if (hour == 18 && minute >= 0 && minute <= 29)
            return 8;
        else
            return 0;
    }

    public static boolean isTollFreeDate(LocalDateTime date) {
        return date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7
                || date.getMonth().getValue() == 7;
    }

    public static void main(String[] args) {
        new TollFeeCalculator("src/test/resources/Lab4.txt");
    }
}
