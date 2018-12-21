package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class simple_test_others {

    public static void main(String[] args) throws Exception {
        String input = "abc,";
        System.out.println(input);
        String[] output = input.split(",");
        for(String str:output)
            System.out.println(str);
        for(int i=0;i<output.length;i++){
            System.out.println(output[i]);
        }
    }
}
