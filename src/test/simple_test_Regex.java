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
        //match " 和 '(同时不匹配\"和\'中的"和',防止误判)
        String n = "\" [key\" + (i + 1) + \"] \" + keywords[i][j] + \"\"";
        String regex = "(?<!\\\\)(\")(.*?)(?<!\\\\)(\")|(?<!\\\\)(\')(.*?)(?<!\\\\)(\')";
        String codeline = "String[] opr = {\"=\", \"(\", \")\", \"{\", \"}\", \"|\", \"+\", \"-\", \"*\", \"%\", \"/\", \"<\", \">\", \"&\", \"|\", \"!\", \"~\", \"[]\", \";\",\"!\",\":\",\".\", \",\"};";
        String[] str_temp = codeline.split(regex);
        int a=0;
        for(String str : str_temp){
            a++;
            System.out.println(a+" "+str);
        }
    }
}


