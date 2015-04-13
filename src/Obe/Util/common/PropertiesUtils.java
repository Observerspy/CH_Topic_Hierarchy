package Obe.Util.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 读取properties 文件属性
 * 
 * @author bing
 * 
 */
public class PropertiesUtils {
	public static String CLASS_DIR = PropertiesUtils.class.getResource("/")
			.getFile();

	/**
	 * @功能：配置路径字符串处理
	 * @return
	 */
	public static String processPath(String dir) {
		return dir.replaceAll("%20", " ");
	}

	/**
	 * @功能：根据传入参数的不同，返回相应的配置文件属性值
	 * @param args
	 * @return
	 */
	public static String getPropertiesValue(String args, String pfilename) {
		Properties propertie = new Properties();
		String configpath = "F:/work/CH_Topic_Hierarchy/" + pfilename;
		String configdir = processPath(configpath);

		String value = "";
		try {
			FileInputStream inputFile = new FileInputStream(configdir);
			propertie.load(inputFile);

			if (propertie.containsKey(args)) {
				value = propertie.getProperty(args).trim();
			}
			inputFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 读取properties 文件，获取所有属性的代码
	 * 
	 * @param configpath
	 * @return
	 */
	public static Map<String, String> getPropertiesMap(String configpath) {
		// public static void getPropertiesMap(String configpath){
		Map<String, String> propertiesMap = new HashMap<String, String>();
		Properties properties = new Properties();
		String configdir = processPath(configpath);

		try {
			FileInputStream inputFile = new FileInputStream(configdir);
			properties.load(inputFile);

			Set<Object> keys = properties.keySet();// 返回属性key的集合
			for (Object key : keys) {
				propertiesMap.put(key.toString(), properties.get(key)
						.toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return propertiesMap;
	}
}
