package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*此处测试正则表达式匹配是否正确*/
public class simple_test_Regex {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String[] input=new String[4];
        input[1] = " import";
         input[2] = " import ";
        input[3] = "import";
        input[4] = "import ";
        System.out.println();
        for(int i=0;i<4;i++) {
            Matcher m = Pattern.compile("(?<=\\s)*?.*").matcher(input[i]);
            while (m.find()) {
                //input = input.replaceAll("(?<=\\s)*?import(?=\\s)*?", " [key] ");
                System.out.println(i);
                System.out.println("起点为: " + m.start());
            }
        }
    }
}

