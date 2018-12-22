package text_level;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author XieHao 谢昊
 * @function 专门进行代码高亮部分处理, 找出需高亮部分, 并留下记号;但高亮的颜色和样式得到Transformation类中处理
 */
public class JavaSyntaxHighlighter {
    private int pos1 = 0, pos2 = 0;//记录注释起始和终止位置
    private String line;//记录输入的整行java源代码并处理
    private String codeline = "";//记录分离注释后的代码行
    private String noteline = "";//记录注释部分
    private boolean Multiline_comment = false;//判断多行注释时使用
    private String str_regex = "(?<!\\\\)(\")(.*?)(?<!\\\\)(\")|(?<!\\\\)(\')(.*?)(?<!\\\\)(\')";//判断字符串的正则表达式 match " 和 '(同时不匹配\"和\'中的"和',防止误判)

    private String[][] regexkeywords;//存储keywords加了正则表达式后的地方
    private String[][] keywords =//这里记录关键字1,2,3,4,5分别有哪些
            {
                    {"abstract", "assert", "break",
                            "case", "catch", "class", "const",
                            "continue", "default", "do", "else",
                            "enum", "extends", "for", "goto", "if", "implements", "import",
                            "instanceof", "interface", "native",
                            "new", "package", "private", "protected", "public",
                            "return", "strictfp", "static", "super","final","finally",
                            "switch", "synchronized", "this", "throws", "throws","void",
                            "transient", "try", "volatile", "while", "break", "null"
                    },//记录代码常用关键字

                    {"boolean", "char", "float", "int", "long", "short" ,"Integer","Scanner","FileWriter","IOException","Exception","InterruptedException","File","FileInputStream","BufferedReader",
                              "String", "double", "StringBuffer", "StringBuilder", "var", "byte", "Object","byte","Map","out","Matcher","Pattern","RandomAccessFile","PrintWriter",
                            "HashAttributeSet", "HashDocAttributeSet", "HashMap", "HashPrintJobAttributeSet", "HashPrintRequestAttributeSet", "HashPrintServiceAttributeSet",
                            "ImageIcon","URL","ProcessBuilder","Thread","Math","System","Throwable","NumberFormatException","CloneNotSupportedException",
                            "TextField","TextArea","Button","Label","Checkbox","Choice","List","Frame","Menubar","Menu","MenuItem","CheckboxMenuItem","Dialog",
                            "Filedialog","MouseMotionListener","PipedInputStream","PipedOutputStream","DataInputStream","DataOutputStream","ObjectInputStream","ObjectOutputStream",
                            "PushbackInputStream","PushbackReader","Socket","ServerSocket","InetAddress","DatagramPacket","DatagramSocket","Applet","Connection","DriverManager",
                            "SQLException","ResultSet","Statement","Jcomponent","JButton","JComboBox","JFileChooser","JInternalFrame","JLabel","JMenu","JMenuBar","JMenuItem",
                            "JPasswordField","JPopupMenu","JProgressBar","JRadioButton","JScrollBar","JScrollPane","JSplitPane","JTable","JTextArea","JTexPane","JToolBar","JToolTip",
                            "Queue","Set","ArrayList","LinkedList","HashSet","Arrays","Font","JPanel","Dimension","Toolkit","GraphicsEnvironment","Transformation","BoxLayout",
                            "JEditorPane","ItemEvent"
                    },//记录代码常用类型名

                    {"1"
                    },/*记录代码常用方法,这里原来设置了下面这么多..,然后我发现方法一般都是在 . 后面 括号前面.. 不是的就需要高级的匹配方式了 然而我没时间了..所以就这样吧
                    "toString", "length", "matcher","matches", "compile", "replace", "trim", "print", "println","containsKey","printf","scanf","next","close","in",
                    "replaceAll", "group", "equals", "start", "find", "continue", "extends", "append", "split", "substring","format","put","nextlnt","nextDouble",
                    "nextLine","hasNext","nextFloat","hasNextInt","hasNextDouble","hasNextFloat","hasNextByte","nextByte","nextBoolean","hasNextBoolean",
                    "end","charAt","printStackTrace","read","seek","writeBytes","getResource","currentThread","sleep","io","appendReplacement","appendTail",
                    "max","min","pow","cbrt","sqrt","abs","random","rint","round","valueOf","parseInt","parseDouble","parseLong","toCharArray","compareTo","currentTimeMillis",
                    "startsWith","endsWith","nextToken","hasMoreTokens","indexOf","setText","getText","setEchoChar","setEditable","addActionListener","removeActionListener",
                    "setText","addTextListener","removeTextListener","insert","replaceRange","getCaretPosition","setLabel","setBackground",
                    "getState","getLabel","add","remove","getSelectedIndex","addItemListener","removeItemListener","getSelectedItem","removeAll",
                    "setForeground","setFonts","setBounds","setLocation","setVisible","getBounds","getToolkit","setEnabled","setSize","getTitle","setResizable",
                    "setMenubar","getItem","getItemCount","addSeparator","setShortcut","setModal","getDirectory","getFile","getModifiers",
                    "getX","getY","getClickCount","addMouseListener","removeMouseListener","mousePressed","mouseReleased","mouseEntered","mouseExited","mouseClicked",
                    "addMouseMotionListener","mouseMoved","mouseDragged","setCursor","writeUTF","writeFloat","writeInt","writeLong","writeShort","writeDouble",
                    "skipByte","readLong","readBoolean","readByte","readChar","readFloat","readFully","write","counnect","unread","getAppletContext","showDocument",
                    "getByName","getHostName","getHostAddress","getLocalHost","receive","getPort","getAddress","getImage","getCodBase","drawImage","setIconImage",
                    "getWidth","getHeight","executeQuery","createStatement","newAudioClip","getAudioClip","play","loop","stop","getDocumenBase","createPlayer","controllerUpdate",
                    "prefetch","deallocate","getContentPane","setIcon","setHorizontalTextposition","setVerticalTextposition","setMnemonic","sort","clear","containsAll",
                    "getChars","getBytes","equalsIgnoreCase","regionMatches","lastIndexOf","concat","ensureCapacity","setLength","setCharAt","reverse","deleteCharAt",
                    "delete","reset","setFont","setPreferredSize","setAlignment","centerWindow","setDefaultCloseOperation","addItem","getLocalGraphicsEnvironment",
                    "getAvailableFontFamilyNames","getSource","setPage","getAbsolutePath","getSelectedFile","getStateChange","SELECTED"*/

                    {"false"},//这个我不想解释..
                    {"true"},
            };

    /**
     * 这是构造函数
     */
    public JavaSyntaxHighlighter() {
        this.line = ""; // 保存当前处理的行

        //下面把正则表达式加入到每个keywords旁边 给后面匹配key时用
        regexkeywords = new String[keywords.length][Math.max(keywords[1].length,keywords[2].length)];//因为关键字中数组1和2的长度最长,按他们中最长的长度来给regexkeywords分配空间
        for (int i = 0; i < keywords.length; i++) {
            int j = 0;
            for (String w : keywords[i]) {//这里添加关键字的正则匹配式子
                if (w != null) {
                    if (i == 2) {
                        regexkeywords[i][j] = "(?<!\\w)(?<=[\\.])";//关键字前面不能有字符,或者字符串标识符"和'
                        regexkeywords[i][j] += "(\\w+?)";//要匹配的关键字
                        regexkeywords[i][j] += "((?=\\.)|(?=\\()|(?=\\)))";//关键字后面不能有字符
                    } else {
                        regexkeywords[i][j] = "(?<!\\w)";//关键字前面不能有字符,或者字符串标识符"和'
                        regexkeywords[i][j] += w;//要匹配的关键字
                        regexkeywords[i][j] += "(?!\\w)";//关键字后面不能有字符
                    }
                }
                j++;
            }
        }
    }

    private void note_pos_find() {
        //查找注释部分位置并存到pos数组里
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

        //用正则模式/(/|\*)(.*)|\*(.*)|(.*)\*/$检查是否为单行注释(//xxx /*xxx*/ *xxx xxx*/)或者是@Test这种
        Pattern p1 = Pattern.compile("^((/([/*]))(.*)|(/\\*(.*))|(/(.*)\\*/)|(^\\*.*$))|(^@\\w+)");
        Matcher find_note1 = p1.matcher(this.line.trim());
        if (find_note1.find()) {
            //处理单行注释;
            pos1 = 0;
            return;
        }

        Matcher find_note2 = Pattern.compile("(?<=[){};=,+])(\\s*?)(//.*$)").matcher(this.line);//查找行尾注释
        if (find_note2.find()) {
            pos1 = find_note2.start(2);  //标记行尾注释;
        }
    }

    private void highlight_note() {
        //'高亮注释行'
        if (!noteline.equals("")) {  // note为空,表示行尾无注释
            Matcher m = Pattern.compile("^(\\*?)(@\\w+)").matcher(noteline.trim());
            if(m.find()){
                noteline = noteline.replaceFirst(m.group(2)," `noteplus` "+m.group(2)+" `end` ");//给注释中
            }
            noteline = noteline.replace(noteline, " `note` " + noteline + " `end` ");//最后把整个注释行贴上注释标识符
        }
    }

    /**
     * 下面的函数
     * 主要思路是用split和group,将字符串和非字符串各分入到两个不同的字符串数组里
     * 分别处理后再按非字符串+字符串+非字符串......这种顺序插回到codeline里
     * (试了一下,这种顺序匹配都会成功,比如字符串前面什么都没有,可能会先插字符串后面的,但是没有,
     * 可能和split特性有关,即如果第一个就是分隔符,那么存入数组的第一个将是一个空白字符)
     * 实现精确的处理
     * 后面的那些匹配原理差不多,就不解释了
     */
    private void highlight_string() {
        //高亮字符串
        String[] str_temp = codeline.split(str_regex);
        Matcher m = Pattern.compile(str_regex).matcher(codeline);
        int n = 0;
        while (m.find()) {//先找有多少个字符串
            n++;
        }
        String[] str = new String[n];//分配记录字符串的数组长度
        n = 0;m.reset();
        while (m.find()) {
            str[n++] = m.group();
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
        int n = 0;
        while (m.find()) {//先找有多少个字符串
            n++;
        }
        String[] str = new String[n];//分配记录字符串的数组长度
        n = 0;m.reset();
        while (m.find()) {
            str[n++] = m.group();
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
            codelineBuffer.append(temp);//添加处理好的非字符串
            if (n < str.length) {
                codelineBuffer.append(str[n++]);//添加字符串
            }
        }
        codeline = codelineBuffer.toString();
    }

    /**
     * replaceFirst是为了避免有的数字包含在别的数字里面,造成错误匹配
     * 所以先把第一次全匹配完了,再replace一遍把可能是重复的数字匹配上
     */
    private void highlight_numbers() {
        //高亮数字
        String[] str_temp = codeline.split(str_regex);
        Matcher m = Pattern.compile(str_regex).matcher(codeline);
        int n = 0;
        while (m.find()) {
            n++;
        }
        String[] str = new String[n];//记录字符串
        n = 0;m.reset();
        while (m.find()) {
            str[n++] = m.group();
        }
        n = 0;
        StringBuffer codelineBuffer = new StringBuffer();
        for (String temp : str_temp) {
            Matcher num = Pattern.compile("(?<!\\w)(?<![_])(\\d+)+(?!\\w)").matcher(temp);//匹配数字的部分
            while (num.find()) {
                temp = temp.replaceFirst(num.group()," `number` "+num.group()+" `end` ");
            }
            num.reset();
            while (num.find()) {
                temp = temp.replace(num.group()," `number` "+num.group()+" `end` ");
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
        String[] opr = {"=", "(", ")", "{", "}", "|", "+", "-", "*", "%", "/", "<", ">", "&", "|", "!", "~", "[", "]", ";", "!", ":", ".", ","};//存储要匹配的运算符
        Matcher m = Pattern.compile(str_regex).matcher(codeline);
        String[] str_temp = codeline.split(str_regex);//记录非字符串
        int n = 0;
        while (m.find()) {
            n++;
        }
        String[] str = new String[n];//记录字符串
        n = 0;m.reset();
        while (m.find()) {
            str[n++] = m.group();
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

    /**
     * 将已经加好标识,转化成html格式
     * @param data 输入已经加入标识符标记的字符串
     * @return 将标识符转为html格式的字符串
     */
    private String translate(String data) {
        //'转换为html标签'
        String[] name = {"note", "noteplus","key1", "key2", "key3", "key4", "key5", "str", "opr","number"};
        for (String n : name) {
            data = data.replaceAll("(?<!\")[ ]`" + n + "`[ ]", "<span class=\'" + n + "\'>");
        }
        data = data.replaceAll("[ ]`end`[ ](?!(\"))", "</span>");//用正则表达式,设置了标识符不能被"和' 所包含,避免用户字符串中就正好包含我们的标识符,产生误判
        return data;
    }

    /**
     * 处理文本文件中的每行,找到不同需高亮的地方并用html格式标记,最后全部转化成html格式,
     * 这是这个类主要的函数,调用这个类别的private函数完成处理
     * @param line 输入读入文件中的一行
     * @return 返回处理好的一行
     */
    String highlight(String line) {
        //'单行代码高亮'
        this.line = line;
        if (this.line.trim().equals("")) {
            return line;
        }  //空串不处理

        note_pos_find();//pos记录注释开始位置,避免对注释处理
        noteline = this.line.substring(pos1, pos2);//取出注释部分
        codeline = this.line.substring(0, pos1) + this.line.substring(pos2, line.length());//取出代码部分

        highlight_note();  //处理行尾注释
        highlight_numbers();//处理数字
        highlight_keyword(); //处理关键字
        highlight_operator();  //处理运算符
        highlight_string(); //处理字符串
        line = codeline + noteline;
        line = line.replace("<", "&lt;");//替换java中的“<”为html的显示符,防止html误判
        line = line.replace(">", "&gt;");//替换java中的“>”为html的显示符
        return translate(line);  //返回转化好标识符,处理好的行
    }
}
