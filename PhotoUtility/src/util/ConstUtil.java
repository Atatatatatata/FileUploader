package util;

public class ConstUtil {
	/**
	 * アプリ内で使用する定数
	 */
	// パス関連
	public static final String UPLOAD_JPG_BASE_PATH = "/Volumes/BIEI/dslr_jpg/";
	public static final String UPLOAD_RAW_BASE_PATH = "/Volumes/BIEI/dslr_raw/";
	public static final String UPLOAD_MOV_BASE_PATH = "/Volumes/BIEI/dslr_movie/";

	// プロパティキー
	public static final String PROP_USER1 = "user1";
	public static final String PROP_USER2 = "user2";
	public static final String PROP_USER3 = "user3";
	public static final String PROP_JPG_BASE_PATH = "jpg_upload_base_path";
	public static final String PROP_RAW_BASE_PATH = "raw_upload_base_path";
	public static final String PROP_MOV_BASE_PATH = "mov_upload_base_path";

	public static final String UPLOAD_SRC_PATH = "/Volumes";

	public static final String EXIF_FILE_NAME = "exif_info.txt";
	public static final String LOG_PATH = "./log";

	// 処理対象
	public static final String FILE_TYPE_JPG = "jpg";
	public static final String FILE_TYPE_RAW = "raw";
	public static final String FILE_TYPE_MOV = "mov";
	public static final String FILE_TYPE_EXIF = "exif";

	// 拡張子
	public static final String EXTENSIONS_NAME_IMAGE = "{jpg,JPG}";
	public static final String EXTENSIONS_NAME_MOVIE = "{mp4,mov,MP4,MOV}";
	public static final String EXTENSIONS_NAME_RAW = "{cr2,cr3,nef,CR2,NEF,CR3}";

	/**
	 * 表示するメッセージ
	 */
	// エラー系
	public static final String ERROR_MSG_DATE_EMPTY = "日付が指定されていません";
	public static final String ERROR_MSG_DATE_FOMATE = "日付の指定に誤りがあります";
	public static final String ERROR_MSG_PLACE_EMPTY = "場所が指定されていません";
	public static final String ERROR_MSG_NAME_EMPTY = "名前が指定されていません";
	public static final String ERROR_MSG_MKDR_ERROR = "フォルダの作成に失敗しました";
	public static final String ERROR_MSG_EXIST_DIRECTORY = "すでに作成されたフォルダです";
	public static final String ERROR_MSG_SRCPATH_EMPTY = "フォルダが選択されていません";
	public static final String ERROR_MSG_UPLOAD = "処理に失敗しました";

	// インフォメーション系
	public static final String EXECUTE_MSG_UPLOAD_COMPLETE = "処理が完了しました";
	public static final String EXECUTE_MSG_MOVE_COMPLETE = "アップロード完了";
	public static final String EXECUTE_MSG_MOVE_NO_EXECUTE = "アップロード対象なし";
	public static final String EXECUTE_MESSAGE = "処理中...";
	public static final String EXECUTE_COMPLETE = "完了";
	public static final String EXECUTE_EXIF_GENERATE = "EXIF生成中...";
}
