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
        String input = " \"<style type=\\\"text/css\\\">\",\"1\";\"2\"";
        System.out.println(input);
        Pattern p = Pattern.compile("(?<!\\\\)(\\\")(.*?)(?<!\\\\)(\\\")");
        Matcher m = p.matcher(input);
        while (m.find()) {
            System.out.println(m.group());
            System.out.println("起点为: "+m.start());
        }
    }
}

