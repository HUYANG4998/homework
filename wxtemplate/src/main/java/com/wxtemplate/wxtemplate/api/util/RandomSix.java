package com.wxtemplate.wxtemplate.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSix {
    public static String Num() {
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < 6; i++) {
            result = result * 10 + array[i];
        }
        if (String.valueOf(result).length() == 6) {
            return String.valueOf(result);
        } else {
            return Num();
        }
    }


}
