package finn.tool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 文件工具类
 * @author hefan.hf
 * @version $Id: FileUtil, v 0.1 16/5/25 21:50 hefan.hf Exp $
 */
public class FileUtil {

    /**
     * 判断路径是否已经存在,不存在则沿路创建
     * @param path
     * @return
     * @throws InterruptedException
     */
    public static boolean isExistedPathWithCreate(String path) throws InterruptedException {
        String[] paths = path.split("/");
        StringBuffer fullPath = new StringBuffer();
        for (int i = 0; i < paths.length; i++) {
            fullPath.append(paths[i]).append("/");
            File file = new File(fullPath.toString());

            if (paths.length - 1 != i) {//判断path到文件名时，无须继续创建文件夹！
                if (!file.exists()) {
                    file.mkdir();
                    InteractUtil.print("创建目录：" + fullPath.toString());
                }
            }
        }
        File file = new File(fullPath.toString());//目录全路径
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得全部目录列表.
     * 默认认为根路径下所有对象必须均为目录.
     * @param path
     * @return
     */
    public static File[] getDirectoryList(String path) {
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (File f : tempList) {
            if (!f.isDirectory()) {
                InteractUtil.print("发现根目录下存在非目录对象!程序退出.");
                System.exit(0);
            }
        }
        InteractUtil.print("路径" + path + "下共有目录" + tempList.length + "个");
        return tempList;
    }

    /**
     * 拷贝文件
     * @param oldPath
     * @param newPath
     */
    public static void copy(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            InteractUtil.print("error  ");
            e.printStackTrace();
        }
    }
}