package com.hardcopy.blechat.utils;

public class CheckNum {
    public static boolean isNumber(String str_num) {
        try {
            double str = Double.parseDouble(str_num);
        }
        catch(NumberFormatException e) {
            return false;
        }
        return true;
    }
}
