package text_level;

public class JavaSyntaxHighlighter {
    private String line;
    private int x;
    private String[] keywords;
    private String[] regexkeywords=
            {"abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "strictfp", "short", "static", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"};;

    JavaSyntaxHighlighter() {
        this.x = 0;
        this.line = ""; // 保存当前处理的行

        int i = 0;
        for (String w : this.keywords) {
            this.regexkeywords[i] += "(?<=\\s)";
            this.regexkeywords[i] += w;
            this.regexkeywords[i] += "(?=\\s)";
            i++;
        }
    }

    void highlight_note(String note) {
        //'高亮注释行'
        if (note != "") {  // note为空,表示行尾无注释
            this.line = this.line.replace(note, " [note] " + note + " [end] ")
        }
    }

    void highlight_string(int pos) {
        //'高亮字符串'
        String codeline = this.line[:pos]; // 代码部分
        String noteline = this.line[pos:]; // 不处理行尾注释
        String strlist = re.findall(r '\".*?\"|\'.*?\'', codeline); // 搜索所有字符串
        if strlist is not None {
            for (String string : strlist) {
                codeline = codeline.replace(string, " [str] " + string + " [end] ");
            }
        }
        this.line = codeline + noteline;
    }

    void highlight_keyword(int pos) {
        //'高亮关键字'
        String codeline = " " + this.line[:pos]+" ";
        String noteline = this.line[pos:];
        for r, w in zip (this.regexkeywords, this.keywords){
            codeline = re.sub(r, " [key] " + w + " [end] ", codeline);
        }
        this.line = codeline + noteline;
    }

    String highlight_operator() {
        //'高亮运算符'
        line = this.line;
        char[] opr = {'=', '(', ')', '{', '}', '|', '+', '-', '*', '/', '<', '>'};
        for (char o : opr) {
            String temp = " [opr] " + o + " [end] ";
            String str_o = o+"";
            line = line.replaceAll(str_o,temp);  // 未实现关于字符串内的运算符处理
        }
    }

    String translate(data="") {
        //'转换为html标签'
        name = ["note", "key", "str", "opr"];
        for n in name:
        data = data.replace(" [" + n + "] ", "<span class='" + n + "'>");
        data = data.replace(" [end] ", "</span>");
        return data;
    }

    String highlight(String line) {
        //'单行代码高亮'
        this.line = line;
        if (this.line.strip() == '') {
            return line;
        }  //空串不处理
        String note;  //注释
        note = " ";
        find_note = re.match(r '/(/|\*)(.*)|\*(.*)|(.*)\*/$', this.line.strip()); //查找单行注释;
        if (find_note) {
            //处理单行注释;
            note = find_note.group();
            this.highlight_note(note);
            return this.line;
        }
        int pos = len(this.line);
        find_note = re.search(r '(?<=[){};])(.*)/(/|\*).*$', this.line.strip());  //查找行尾注释;
        if (find_note) {
            note = find_note.group();  //标记行尾注释;
            pos = find_note.span()[0];//标记注释位置;
        }
        this.highlight_note(note);  //处理行尾注释;
        this.highlight_keyword(pos); //处理关键字;
        this.highlight_string(pos); //处理字符串;
        this.highlight_operator();  //处理运算符;
        return this.line;  //返回处理好的行;
    }
}
