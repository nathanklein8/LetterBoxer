package com.neklein3;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class UniqueCharComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {
        int uniqueCharsStr1 = countUniqueChars(str1);
        int uniqueCharsStr2 = countUniqueChars(str2);
        return Integer.compare(uniqueCharsStr2, uniqueCharsStr1);
    }

    private int countUniqueChars(String str) {
        Set<Character> uniqueChars = new HashSet<>();
        for (char c : str.toCharArray()) {
            uniqueChars.add(c);
        }
        return uniqueChars.size();
    }

}
