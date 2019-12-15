package com.oosegroup.fridgefoodtracker.models;

import java.util.Date;

/**
 * handles the populating of the progress bars
 */
public class ProgressBar {

    private static final int TEN_PERCENT = 10;

    private static final int TWENTY_PERCENT = 20;

    private static final int THIRTY_PERCENT = 30;

    private static final int FORTY_PERCENT = 40;

    private static final int FIFTY_PERCENT = 50;

    private static final int SIXTY_PERCENT = 60;

    private static final int SEVENTY_PERCENT = 70;

    private static final int EIGHTY_PERCENT = 80;

    private static final int NINETY_PERCENT = 90;

    private static final int FULL = 100;

    /**
     * returns the correct progress status of the bar
     * @param item the item to generate the expiration date
     * @return the percent as an integer out of 100
     */
    public static Integer getView(Item item) {
        Date expDate = item.getDateExpired();
        Date entDate = item.getDateEntered();
        Date currDate = new Date();

        if (currDate.compareTo(expDate) > 0) {
            return ProgressBar.FULL;
        }

        long timeElapsed = currDate.getTime() - entDate.getTime();
        long timeTotal = expDate.getTime() - entDate.getTime();

        double percentElapsed = (double) timeElapsed / timeTotal;

        if (percentElapsed < 0.1) {
            return ProgressBar.TEN_PERCENT;
        } else if (percentElapsed < 0.2) {
            return ProgressBar.TWENTY_PERCENT;
        } else if (percentElapsed < 0.3) {
            return ProgressBar.THIRTY_PERCENT;
        } else if (percentElapsed < 0.4) {
            return ProgressBar.FORTY_PERCENT;
        } else if (percentElapsed < 0.5) {
            return ProgressBar.FIFTY_PERCENT;
        } else if (percentElapsed < 0.6) {
            return ProgressBar.SIXTY_PERCENT;
        } else if (percentElapsed < 0.7) {
            return ProgressBar.SEVENTY_PERCENT;
        } else if (percentElapsed < 0.8) {
            return ProgressBar.EIGHTY_PERCENT;
        } else if (percentElapsed < 0.9) {
            return ProgressBar.NINETY_PERCENT;
        } else {
            return ProgressBar.FULL;
        }
    }
}
