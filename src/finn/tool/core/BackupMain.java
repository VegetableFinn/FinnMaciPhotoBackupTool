package finn.tool.core;

import java.io.File;

import finn.tool.util.FileUtil;
import finn.tool.util.InteractUtil;

/**
 * 备份检查去重工具主入口
 *
 * @author hefan.hf
 * @version $Id: BackupMain, v 0.1 16/5/25 21:24 hefan.hf Exp $
 */
public class BackupMain {
	//照片导出地址
	private static String export_photo_addr;
	//照片备份地址
	private static String backup_photo_addr;
	//视频备份地址
	private static String backup_video_addr;
	//计数器
	private static int copy_count = 0;
	//耗时统计
	private static long start_time = System.currentTimeMillis();
	//文件夹数目
	private static int directory_count = 0;
	//当前文件夹进度
	private static int current_directory = 0;

	/**
	 * 开始工作
	 */
	public static void compareAndCopy() throws InterruptedException {
		//Step 0. 获得路径
		export_photo_addr = InteractUtil.readDataFromConsole("请输入照片导出根目录地址:");
		backup_photo_addr = InteractUtil.readDataFromConsole("请输入照片备份根目录地址:");
		backup_video_addr = InteractUtil.readDataFromConsole("请输入视频备份根目录地址:");
		start_time = System.currentTimeMillis();

		//Step 1. 获得导出地址中的全部文件夹
		File[] directoryList = FileUtil.getDirectoryList(export_photo_addr);
		directory_count = directoryList.length;

		//Step 2. 对于每个文件夹,默认其中的都为文件,没有嵌套文件夹
		for (File f : directoryList) {
			File[] subFiles = f.listFiles();
			for (File photoFile : subFiles) {
				backupPhoto(photoFile);
			}
			current_directory++;
			InteractUtil.print("当前进度:" + current_directory + " / " + directory_count);
		}

		//Step 3. Done
		long duration = System.currentTimeMillis() - start_time;
		InteractUtil.print("增量备份完成.共增加" + copy_count + "个文件,耗时" + duration / 1000 + "秒.");
	}

	/**
	 * 从导出地址备份到备份路径中
	 *
	 * @param photoFile
	 */
	private static void backupPhoto(File photoFile) throws InterruptedException {
		String backupFilePath = photoFile.getAbsolutePath().replaceFirst(export_photo_addr, backup_photo_addr);
		if (photoFile.getName().endsWith(".MOV") || photoFile.getName().endsWith(".mov")) {
			//若mov格式且大小大于10M,认为是视频文件
			long photoSize = photoFile.length() / 1024 / 1024;
			if (photoSize > 10) {
				backupFilePath = photoFile.getAbsolutePath().replaceFirst(export_photo_addr, backup_video_addr);
			}
		} else if (photoFile.getName().endsWith(".AAE") || photoFile.getName().endsWith(".aae")) {
			//慢动作文件也需要拷贝
			backupFilePath = photoFile.getAbsolutePath().replaceFirst(export_photo_addr, backup_video_addr);
		}

		boolean existed = FileUtil.isExistedPathWithCreate(backupFilePath);
		if (!existed) {
			FileUtil.copy(photoFile.getAbsolutePath(), backupFilePath);
			copy_count++;
		}
	}

	public static void main(String[] args) {
		try {
			compareAndCopy();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}