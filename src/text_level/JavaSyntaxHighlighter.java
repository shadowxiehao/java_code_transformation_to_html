package text_level;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaSyntaxHighlighter {
    private String line;
    String codeline="" ;
    String noteline="" ;
    private String[] regexkeywords;
    private String[] keywords =
            {"abstract", "assert", "boolean", "break", "byte",
                    "case", "catch", "char", "class", "const",
                    "continue", "default", "do", "double", "else",
                    "enum", "extends", "final", "finally", "float",
                    "for", "goto", "if", "implements", "import",
                    "instanceof", "int", "interface", "long", "native",
                    "new", "package", "private", "protected", "public",
                    "return", "strictfp", "short", "static", "super",
                    "switch", "synchronized", "this", "throws", "throws",
                    "transient", "try", "void", "volatile", "while"};


    public JavaSyntaxHighlighter() {
        this.line = ""; // 保存当前处理的行

        int i = 0;
        int n = this.keywords.length;
        regexkeywords = new String[n];
        for (String w : this.keywords) {//这里添加关键字的正则匹配式子
            this.regexkeywords[i] = "(?<!\\w)";//关键字前面不能有字符
            this.regexkeywords[i] += w;//要匹配的关键字
            this.regexkeywords[i] += "(?!\\w)+?";//关键字后面不能有字符
            i++;
        }
    }

    int note_pos_find(){
        int pos=line.length();

        //用正则模式/(/|\*)(.*)|\*(.*)|(.*)\*/$检查是否为单行注释(//xxx /*xxx*/ *xxx xxx*/)
        //如果是,highlight_note(self.line)高亮注释部分并返回
        Pattern p1 = Pattern.compile("^((/(/|\\*))(.*)|(/\\*(.*))|(/(.*)\\*/)|(^\\*.*$))");
        Matcher find_note1 = p1.matcher(this.line.trim());
        if (find_note1.find()) {
            //处理单行注释;
            pos=0;
            return pos;
        }

        Matcher find_note2 = Pattern.compile("((?<=[){};])(.*)/(/|\\*).*$)").matcher(this.line);//查找行尾注释
        if (find_note2.find()) {
            pos = find_note2.start();  //标记行尾注释;
        }
        codeline = this.line.substring(pos) ;
        return pos;
    }

    void highlight_note() {
        //'高亮注释行'
        if (!noteline.equals("")) {  // note为空,表示行尾无注释
            noteline = noteline.replace(noteline, " [note] " + noteline + " [end] ");
        }
    }

    void highlight_string() {
        //'高亮字符串'

        //match " 和 '(同时不匹配\"和\'中的"和',防止误判)
        Matcher m = Pattern.compile("(?<!\\\\)(\\\")(.*?)(?<!\\\\)(\\\")|(?<!\\\\)(\\\')(.*?)(?<!\\\\)(\\\')").matcher(codeline);
        while (m.find()) {
            String strlist = m.group();
            codeline = codeline.replace(strlist, " [str] " + strlist + " [end] ");
        }
    }

    void highlight_keyword() {
        //'高亮关键字'
        for (int i = 0; i < keywords.length; i++) {
            codeline = codeline.replaceAll(regexkeywords[i], " [key] " + keywords[i] + " [end] ");
        }
    }

    void highlight_operator() {
        //'高亮运算符'
        char[] opr = {'=', '(', ')', '{', '}', '|', '+', '-', '*', '/', '<', '>'};
        for (char o : opr) {
            String temp = " [opr] " + o + " [end] ";
            String str_o = o + "";
            codeline = codeline.replace(str_o, temp);  // 实现非注释的运算符高亮
        }
    }


    String translate(String data) {
        //'转换为html标签'
        String[] name = {"note", "key", "str", "opr"};
        for (String n : name) {
            data = data.replace(" [" + n + "] ", "<span class=\'" + n + "\'>");
        }
        data = data.replace(" [end] ", "</span>");
        return data;
    }

    String highlight(String line) {
        //'单行代码高亮'
        this.line = line;
        if (this.line.trim().equals("")) {
            return line;
        }  //空串不处理

        int pos = note_pos_find();
        codeline = this.line.substring(0, pos) ;
        noteline = this.line.substring(pos);

        this.highlight_note();  //处理行尾注释;
        this.highlight_keyword(); //处理关键字;
        this.highlight_string(); //处理字符串;
        this.highlight_operator();  //处理运算符;

        return codeline+noteline;  //返回处理好的行;
    }
}
