package GUI_start;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import text_level.Transformation;

import java.io.*;

/**
 * @author Lynn 武丞 这个类完成图形界面的设计
 */

public class GUI extends JFrame implements ActionListener {
	String str1, str2, strOutPut, strTemp;
	static JEditorPane jEditorPane1 = new JEditorPane();// 用于显示java文件预览
	static JEditorPane jEditorPane2 = new JEditorPane();// 用于显示html文件预览
	static JButton b1 = new JButton("...");// 测试按钮，点击之后选择本地文件进行浏览，预览放在jEditorPane1中
	static JButton b2 = new JButton("...");
	static JButton b3 = new JButton("开始转换");
	static JButton b4 = new JButton("关于我们");
	static JTextField t1 = new JTextField(30);// 存放源文件地址
	static JTextField t2 = new JTextField(29);// 存放目标文件地址
	static JComboBox cbxFont = new JComboBox();
	static JComboBox cbxFontSize = new JComboBox();
	static JComboBox cbxKeyWords1 = new JComboBox();
	static JComboBox cbxKeyWords2 = new JComboBox();
	static JComboBox cbxKeyWords3 = new JComboBox();
	static JComboBox cbxNote= new JComboBox();
	static JComboBox cbxString = new JComboBox();
	static JComboBox cbxOpr = new JComboBox();

	public static void main(String[] args) {

		// 主要框架：Frame以及Frame上BoxLayout的Container
		GUI frame = new GUI("java文件格式转换器");
		Container container = new Container();// 由于整体要采用盒式布局，而frame无法设置为盒式布局，所以选择以contianer作为整体画布
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		// 第一部分：panel1 -- 预览区域
		JScrollPane scrollpane1 = new JScrollPane(jEditorPane1);// 初始化滚动条ScrollPane
		scrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JScrollPane scrollpane2 = new JScrollPane(jEditorPane2);
		scrollpane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JPanel panel1 = new JPanel(new GridLayout(1, 0, 3, 3));
		panel1.setBackground(Color.WHITE);
		panel1.setPreferredSize(new Dimension(0, 400));
		JPanel panel1_1 = new JPanel();
		panel1_1.setBackground(Color.WHITE);
		panel1_1.setLayout(new BoxLayout(panel1_1, BoxLayout.Y_AXIS));

		JPanel panel1_2 = new JPanel();
		panel1_2.setBackground(Color.WHITE);
		panel1_2.setLayout(new BoxLayout(panel1_2, BoxLayout.Y_AXIS));

		JLabel jL1_1 = new JLabel("转换前(java文件)：");
		jL1_1.setFont(new Font("楷体", 1, 17));
		jL1_1.setPreferredSize(new Dimension(20, 20));
		panel1_1.add(jL1_1);
		panel1_1.add(scrollpane1);
		JLabel jL1_2 = new JLabel("转换后(html文件)：");
		jL1_2.setFont(new Font("楷体", 1, 17));
		jL1_2.setPreferredSize(new Dimension(20, 20));
		panel1_2.add(jL1_2);
		panel1_2.add(scrollpane2);
		panel1.add(panel1_1);
		panel1.add(panel1_2);

		// 第二部分：panel2 -- 地址选择区域与关键字颜色选择区域
		JPanel panel2 = new JPanel(new GridLayout(1, 0));// panel2存放地址选择部分
		panel2.setBackground(Color.WHITE);
		panel2.setPreferredSize(new Dimension(0, -120));
		FlowLayout fc = new FlowLayout();
		fc.setAlignment(FlowLayout.CENTER);
		JPanel panel2_1 = new JPanel(fc);
		panel2_1.setBackground(Color.WHITE);
		JPanel panel2_2 = new JPanel(fc);
		panel2_2.setBackground(Color.WHITE);

		JLabel jL2_1 = new JLabel("      源文件地址：");
		jL2_1.setFont(new Font("楷体", 1, 15));
		panel2_1.add(jL2_1);
		t1.setEditable(false);
		t1.setPreferredSize(new Dimension(400, 20));
		b1.setPreferredSize(new Dimension(35, 20));
		panel2_1.add(t1);
		panel2_1.add(b1);
		JLabel jL2_2 = new JLabel("   保存文件地址：");
		jL2_2.setFont(new Font("楷体", 1, 15));
		panel2_2.add(jL2_2);
		t2.setEditable(false);
		t2.setPreferredSize(new Dimension(400, 20));
		b2.setPreferredSize(new Dimension(35, 20));
		panel2_2.add(t2);
		panel2_2.add(b2);
		panel2.add(panel2_1);
		panel2.add(panel2_2);
		
		initKeyWords();
		JLabel space1 = new JLabel("                                                                                                                                                            ");
		
		JLabel kW1 = new JLabel("       关键字1：");
		kW1.setFont(new Font("楷体", 1, 13));
		JLabel kW2 = new JLabel("  关键字2：");
		kW2.setFont(new Font("楷体", 1, 13));
		JLabel kW3 = new JLabel("  关键字3：");
		kW3.setFont(new Font("楷体", 1, 13));
		cbxKeyWords1.setPreferredSize(new Dimension(90,20));
		cbxKeyWords2.setPreferredSize(new Dimension(90,20));
		cbxKeyWords3.setPreferredSize(new Dimension(90,20));
		panel2_1.add(space1);
		panel2_1.add(kW1);
		panel2_1.add(cbxKeyWords1);
		panel2_1.add(kW2);
		panel2_1.add(cbxKeyWords2);
		panel2_1.add(kW3);
		panel2_1.add(cbxKeyWords3);
		
		JLabel space2 = new JLabel("                                                                                                                                                                               ");
		
		JLabel nOTE = new JLabel("   注释：");
		nOTE.setFont(new Font("楷体", 1, 13));
		JLabel sTring = new JLabel("    字符串：");
		sTring.setFont(new Font("楷体", 1, 13));
		JLabel oPR = new JLabel("    运算符：");
		oPR.setFont(new Font("楷体", 1, 13));
		cbxNote.setPreferredSize(new Dimension(90,20));
		cbxString.setPreferredSize(new Dimension(90,20));
		cbxOpr.setPreferredSize(new Dimension(90,20));
		panel2_2.add(space2);
		panel2_2.add(nOTE);
		panel2_2.add(cbxNote);
		panel2_2.add(sTring);
		panel2_2.add(cbxString);
		panel2_2.add(oPR);
		panel2_2.add(cbxOpr);

		// 第三部分：panel3 -- 字体与字号下拉框与开始按钮
		JPanel panel3 = new JPanel(new GridLayout(1, 0));
		panel3.setPreferredSize(new Dimension(0, -80));
		panel3.setBackground(Color.WHITE);
		
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		JPanel panel3_1 = new JPanel(fl);
		panel3_1.setBackground(Color.WHITE);
		JPanel panel3_2 = new JPanel(fc);
		panel3_2.setBackground(Color.WHITE);
		JPanel panel3_1_1 = new JPanel(fl);
		panel3_1_1.setBackground(Color.WHITE);
		JPanel panel3_1_2 = new JPanel(fl);
		panel3_1_2.setBackground(Color.WHITE);
		
		cbxFont.setPreferredSize(new Dimension(375,20));
		cbxFontSize.setPreferredSize(new Dimension(50,20));
		JLabel fontLabel = new JLabel("      字体名称：  ");
		fontLabel.setFont(new Font("楷体", 1, 15));
		JLabel fontSizeLabel = new JLabel("      字体大小：  ");
		fontSizeLabel.setFont(new Font("楷体", 1, 15));
		
		initFonts();
		panel3.add(panel3_1);
		panel3.add(panel3_2);
		panel3_1.add(panel3_1_1);
		panel3_1.add(panel3_1_2);
		
		panel3_1_1.add(fontLabel);
		panel3_1_1.add(cbxFont);
		panel3_1_2.add(fontSizeLabel);
		panel3_1_2.add(cbxFontSize);
		
		b3.setPreferredSize(new Dimension(120,50));
		b3.setFont(new Font("楷体", 1, 15));
		b4.setPreferredSize(new Dimension(120,50));
		b4.setFont(new Font("楷体", 1, 15));
		panel3_2.add(new JLabel("              "));
		panel3_2.add(b3);
		panel3_2.add(new JLabel("              "));
		panel3_2.add(b4);

		// 第四部分：panel4 -- 操作提示
		JPanel panel4 = new JPanel(fc);
		panel4.setPreferredSize(new Dimension(0, -100));
		panel4.setBackground(Color.WHITE);
		
		JLabel tip0 = new JLabel("                            使用说明                            ");
		tip0.setFont(new Font("楷体", 1, 14));
		JLabel tip1 = new JLabel("                            1: 选择源文件地址时请选择目标文件，选择保存文件地址时请选择文件夹。                            ");
		tip1.setFont(new Font("楷体", 1, 14));
		JLabel tip2 = new JLabel("2: 进行转换前请选择每类关键字需要配置的颜色");
		tip2.setFont(new Font("楷体", 1, 14));
		JLabel tip3 = new JLabel("    3: 关键字1：public、abstract、static等……  关键字2：int、float、char等……  关键字3：toString、length、matcher等……    ");
		tip3.setFont(new Font("楷体", 1, 14));
		JLabel tip4 = new JLabel("4: 生成的目标文件名为\"Result.html\"");
		tip4.setFont(new Font("楷体", 1, 14));
		
		panel4.add(tip0);
		panel4.add(tip1);
		panel4.add(tip2);
		panel4.add(tip3);
		panel4.add(tip4);

		container.add(panel1);// container上添加部件
		container.add(panel2);
		container.add(panel3);
		container.add(panel4);
		
		frame.add(container);

		jEditorPane1.setEditable(false);// 设置预览框不可修改，这样就算鼠标点上去，也不会出现光标
		jEditorPane2.setEditable(false);
		frame.setVisible(true);
	}

	public GUI(String sTitle) {
		super(sTitle);

		b1.addActionListener(this);// 设置按钮的动作监听
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);

		setSize(1200, 1000);// 整体frame大小
		centerWindow();// 用于使主窗口位于屏幕中央显示

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void centerWindow() {// 用于使主窗口位于屏幕中央显示
		Toolkit tk = getToolkit();
		Dimension dm = tk.getScreenSize();

		setLocation((int) (dm.getWidth() - getWidth()) / 2, (int) (dm.getHeight() - getHeight()) / 2);

	}
	
	public static void initKeyWords() {
		String[] colors = {
				"#00008F 深蓝","#1590EE 浅蓝","#7D26CD 紫色","#EE0000 红色","#7CFC00 草绿","#808080 灰色","#EE7600 橙色","#FF00D6 粉色"
		};
		
		for (int i = 0; i < 8; i++) {
			cbxKeyWords1.addItem(colors[i]);
		}
		for (int i = 1; i < 8; i++) {
			cbxKeyWords2.addItem(colors[i]);
		}
		cbxKeyWords2.addItem(colors[0]);
		for (int i = 2; i < 8; i++) {
			cbxKeyWords3.addItem(colors[i]);
		}
		for(int i = 0; i < 2; i++) {
			cbxKeyWords3.addItem(colors[i]);
		}
		for (int i = 5; i < 8; i++) {
			cbxNote.addItem(colors[i]);
		}
		for(int i = 0; i < 5; i++) {
			cbxNote.addItem(colors[i]);
		}
		for (int i = 6; i < 8; i++) {
			cbxString.addItem(colors[i]);
		}
		for(int i = 0; i < 6; i++) {
			cbxString.addItem(colors[i]);
		}
		for (int i = 7; i < 8; i++) {
			cbxOpr.addItem(colors[i]);
		}
		for(int i = 0; i < 7; i++) {
			cbxOpr.addItem(colors[i]);
		}
		
	}
	
	public static void initFonts() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontList = ge.getAvailableFontFamilyNames();
		int i;
		
		for(i = 1; i < fontList.length; i++) {
			cbxFont.addItem(fontList[i]);
		}
		for(i = 13; i <= 72; i++) {
			cbxFontSize.addItem(i);
		}
	}

	public void actionPerformed(ActionEvent e) {// 按钮动作监听器

		if (e.getSource() == b1) {// 如果动作源是按钮1
			File file = null;
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			Transformation t;

			try { 
				int flag = chooser.showOpenDialog(null);
				if (flag == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();
					String s = file.getAbsolutePath();
					str1 = s.replace("\\", "\\\\").toString();
					String str = "file:" + s;
					jEditorPane1.setPage(str);
					t1.setText(s);
					str2 = str1.toString() + ".html";
					strTemp = "file:" + str2;
					t = new Transformation(str1, str2);
					t.Main_Transformation();
					jEditorPane2.setPage(strTemp);
				}
			} catch (IOException ex) {
				System.out.println(e);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (e.getSource() == b2) {// 如果动作源是按钮2

			File file = null;
			JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			try {
				int flag = chooser.showOpenDialog(null);
				if (flag == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();
					String s = file.getAbsolutePath();
					str2 = s.replace("\\", "\\\\").toString() + "\\\\Result.html";
					strOutPut = "file:" + s + "\\Result.html";
					t2.setText(s);
				}
			} catch (HeadlessException ex) {
				System.out.println(e);
			}
		} else if (e.getSource() == b3) {
			Transformation t;
			try {
				String font = cbxFont.getSelectedItem().toString();
				String fontSize = cbxFontSize.getSelectedItem().toString();
				String keyWords1 = cbxKeyWords1.getSelectedItem().toString().substring(0, 7);
				String keyWords2 = cbxKeyWords1.getSelectedItem().toString().substring(0, 7);
				String keyWords3 = cbxKeyWords1.getSelectedItem().toString().substring(0, 7);
				String note = cbxNote.getSelectedItem().toString().substring(0, 7);
				String string = cbxString.getSelectedItem().toString().substring(0, 7);
				String opr = cbxOpr.getSelectedItem().toString().substring(0, 7);
				System.out.println(keyWords1);
				t = new Transformation(str1, str2);
				
				String[] getHead = t.getHtml_head();
				getHead[8] = "pre{font-family:"+font+";font-size:"+fontSize+"px;}";
				getHead[10] = ".key1{color:" + keyWords1 + ";font-weight:bold;}";
				getHead[11] = ".key2{color:" + keyWords2 + ";font-weight:bold;}";
				getHead[12] = ".key3{color:" + keyWords3 + ";font-weight:bold;}";
				getHead[15] = ".note{color:" + note + ";font-weight:bold;font-style:italic;}";
				getHead[16] = ".str{color:" + string + ";font-weight:bold;}";
				getHead[17] = ".opr{color:" + opr + ";font-weight:bold;}";
				t.setHtml_head(getHead);
				
				t.Main_Transformation();
				jEditorPane2.setPage(strOutPut);
				JOptionPane.showMessageDialog(null, "格式转换完成！", "提示", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}else if (e.getSource() == b4) {
			JOptionPane.showMessageDialog(null, "作者：171304213 武丞\n             171304214 谢昊", "关于我们", JOptionPane.INFORMATION_MESSAGE);
		}

	}

}
