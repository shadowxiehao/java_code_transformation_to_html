package test;
import org.junit.jupiter.api.Test;
import text_level.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TransformTest {

    public static void main(String[] args){
        //录入文件路径 测试时用
        System.out.print("请输入Java文件路径:");
        String a ="F:\\study&work\\works\\java\\class_design\\curriculum design\\src\\text_level\\Transform.java";
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