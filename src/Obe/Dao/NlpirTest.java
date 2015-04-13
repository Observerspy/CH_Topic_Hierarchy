package Obe.Dao;

import java.io.UnsupportedEncodingException;

import utils.SystemParas;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class NlpirTest {

	// 定义接口CLibrary，继承自com.sun.jna.Library
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				"F:\\work\\CH_Topic_Hierarchy\\lib\\win64\\NLPIR", CLibrary.class);
		
		public int NLPIR_Init(String sDataPath, int encoding,
				String sLicenceCode);
				
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);
		public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);
		public int NLPIR_AddUserWord(String sWord);//add by qp 2008.11.10
		public int NLPIR_DelUsrWord(String sWord);//add by qp 2008.11.10
		public String NLPIR_GetLastErrorMsg();
		public void NLPIR_Exit();
	}

	public static String transString(String aidString, String ori_encoding,
			String new_encoding) {
		try {
			return new String(aidString.getBytes(ori_encoding), new_encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getWords(String sInput) throws Exception {
		String argu = "F:\\work\\CH_Topic_Hierarchy";
		// String system_charset = "GBK";//GBK----0
		String system_charset = "UTF-8";
		int charset_type = 1;
		
		int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
		String nativeBytes = null;

		if (0 == init_flag) {
			nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！fail reason is "+nativeBytes);
			return null;
		}

		//String sInput = "此次地震震央位在南投县集集镇（北纬23.8度、东经120.78度），即日月潭西偏南9.2公里，芮氏规模7.3，释出的总能量约为1998年瑞里地震的40倍，相当于50颗广岛原子弹的威力，地震深度8.0公里。利用波形反演得到的为一低角度逆冲断层、伴随些许走向滑移份量，释放出能量为1.77±0.4×<math>10^{20}</math>N-m。滑移方向则为北偏西60度，随着时间而改变，显示断层的破裂并不均匀。断层走向为北偏东18度，倾角19度。此次地震属内陆浅层地震，断层地表破裂面全长约105公里，造成地表断层最大垂直错动量达到11公尺、最大水平错动量达10公尺以上，平均错动量约4公尺，破坏力相当强大";
		//String nativeBytes = null;
		try {
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);

			//System.out.println("分词结果为： " + nativeBytes);
			
//			CLibrary.Instance.NLPIR_AddUserWord("要求美方加强对输 n");
//			CLibrary.Instance.NLPIR_AddUserWord("华玉米的产地来源 n");
//			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);
//			System.out.println("增加用户词典后分词结果为： " + nativeBytes);
//			
//			CLibrary.Instance.NLPIR_DelUsrWord("要求美方加强对输");
//			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(sInput, 1);
//			System.out.println("删除用户词典后分词结果为： " + nativeBytes);
//			
//			
//			int nCountKey = 0;
//			String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(sInput, 10,false);
//
//			System.out.print("关键词提取结果是：" + nativeByte);
//
//			nativeByte = CLibrary.Instance.NLPIR_GetFileKeyWords("D:\\NLPIR\\feedback\\huawei\\5341\\5341\\产经广场\\2012\\5\\16766.txt", 10,false);
//
//			System.out.print("关键词提取结果是：" + nativeByte);

			

			CLibrary.Instance.NLPIR_Exit();

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return nativeBytes;

	}
}
