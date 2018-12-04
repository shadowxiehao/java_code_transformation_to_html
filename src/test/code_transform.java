package test;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

/*java源码转换成HTML*/
public class code_transform {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("F:\\study&work\\works\\java\\class_design\\class_design\\src\\test\\code_transform.java"));
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

    public static String process(String s) {
        StringBuilder sb = new StringBuilder();
        char[] ch = new char[50];
        ch = s.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            switch (ch[i]) {
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
                    sb.append(ch[i]);
            }
        }
        String string = sb.toString();
        string=string.replaceAll("public", "<b>public</b>");
        string = string.replaceAll("class", "<b>class</b>");
        string = string.replaceAll("static", "<b>static</b>");
        string = string.replaceAll("main", "<b>main</b>");
        string = string.replaceAll("void", "<b>void</b>");
        int n=string.lastIndexOf("\\/\\/");
        if(n!=-1){
            String temp=string.substring(n);
            string=string.replaceAll(temp, "<font color=green>"+temp+"</font>");
        }
        string+="<br/>\n";
        return string;

    }

}

