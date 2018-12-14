package text_level;

import org.testng.annotations.Test;

import java.io.*;
import java.util.Scanner;

/**
 * @author XieHao 谢昊
 * 这个类主要进行文本格式层面的转化
 */

public class Transformation {
    private String input_file ;
    private String output_file ;
    private JavaSyntaxHighlighter hlt = new JavaSyntaxHighlighter();//创建JavaSyntaxHighlighter类
    private String[] html_head = {"<!DOCTYPE html>",
            "<html>", "<head>",
            "<title>", "generated by JavaSyntaxHighlighter", "</title>",
            "<style type=\"text/css\">",
            "pre{font-family:\\'Microsoft Yahei\\';font-size:20;}",
            ".key{color:#000080;font-weight:bold;}",
            ".note{color:#808080;font-weight:bold;font-style:italic;}",
            ".str{color:#008000;font-weight:bold;}",
            ".opr{color:#DB380D;font-weight:bold;}",
            "</style>", "</head>", "<body>", "<pre>"};
    private String[] html_tail = {"</pre>", "</body>", "</html>"};

    public Transformation(String input_file, String output_file) throws Exception {

        this.input_file = input_file;
        this.output_file = output_file;
    }

    public boolean Main_Transformation() throws Exception {

        File readin_file = new File(input_file);// 指定要读取的文件
        File putout_file = new File(output_file);
        BufferedReader reader = null;
        PrintWriter output = null;
        try {//这里办正事= =\输出文件
            reader = new BufferedReader(new FileReader(readin_file));
            output = new PrintWriter(putout_file);

            //这里将html_head+\n先放到文件头部
            output.write(head_process());

            // 一次读入一行，直到读入null为文件结束
            String str;
            String data = new String();
            int n = 0;
            while ((str = reader.readLine()) != null) {
                if (n != 0) {
                    data += '\n';
                }
                data += process(str);
                n++;
            }
            output.write(hlt.translate(data)); //转换为html的<> 标签

            //这里放入html结尾格式
            output.write(tail_process());

            reader.close();//关闭输入
            output.close();//关闭输出

        } catch (IOException e) {//这里处理异常
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return true;
    }

    /**
     * 这里将html_head+\n先放到文件头部
     * @return
     */
    private String head_process(){
        String temp = new String();
        for (String str1 : html_head) {
            if (str1 != "<pre>") {
                temp += str1 + '\n';
            } else {
                temp += "<pre>";
            }
        }
        return temp;
    }

    /**
     *
     * @param string 输入中间的一行字符串,进行html转化和高亮处理
     * @return 返回处理后的字符串
     */
    private String process(String string) {
        String final_str = new String();

        string.replace("<", "&lt");//替换java中的“<”为html的显示符

        final_str = hlt.highlight(string);//进行代码高亮处理
        final_str += '\n';//最后每行换行

        return final_str;
    }

    private String tail_process(){
        String temp = new String();
        for (String str2 : html_tail) {
            if (!str2.equals("</html>")) {
                temp += str2 + '\n';
            } else {
                temp += "</html>";
            }
        }
        return temp;
    }
}
