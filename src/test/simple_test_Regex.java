package test;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*java源码转换成HTML*/
public class simple_test_Regex {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Pattern p = Pattern.compile("\\w");
        Matcher m = p.matcher("aababbacbadbaeb");
        if (m.find()) {
            System.out.println(m.group());
            System.out.println("起点为: "+m.start());
            System.out.println(m.replaceAll("这里aeb被替换"));
        }
    }
}

