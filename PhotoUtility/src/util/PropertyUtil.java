package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

public class PropertyUtil {

	private static final String INIT_FILE_PATH = "user.properties";
    private static final Properties properties;

    private PropertyUtil() throws Exception {
    }

    static {
        properties = new Properties();
        try {
            properties.load(Files.newBufferedReader(Paths.get(INIT_FILE_PATH), StandardCharsets.UTF_8));
        } catch (IOException e) {
            // ファイル読み込みに失敗
            System.out.println(String.format("ファイルの読み込みに失敗しました。ファイル名:%s", INIT_FILE_PATH));
        }
    }

    /**
     * プロパティ値を取得する
     *
     * @param key キー
     * @return 値
     */
    public static String getProperty(final String key) {
        return getProperty(key, "");
    }

    /**
     * プロパティ値を取得する
     *
     * @param key キー
     * @param defaultValue デフォルト値
     * @return キーが存在しない場合、デフォルト値
     *          存在する場合、値
     */
    public static String getProperty(final String key, final String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /** 設定ファイルの存在チェック
     *
     * @return
     */
    public static boolean checkPropertyFileExist()
    {
    		boolean ret = true;
    		if(! FileUtil.exist(INIT_FILE_PATH))
    		{
    			ret = false;
    		}
    		return ret;
    }
	/**
	 * プロパティファイルの存在チェック、設定内容をチェックする
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, String> getSetting() throws Exception
	{
		// 存在チェック
		if(!PropertyUtil.checkPropertyFileExist())
		{
			throw new Exception(String.format("ファイルの読み込みに失敗しました。ファイル名:%s", INIT_FILE_PATH));
		}

		// 保存用ディレクトリのベースpath
		HashMap<String, String> settingList = new HashMap<String, String>();

		// 設定ファイルからそれぞれのファイルの保存先を取得
		String jpgBasePath = PropertyUtil.getProperty(ConstUtil.PROP_JPG_BASE_PATH);
		String rawBasePath = PropertyUtil.getProperty(ConstUtil.PROP_RAW_BASE_PATH);
		String movBasePath = PropertyUtil.getProperty(ConstUtil.PROP_MOV_BASE_PATH);

		// 指定したディレクトリの存在チェック
		if(! FileUtil.exist(jpgBasePath))
		{
			throw new Exception("指定したJpgの保存先が無効です");
		}
		if(! FileUtil.exist(rawBasePath))
		{
			throw new Exception("指定したRawの保存先が無効です");
		}
		if(! FileUtil.exist(movBasePath))
		{
			throw new Exception("指定したMovの保存先が無効です");
		}

		// 問題なければ返却用のMapに入れる
		settingList.put(ConstUtil.FILE_TYPE_JPG, jpgBasePath);
		settingList.put(ConstUtil.FILE_TYPE_RAW, rawBasePath);
		settingList.put(ConstUtil.FILE_TYPE_MOV, movBasePath);

		return settingList;
	}
}