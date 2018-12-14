package text_level;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaSyntaxHighlighter {
    private String line;
    private int x;
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
                    "switch", "synchronized", "this", "throw", "throws",
                    "transient", "try", "void", "volatile", "while"};


    public JavaSyntaxHighlighter() {
        this.x = 0;
        this.line = ""; // 保存当前处理的行

        int i = 0;
        int n = this.keywords.length;
        regexkeywords = new String[n];
        for (String w : this.keywords) {
            this.regexkeywords[i] += "(?<=\\s)";
            this.regexkeywords[i] += w;
            this.regexkeywords[i] += "(?=\\s)";
            i++;
        }
    }

    void highlight_note(String note) {
        //'高亮注释行'
        if (!note.equals("")) {  // note为空,表示行尾无注释
            this.line = this.line.replace(note, " [note] " + note + " [end] ");
        }
    }

    void highlight_string(int pos) {
        //'高亮字符串'
        String codeline = this.line.substring(0, pos); // 代码部分
        String noteline = this.line.substring(pos); // 不处理行尾注释

        Matcher m = Pattern.compile("((\\\".*?\\\")|(\\'.*?\\'))").matcher(codeline);
        int n;
        if (m.find()) {
            n = m.groupCount();
            if (n != 0) {
                String[] strlist = new String[n];
                for (int i = 0; i < n; i++) {
                    strlist[i] = m.group();
                }
                for (String string : strlist) {
                    codeline = codeline.replace(string, " [str] " + string + " [end] ");
                }
            }
        }
        //String[] strlist = re.findall("\".*?\"|\'.*?\'", codeline); // 搜索所有字符串
//        if strlist is not None {
//            for (String string : strlist) {
//                codeline = codeline.replace(string, " [str] " + string + " [end] ");
//            }
//        }
        this.line = codeline + noteline;
    }

    void highlight_keyword(int pos) {
        //'高亮关键字'
        String codeline = " " + this.line.substring(0, pos) + " ";
        String noteline = this.line.substring(pos);
        for (int i=0;i<keywords.length;i++) {
                codeline = codeline.replaceAll(regexkeywords[i], " [key] " + keywords[i] + " [end] ");
        }
//        for r, w in zip (this.regexkeywords, this.keywords){
//            codeline = re.sub(r, " [key] " + w + " [end] ", codeline);
//        }
        this.line = codeline + noteline;
    }

    void highlight_operator() {
        //'高亮运算符'
        char[] opr = {'=', '(', ')', '{', '}', '|', '+', '-', '*', '/', '<', '>'};
        for (char o : opr) {
            String temp = " [opr] " + o + " [end] ";
            String str_o = o + "";
            line = line.replace(str_o, temp);  // 未实现关于字符串内的运算符处理
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
        String note;  //注释
        note = "";

        //用正则模式/(/|\*)(.*)|\*(.*)|(.*)\*/$检查是否为单行注释(//xxx /*xxx*/ *xxx xxx*/)
        //如果是,highlight_note(self.line)高亮注释部分并返回
        Pattern p1 = Pattern.compile("(/(/|\\*))(.*)|(/\\*(.*))|(/(.*)\\*/)|(^\\*.*$)");
        Matcher find_note1 = p1.matcher(this.line.trim());
        //find_note = re.match('/(/|\*)(.*)|\*(.*)|(.*)\*/$', this.line.trim()); //查找单行注释
        if (find_note1.find()) {
            //处理单行注释;
            note = find_note1.group(0);
            this.highlight_note(note);
            System.out.println("0 "+note+" ");//test
            return this.line;
        }
        int pos = this.line.length();
        System.out.println("1 "+note+" "+pos);//test

        //用正则模式(?<=[){};])(.*)/(/|\*).*$查找行尾注释,
        //如果有,返回注释本身note,以及代码与注释的分割位置pos
        //例如:int i = 0; //*Note*, note="//*Note*" pos=10
        //如果没有,note="" pos=len(self.line)分割位置在行尾
        //也就是说:self.line[:pos]为代码,self.line[pos:]为行尾注释
        Matcher find_note2 = Pattern.compile(".*((?<=[){};])(.*)/(/|\\*).*$)").matcher(this.line.trim());//查找行尾注释

        if (find_note2.find()) {
            note = find_note2.group(1);  //标记行尾注释;
            pos = find_note2.start(1);//标记注释位置;//span() 返回一个元组包含匹配 (开始,结束) 的位置
            System.out.println("2 "+note+" "+pos);//test
        }

        this.highlight_note(note);  //处理行尾注释;
        this.highlight_keyword(pos); //处理关键字;
        this.highlight_string(pos); //处理字符串;
        this.highlight_operator();  //处理运算符;
        return this.line;  //返回处理好的行;
    }
}
