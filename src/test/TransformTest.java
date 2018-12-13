package test;
import org.junit.jupiter.api.Test;
import text_level.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class TransformTest {

    @Test
    public void main_transform() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("F:\\study&work\\works\\java\\class_design\\curriculum design\\src\\text_level\\Transform.java"));
        PrintWriter pw = new PrintWriter("F:\\study&work\\works\\java\\class_design\\class_design\\src\\test\\b.html");
        pw.write("<html><body>\n");
        String str = null;
        while ((str = br.readLine()) != null) {
            pw.write(process(str));
        }
        pw.write("</body></html>");
        br.close();
        pw.close();
        System.out.println("ok");
    }

    @Test
    public String process(String s) {
        int len = s.length(); //定义字符串长度len
        StringBuilder sb = new StringBuilder();
        char[] ch = new char[len]; //创建长度为len的字符数组
        ch = s.toCharArray(); //转为字符数组 方便判断

        for (char i : ch) {
            switch (i) {
                case '&':
                    sb.append("&");
                    break;
                case ' ':
                    sb.append(" ");
                    break;
                case '<':
                    sb.append("<");
                    break;
                case '>':
                    sb.append(">");
                    break;
                case '"':
                    sb.append(" \" ");
                    break;
                case '\t':
                    sb.append("    ");
                    break;
                default:
                    sb.append(i);
            }
        }
        String string = sb.toString();
        string = string.replaceAll("public", "<b>public</b>");
        string = string.replaceAll("class", "<b>class</b>");
        string = string.replaceAll("static", "<b>static</b>");
        string = string.replaceAll("main", "<b>main</b>");
        string = string.replaceAll("void", "<b>void</b>");
        int n = string.lastIndexOf("\\/\\/");
        if (n != -1) {
            String temp = string.substring(n);
            string = string.replaceAll(temp, "<font color=green>" + temp + "</font>");
        }
        string += "<br/>\n";
        return string;
    }
}