package com.hardcopy.blechat.utils;

public class CheckNum {


    public static boolean isNumber(String str_num) {
        return  str_num.matches("^[0-9]*$");
    }

    public static boolean isNumberIncludeFloat(String str_num) {
        return  str_num.matches("^[+-]?\\d*(\\.?\\d*)$");
    }

    public static boolean isKorean(String str_num) {
        return  str_num.matches("^[가-힣]*$");
    }

    public static boolean isChar(String str_num) {
        return  str_num.matches("^[a-zA-Z]*$");
    }


}
