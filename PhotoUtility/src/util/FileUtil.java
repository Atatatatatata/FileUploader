package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;

import form.MainFrame;
import model.ExecuteResultBean;
import model.ExifInfoBean;

public class FileUtil {

	/**
	 * ファイルコピーモード
	 */
	public static final String MODE_MOVE = "MV";
	public static final String MODE_COPY = "CP";

	/**
	 * アップロード先のパスを返す
	 * @param dateString
	 * @param placeString
	 * @param nameString
	 * @return
	 */
	public static HashMap<String, String> getSavePath(String dateString, String placeString) {

		// 保存用ディレクトリのベースpath
		HashMap<String, String> savePathList = new HashMap<String, String>();

		// 設定ファイルからそれぞれのファイルの保存先を取得
		String jpgBasePath = PropertyUtil.getProperty(ConstUtil.PROP_JPG_BASE_PATH);
		String rawBasePath = PropertyUtil.getProperty(ConstUtil.PROP_RAW_BASE_PATH);
		String movBasePath = PropertyUtil.getProperty(ConstUtil.PROP_MOV_BASE_PATH);

		// それぞれのベースパスをセット
		savePathList.put(ConstUtil.FILE_TYPE_JPG, jpgBasePath);
		savePathList.put(ConstUtil.FILE_TYPE_MOV, movBasePath);
		savePathList.put(ConstUtil.FILE_TYPE_RAW, rawBasePath);

		for (String key : savePathList.keySet()) {
			String basePath = savePathList.get(key);

			// ベースpath+フォームで指定した日付、場所、名前を結合して実際に保存するフォルダ名として保持
			savePathList.put(key, basePath + dateString + "_" + placeString + "_" + key);
		}

		return savePathList;
	}

	/**
	 * フォルダを作成する 面倒なので、既存フォルダがあってもerrorにしない 重複するかしないかは入力バリデーションではじく
	 *
	 * @param path
	 * @return
	 */
	public static boolean makeDirectory(String path) {
		boolean ret = true;
		File directry = new File(path);
		if (!directry.exists()) {
			// フォルダがなければ作成
			ret = directry.mkdir();
		}
		return ret;
	}

	/**
	 * フォルダを作成する
	 *
	 * @param savePathList
	 * @return フォルダ作成結果
	 */
	public static boolean bulkMakeDirectory(HashMap<String, String> savePathList) {
		boolean ret = false;
		for (String value : savePathList.values()) {
			// それぞれの拡張子に対応するフォルダの作成
			ret = makeDirectory(value);
			if (!ret) {
				return ret;
			}
		}
		return ret;
	}

	/**
	 * 指定したディレクトリのファイルすべてを指定したディレクトリに移動する。
	 *
	 * @param srcDirPath
	 * @param distDirPath
	 * @param extString
	 * @param param
	 *            拡張子指定
	 * @return
	 */
	public static void bulkMoveFile(MainFrame frame, String srcDirPath, String distDirPath, String extString,
			ExecuteResultBean param, String type, String mode) {
		// 拡張子
		String extension = "*.";
		int totalCount = 0;
		int fileCount = 0;

		if (!extString.isEmpty()) {
			// 拡張子指定があったら条件に追加
			extension += extString;
		} else {
			// なければすべての拡張子を対象とする
			extension += "*";
		}

		// 移動元と先のPathを生成
		Path srcPath = Paths.get(srcDirPath);
		Path distPath;

		frame.setStringChildProgress(type + ConstUtil.EXECUTE_MESSAGE);

		// 条件に該当するファイルの取得
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(srcPath, extension)) {
			for (Path moveFilePath : ds) {
				// macで移動対象としないファイル（._から始まる）を除外する
				String moveFileName = moveFilePath.getFileName().toString();
				if(CommonUtil.isAvailableFile(moveFileName))
				{
					if (fileCount == 0) {
						// 一回目のループでの特殊処理
						totalCount = fileCount(srcPath, extension);
						frame.setChildProgressMax(totalCount);

						// フォルダ作成
						if (!makeDirectory(distDirPath)) {
							// フォルダ作成に失敗したらエラーとして処理終了
							param.setResult(false);
							param.setMessage(ConstUtil.ERROR_MSG_MKDR_ERROR);
							param.setExecuteCount(fileCount);
							return;
						}
					}

					// 移動先のパスの取得。
					// ファイル名まで含めるので、移動先のパス+移動対象パスからファイル名のみを抜き出して結合する
					distPath = Paths.get(distDirPath + "/" + moveFilePath.getFileName());

					// ファイル処理
					if (mode.equals(MODE_MOVE)) {
						Files.move(moveFilePath, distPath, StandardCopyOption.REPLACE_EXISTING);
					} else if (mode.equals(MODE_COPY)) {
						Files.copy(moveFilePath, distPath, StandardCopyOption.REPLACE_EXISTING);
					}
					fileCount++;
					frame.updateChildProgress(fileCount);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// プログレス完了表示
		frame.setStringChildProgress(ConstUtil.EXECUTE_COMPLETE);
		frame.updateChildProgress(0);

		// すべての処理が終わったら、正常終了で結果を返す
		param.setResult(true);
		param.setMessage("");
		param.setExecuteCount(fileCount);
		return;
	}

	/**
	 * ファイル件数を数える
	 *
	 * @param ds
	 * @return
	 */
	public static int fileCount(Path path, String extension) {
		int count = 0;
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, extension)) {
			for (Path file : stream) {
				file.getFileName();
				count++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * exifファイルを出力
	 * @param outputPath
	 * @param jpgPath
	 */
	public static void outputExifInfoFile(String outputPath, String jpgPath) {
		String extension = "*.{jpg,JPG}";
		String jpgFileName = "";
		HashMap<String, String> exifInfoList = new HashMap<>();

		try (DirectoryStream<Path> jpgDir = Files.newDirectoryStream(Paths.get(jpgPath), extension)) {
			for (Path file : jpgDir) {
				// ファイル名を取得
				jpgFileName = file.getFileName().toString();

				// オブジェクト生成
				ExifInfoBean exifInfo = new ExifInfoBean();

				// ファイルのexif情報をオブジェクトにセット
				exifInfo.setExifInfo(file.toString());

				// ファイル名、成型後をマップにセット
				exifInfoList.put(jpgFileName, exifInfo.getExifInfoFormatted());
			}

			if (exifInfoList.size() > 0) {
				makeExifInfoFile(exifInfoList, outputPath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * exif情報をファイルに書き込む
	 *
	 * @param exifInfoList
	 * @param savePath
	 */
	private static void makeExifInfoFile(HashMap<String, String> exifInfoList, String savePath) {
		String exifFileName = ConstUtil.EXIF_FILE_NAME;
		File exifFile = new File(savePath + "/" + exifFileName);
		PrintWriter pw;
		try {
			// 追記モードでwriterを生成
			pw = new PrintWriter(new BufferedWriter(new FileWriter(exifFile, true)));

			// ソートのためのキーを取得
			Object[] mapkey = exifInfoList.keySet().toArray();
			Arrays.sort(mapkey);

			//for (Entry<String, String> exifInfo : exifInfoList.entrySet()) {
			for (Object key : mapkey)
			{
				// 内容をファイルに書き出し
				pw.println(key.toString() + "\t" + exifInfoList.get(key.toString()));
			}
			// 終了後はclose
			pw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	/**
	 * 指定したパスのディレクトリを開く
	 */
	public static void openDirectry(String path)
	{
		   try {
			   // windowsサービス
		       Runtime runtime = Runtime.getRuntime();
		       //String cmd = "explorer "+path;
		       // Macではこっちのコマンド
		       String cmd = "/usr/bin/open "+path;
		       runtime.exec(cmd);
		   } catch (IOException e) {
		       //例外処理
		   }
	}

	/** ファイルの存在チェックをします
	 *
	 * @param filePath
	 * @return
	 */
	public static boolean exist(String filePath)
	{
		boolean ret = true;
		File file = new File(filePath);
		if (! file.exists()){
		    ret = false;
		}
		return ret;
	}
}
