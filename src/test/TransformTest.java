package test;
import text_level.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;


class TransformTest {
    public static void main(String[] args){
        //录入文件路径 测试时用
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入Java文件路径:");
        String a ="F:\\study&work\\works\\java\\class_design\\curriculum design\\src\\GUI_start\\GUI.java";
        String a1 = "F:\\study&work\\works\\java\\homework\\ex_for_class\\src\\ex6\\ex6special\\main_ex6_special.java";
        System.out.println();
        System.out.print("请输入想输出的html文件路径:");
        String b = "F:\\study&work\\works\\java\\class_design\\curriculum design\\src\\text_level\\1.html";
        System.out.println();
        try {
            Transformation t = new Transformation(a,b);
            System.out.println("1");
            t.Main_Transformation();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}