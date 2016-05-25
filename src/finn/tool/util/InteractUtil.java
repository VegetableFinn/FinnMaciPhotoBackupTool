package finn.tool.util;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/**
 * 交互工具类
 * @author hefan.hf
 * @version $Id: InteractUtil, v 0.1 16/5/25 22:08 hefan.hf Exp $
 */
public class InteractUtil {
    /**
     * 从console获得输入
     * @param prompt
     * @return
     */
    public static String readDataFromConsole(String prompt) {
        Scanner scanner = new Scanner(System.in);
        print(prompt);
        String str = scanner.nextLine();
        if (str.charAt(str.length() - 1) == '/') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    /**
     * 以UTF-8的方式输出信息
     * @param message
     */
    public static void print(String message) {
        PrintStream out = null;
        try {
            out = new PrintStream(System.out, true, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        out.println(message);
    }
}