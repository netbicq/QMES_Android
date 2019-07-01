package kkkj.android.revgoods.bean;

import java.util.ArrayList;
import java.util.List;

import kkkj.android.revgoods.R;

public class SwitchIcon {

    private static final int oneRed = R.drawable.ic_one_red_36dp;
    private static final int twoRed = R.drawable.ic_two_red_36dp;
    private static final int threeRed = R.drawable.ic_three_red_36dp;
    private static final int forRed = R.drawable.ic_for_red_36dp;
    private static final int fiveRed = R.drawable.ic_five_red_36dp;
    private static final int sixRed = R.drawable.ic_six_red_36dp;
    private static final int sevenRed = R.drawable.ic_seven_red_36dp;
    private static final int eightRed = R.drawable.ic_eight_red_36dp;

    private static final int oneGreen = R.drawable.ic_one_green_36dp;
    private static final int twoGreen = R.drawable.ic_two_green_36dp;
    private static final int threeGreen = R.drawable.ic_three_green_36dp;
    private static final int forGreen = R.drawable.ic_for_green_36dp;
    private static final int fiveGreen = R.drawable.ic_five_green_36dp;
    private static final int sixGreen = R.drawable.ic_six_green_36dp;
    private static final int sevenGreen = R.drawable.ic_seven_green_36dp;
    private static final int eightGreen = R.drawable.ic_eight_green_36dp;

    private static List<Integer> redIcon = new ArrayList<>();
    private static List<Integer> greenIcon = new ArrayList<>();


    public static List<Integer> getRedIcon() {
        redIcon.add(oneRed);
        redIcon.add(twoRed);
        redIcon.add(threeRed);
        redIcon.add(forRed);
        redIcon.add(fiveRed);
        redIcon.add(sixRed);
        redIcon.add(sevenRed);
        redIcon.add(eightRed);

        return redIcon;
    }

    public static List<Integer> getGreenIcon() {
        greenIcon.add(oneGreen);
        greenIcon.add(twoGreen);
        greenIcon.add(threeGreen);
        greenIcon.add(forGreen);
        greenIcon.add(fiveGreen);
        greenIcon.add(sixGreen);
        greenIcon.add(sevenGreen);
        greenIcon.add(eightGreen);

        return greenIcon;
    }
}
