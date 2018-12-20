package text_level;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
@author XieHao
@function 专门进行代码高亮部分处理,找出需高亮部分,并留下记号;但高亮的颜色和样式得到Transformation类中处理
 */
public class JavaSyntaxHighlighter {
    private int pos1 = 0, pos2 = 0;//记录注释起始和终止位置
    private String line;//记录输入的整行java源代码并处理
    private String codeline = "";//记录分离注释后的代码行
    private String noteline = "";//记录注释部分
    private boolean Multiline_comment = false;//判断多行注释时使用
    private String str_regex = "(?<!\\\\)(\")(.*?)(?<!\\\\)(\")|(?<!\\\\)(\')(.*?)(?<!\\\\)(\')";//判断字符串的正则表达式 match " 和 '(同时不匹配\"和\'中的"和',防止误判)

    private String[][] regexkeywords;
    private String[][] keywords =
            {
                    {"abstract", "assert", "break",
                            "case", "catch", "class", "const",
                            "continue", "default", "do", "else",
                            "enum", "extends", "for", "goto", "if", "implements", "import",
                            "instanceof", "interface", "native",
                            "new", "package", "private", "protected", "public",
                            "return", "strictfp", "static", "super",
                            "switch", "synchronized", "this", "throws", "throws",
                            "transient", "try", "volatile", "while", "break", "null"
                    },

                    {"boolean", "char", "float", "int", "long", "short", "void",
                            "final", "finally", "String", "double", "StringBuffer", "StringBuilder", "var", "byte", "Object",
                            "HashAttributeSet", "HashDocAttributeSet", "HashMap", "HashPrintJobAttributeSet", "HashPrintRequestAttributeSet", "HashPrintServiceAttributeSet", "HashSet"
                    },
                    {"toString", "length", "matcher", "Pattern", "compile", "Matcher", "replace", "trim", "print", "println",
                            "replaceAll", "group", "equals", "start", "find", "continue", "extends", "append", "split", "substring",
                            "_BindingIteratorImplBase", "_BindingIteratorStub", "_NamingContextImplBase", "_NamingContextStub", "_PolicyStub", "_Remote_Stub", "_ServantActivatorStub", "_ServantLocatorStub"
                    },
                    {"false"},
                    {"true"},
            };

    /*
    构造函数
     */
    public JavaSyntaxHighlighter() {
        this.line = ""; // 保存当前处理的行
        regexkeywords = new String[keywords.length][keywords[0].length];
        for (int i = 0; i < keywords.length; i++) {
            int j = 0;
            for (String w : keywords[i]) {//这里添加关键字的正则匹配式子
                if (w != null) {
                    regexkeywords[i][j] = "(?<!\\w)";//关键字前面不能有字符,或者字符串标识符"和'
                    regexkeywords[i][j] += w;//要匹配的关键字
                    regexkeywords[i][j] += "(?!\\w)";//关键字后面不能有字符
                }
                j++;
            }
        }
    }

    private void note_pos_find() {
        pos1 = line.length();
        pos2 = line.length();//先假设没有注释
        //判断是否是多行注释
        Matcher pp1 = Pattern.compile("\\*/").matcher(this.line);
        Matcher pp2 = Pattern.compile("^/\\*").matcher(this.line.trim());
        Matcher pp3 = Pattern.compile("(?<=[){};])(\\s*?)(\\*/.*$)").matcher(this.line);
        if (!Multiline_comment) {//如果还不是多行注释,就找多行注释开始标志
            if (pp2.find()) {//查找开头有没有多行注释起始标志
                pos1 = 0;
                Multiline_comment = true;
            }
            if (pp3.find()) {//查找行尾有没有多行注释
                pos1 = pp3.start(2);
                Multiline_comment = true;
            }
        }
        if (Multiline_comment) {//如果还在多行注释中
            if (pos1 == line.length()) {
                pos1 = 0;
            }//如果注释起始地点未改变,仍为最后位置,代表前面没找到多行注释开始标志,也就代表这前面都是多行注释
            if (pp1.find()) {//找到结束标识符 */
                pos2 = pp1.end();
                Multiline_comment = false;
                return;
            } else return;
        }

        //用正则模式/(/|\*)(.*)|\*(.*)|(.*)\*/$检查是否为单行注释(//xxx /*xxx*/ *xxx xxx*/)
        Pattern p1 = Pattern.compile("^((/([/*]))(.*)|(/\\*(.*))|(/(.*)\\*/)|(^\\*.*$))");
        Matcher find_note1 = p1.matcher(this.line.trim());
        if (find_note1.find()) {
            //处理单行注释;
            pos1 = 0;
            return;
        }

        Matcher find_note2 = Pattern.compile("(?<=[){};])(\\s*?)(//.*$)").matcher(this.line);//查找行尾注释
        if (find_note2.find()) {
            pos1 = find_note2.start(2);  //标记行尾注释;
        }
    }

    private void highlight_note() {
        //'高亮注释行'
        if (!noteline.equals("")) {  // note为空,表示行尾无注释
            noteline = noteline.replace(noteline, " `note` " + noteline + " `end` ");
        }
    }

    private void highlight_string() {
        //'高亮字符串'

        String[] str_temp = codeline.split(str_regex);
        Matcher m = Pattern.compile(str_regex).matcher(codeline);
        Matcher m2 = Pattern.compile(str_regex).matcher(codeline);
        int n = 0;
        while (m.find()) {
            n++;
        }
        String[] str = new String[n];//记录字符串
        n = 0;
        while (m2.find()) {
            str[n++] = m2.group();
        }
        n = 0;
        StringBuffer codelineBuffer = new StringBuffer();
        for (String temp : str) {
            if (n < str_temp.length) {
                codelineBuffer.append(str_temp[n++]);
            }
            codelineBuffer.append(" `str` " + temp + " `end` ");
        }
        if (n < str_temp.length) {
            codelineBuffer.append(str_temp[n++]);
        }
        codeline = codelineBuffer.toString();
    }

    private void highlight_keyword() {
        //'高亮关键字'
        String[] str_temp = codeline.split(str_regex);
        Matcher m = Pattern.compile(str_regex).matcher(codeline);
        Matcher m2 = Pattern.compile(str_regex).matcher(codeline);
        int n = 0;
        while (m.find()) {
            n++;
        }
        String[] str = new String[n];//记录字符串
        n = 0;
        while (m2.find()) {
            str[n++] = m2.group();
        }
        n = 0;
        StringBuffer codelineBuffer = new StringBuffer();
        for (String temp : str_temp) {
            for (int i = 0; i < regexkeywords.length; i++) {
                for (int j = 0; j < regexkeywords[i].length; j++) {
                    if (regexkeywords[i][j] != null) {
                        temp = temp.replaceAll(regexkeywords[i][j], " `key" + (i + 1) + "` " + keywords[i][j] + " `end` ");
                    }
                }
            }
            codelineBuffer.append(temp);
            if (n < str.length) {
                codelineBuffer.append(str[n++]);
            }
        }
        codeline = codelineBuffer.toString();
    }

    private void highlight_numbers() {
        //高亮数字
        String[] str_temp = codeline.split(str_regex);
        Matcher m = Pattern.compile(str_regex).matcher(codeline);
        Matcher m2 = Pattern.compile(str_regex).matcher(codeline);
        int n = 0;
        while (m.find()) {
            n++;
        }
        String[] str = new String[n];//记录字符串
        n = 0;
        while (m2.find()) {
            str[n++] = m2.group();
        }
        n = 0;
        StringBuffer codelineBuffer = new StringBuffer();
        for (String temp : str_temp) {
            Matcher num = Pattern.compile("(?<!\\w)(?<![_])(\\d+)(?!\\w)").matcher(temp);
            while (num.find()) {
                temp = temp.replace(m.group()," `number` "+m.group()+" `end` ");
            }
            codelineBuffer.append(temp);
            if (n < str.length) {
                codelineBuffer.append(str[n++]);
            }
        }
        codeline = codelineBuffer.toString();
    }

    private void highlight_operator() {
        //'高亮运算符'
        String[] opr = {"=", "(", ")", "{", "}", "|", "+", "-", "*", "%", "/", "<", ">", "&", "|", "!", "~", "[", "]", ";", "!", ":", ".", ","};
        Matcher m = Pattern.compile(str_regex).matcher(codeline);
        Matcher m2 = Pattern.compile(str_regex).matcher(codeline);
        String[] str_temp = codeline.split(str_regex);//记录非字符串
        int n = 0;
        while (m.find()) {
            n++;
        }
        String[] str = new String[n];//记录字符串
        n = 0;
        while (m2.find()) {
            str[n++] = m2.group();
        }
        n = 0;
        StringBuffer codelineBuffer = new StringBuffer();
        for (String temp : str_temp) {
            for (String o : opr) {
                temp = temp.replace(o, " `opr` " + o + " `end` ");
            }
            codelineBuffer.append(temp);
            if (n < str.length) {
                codelineBuffer.append(str[n++]);
            }
        }
        codeline = codelineBuffer.toString();
    }

    public String translate(String data) {
        //'转换为html标签'
        String[] name = {"note", "key1", "key2", "key3", "key4", "key5", "str", "opr","number"};
        for (String n : name) {
            data = data.replaceAll("(?<!\")[ ]`" + n + "`[ ]", "<span class=\'" + n + "\'>");
        }
        data = data.replaceAll("[ ]`end`[ ](?!(\"))", "</span>");//用正则表达式,设置了标识符不能被"和' 所包含,避免用户字符串中就正好包含我们的标识符,产生误判
        return data;
    }

    String highlight(String line) {
        //'单行代码高亮'
        this.line = line;
        if (this.line.trim().equals("")) {
            return line;
        }  //空串不处理

        note_pos_find();//pos记录注释开始位置,避免对注释处理
        noteline = this.line.substring(pos1, pos2);
        codeline = this.line.substring(0, pos1) + this.line.substring(pos2, line.length());

        this.highlight_note();  //处理行尾注释
        this.highlight_keyword(); //处理关键字
        this.highlight_operator();  //处理运算符
        this.highlight_string(); //处理字符串
        return codeline + noteline;  //返回处理好的行
    }
}
