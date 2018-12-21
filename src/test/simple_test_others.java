package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class simple_test_others {

    public static void main(String[] args) throws Exception {
        String input = " \"<style type=\\\"text/css\\\">\",\"1\";\"2\"";
        System.out.println(input);
        int pos = 3;
        System.out.println(pos);
        String noteline = input.substring(0,pos);
        System.out.println(noteline);
    }
}
