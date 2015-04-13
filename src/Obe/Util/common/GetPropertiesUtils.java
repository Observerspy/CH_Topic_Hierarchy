package Obe.Util.common;

import java.io.File;
import java.util.Date;

/**
 * 临时文件路径获取 Utils
 * 
 * @author bing
 * 
 */
public class GetPropertiesUtils {
	/**
	 * 临时文件存放路径
	 */
	private static String CONFIG_FILENAME = ConstantUtils.CONFIG;

	/**
	 * 临时文件存放路径的属性名
	 */
	private static String INPUTD = ConstantUtils.INPUTD;
	private static String OUTPUTD = ConstantUtils.OUTPUTD;
	private static String EX = ConstantUtils.EX;


	/**
	 * 获取聚类处理临时文件存放路径
	 * 
	 * @return
	 */
	public static String getInputDir() {
		String tempdir = PropertiesUtils.getPropertiesValue(INPUTD,
				CONFIG_FILENAME);

		File file = new File(tempdir);

		if (!file.exists()) {
			file.mkdirs();
		}

		return tempdir;
	}
	
	public static String getoutPutDir() {
		String tempdir = PropertiesUtils.getPropertiesValue(OUTPUTD,
				CONFIG_FILENAME);

		File file = new File(tempdir);

		if (!file.exists()) {
			file.mkdirs();
		}

		return tempdir;
	}
	
	public static String getEx() {
		String tempdir = PropertiesUtils.getPropertiesValue(EX,
				CONFIG_FILENAME);

		File file = new File(tempdir);

		if (!file.exists()) {
			file.mkdirs();
		}

		return tempdir;
	}

	/**
	 * 根据QueryModel 中的开始时间和截止时间来确定临时文件名
	 * 
	 * @param queryModel
	 * @return
	 */

	public static void main(String[] args){
		
	}
}
