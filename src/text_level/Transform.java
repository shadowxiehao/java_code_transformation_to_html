package text_level;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * @author XieHao 谢昊
 * 这个类主要进行文本格式层面的转化
 */
public class Transform {
    String a,b = new String();

    public Transform(){
        ;
    }

    /**
     * 这个函数用于获取字符文本的所在地址和输出地址 并进行转化后 输出html到指定地址
     *
     * @throws Exception 读取文件时可能产生的exception
     */
    @Test
    public void main_transform() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("F:\\study&work\\works\\java\\class_design\\curriculum design\\src\\text_level\\Transform.java"));
        PrintWriter pw = new PrintWriter("F:\\study&work\\works\\java\\class_design\\curriculum design\\src\\test\\b.html");
        pw.write("<html><body>\n<pre><white-space: pre>");
        String str = null;
        while ((str = br.readLine()) != null) {
            pw.write(process(str));
        }
        pw.write("</body></html>");
        br.close();
        pw.close();
        System.out.println("ok");
    }

    /**
     * 读取java格式string 转化为 html格式 string
     *
     * @param s java格式的字符串
     * @return html格式的字符串
     */
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
