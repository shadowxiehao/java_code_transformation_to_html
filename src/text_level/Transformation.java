package text_level;

import java.io.*;

/**
 * @author XieHao 谢昊
 * 这个类主要进行文本格式层面的转化(需要调用JavaSyntaxHighlighter类)
 * 将文本文件转为html格式
 * 并且将符合的不同代码部分高亮处理
 */
public class Transformation extends JavaSyntaxHighlighter{
    private String input_file ;
    private String output_file ;
    private String[] html_head = {"<!DOCTYPE html>",
            "<html>", "<head>","<meta charset=\"UTF-8\">",
            "<title>", "generated by JavaSyntaxHighlighter", "</title>",
            "<style type=\"text/css\">",
            "pre{font-family:Microsoft Yahei;font-size:15px;}",
            "pre{background:#fff9f9;}",
            ".key1{color:#00008F;font-weight:bold;}",
            ".key2{color:#1590EE;font-weight:bold;}",
            ".key3{color:#7D26CD;font-weight:bold;}",
            ".key4{color:#EE0000;font-weight:bold;}",
            ".key5{color:#7CFC00;font-weight:bold;}",
            ".note{color:#808080;font-weight:bold;font-style:italic;}",
            ".str{color:#EE7600;font-weight:bold;}",
            ".opr{color:#F000B2;font-weight:bolder;}",
            ".number{color:#45818e;font-weight:bold;}",
            ".noteplus{color:#b05001;font-weight:bold;font-style:italic;}",
            "</style>", "</head>", "<body>", "<pre>"};
    private String[] html_tail = {"</pre>", "</body>", "</html>"};//html尾文件,会添加到html的最后,作为标记格式设置的结束

    public String[] getHtml_tail() {
        return html_tail;
    }
    public String[] getHtml_head() {
        return html_head;
    }
    public void setHtml_tail(String[] html_tail) {
        this.html_tail = html_tail;
    }
    public void setHtml_head(String[] html_head) {
        this.html_head = html_head;
    }

    public Transformation(String input_file, String output_file) throws Exception {
        super();
        try {
            this.input_file = input_file;
            this.output_file = output_file;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 运行这里开始进行 文本格式 转 代码高亮的html格式
     * @throws Exception 文件读取可能错误
     * @return 如果成功运行,无错误,返回ture
     */
    public boolean Main_Transformation() throws Exception {
        File read_in_file = new File(input_file);// 指定要读取的文件
        File put_out_file = new File(output_file);
        BufferedReader reader = null;
        PrintWriter output ;
        try {//这里办正事= =\输出文件
            reader = new BufferedReader(new FileReader(read_in_file));
            output = new PrintWriter(put_out_file);

            //这里将html_head+\n先放到文件头部
            output.write(head_process());

            // 一次读入一行，直到读入null为文件结束
            String str;
            String data = "";
            int n = 0;
            while ((str = reader.readLine()) != null) {
                if (n != 0) {
                    data += '\n';
                }
                data += process(str);
                n++;
            }
            output.write(data); //转换为html的<> 标签

            //这里放入html结尾格式
            output.write(tail_process());

            reader.close();//关闭输入
            output.close();//关闭输出

        } catch (Exception e) {//这里处理异常
            e.printStackTrace();
            return false;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
            return true;
        }
    }

    /**
     * 处理html头文件,将其合并成一组字符串,除了头尾,每个都加/n换行
     * @return 含 html头文件 的字符串
     */
    private String head_process(){
        StringBuilder temp = new StringBuilder();
        for (String str1 : html_head) {
            if (!str1.equals("<pre>")) {
                temp.append(str1 + '\n');
            } else {
                temp.append("<pre>");
            }
        }
        return temp.toString();
    }

    /**
     * 将得到的一行字符串,转成高亮后的html格式
     * 最后加个换行符
     * @param string 输入中间的一行字符串,进行html转化和高亮处理
     * @return 返回处理后的字符串
     */
    private String process(String string) {
        String final_str ;
        final_str = highlight(string);//进行代码高亮处理
        final_str += '\n';//最后每行换行

        return final_str;
    }

    /**
     * 处理html尾部文件格式,将其合并成一组字符串,除了头尾,中间以\n换行分隔
     * @return 含 html尾部文件格式 的字符串
     */
    private String tail_process(){
        StringBuilder temp = new StringBuilder();
        for (String str2 : html_tail) {
            if (!str2.equals("</html>")) {
                temp.append( str2 + '\n');
            } else {
                temp.append("</html>");
            }
        }
        return temp.toString();
    }
}
