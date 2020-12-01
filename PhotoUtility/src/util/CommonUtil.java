package util;

public class CommonUtil {

	private static final String MAC_EXCLUDE_STRING="._";

	/**
	 * ダミーファイルか否か
	 * @param fineName
	 * @return 判定結果
	 */
	public static boolean isAvailableFile(String fineName)
	{
		boolean ret = true;
		if(fineName.startsWith(MAC_EXCLUDE_STRING))
		{
			ret = false;
		}
		return ret;
	}
}
