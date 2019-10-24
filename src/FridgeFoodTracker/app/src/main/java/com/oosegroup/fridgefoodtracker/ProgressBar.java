package com.oosegroup.fridgefoodtracker;

import com.oosegroup.fridgefoodtracker.models.Item;

import java.util.Date;

public class ProgressBar {

    /** URL to database */
    public static final String TEN_PERCENT = "..........";

    /** URL to database */
    public static final String TWENTY_PERCENT = "/........";

    /** URL to database */
    public static final String THIRTY_PERCENT = "//.......";

    /** URL to database */
    public static final String FORTY_PERCENT = "///......";

    /** URL to database */
    public static final String FIFTY_PERCENT = "////.....";

    /** URL to database */
    public static final String SIXTY_PERCENT = "/////....";

    /** URL to database */
    public static final String SEVENTY_PERCENT = "//////...";

    /** URL to database */
    public static final String EIGHTY_PERCENT = "///////..";

    /** URL to database */
    public static final String NINETY_PERCENT = "////////.";

    /** URL to database */
    public static final String EXPIRED = "/////////";

    public static String getView(Item item) {
        Date expDate = item.getDateExpired();
        Date entDate = item.getDateEntered();
        Date currDate = new Date();
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
            return ProgressBar.EXPIRED;
        }
    }
}
