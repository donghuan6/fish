package com.nine.leetcode;

/**
 * @author fan
 */
public class _2496 {

    public static void main(String[] args) {
//        String[] strs = {"1","01","001","0001"};
        String[] strs = {"alic3", "bob", "3", "4", "00000"};
        System.out.println(maximumValue(strs));
    }

    static int maximumValue(String[] strs) {
        int tmp = 0;
        for (String s : strs) {
            int n;
            try {
                n = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                n = s.length();
            }
            if (tmp < n) {
                tmp = n;
            }
        }
        return tmp;
    }
}
