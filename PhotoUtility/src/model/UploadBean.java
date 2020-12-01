package model;

import java.beans.Beans;
import java.util.HashMap;

import javax.swing.JOptionPane;

import form.MainFrame;
import util.ConstUtil;
import util.FileUtil;

public class UploadBean extends Beans {

	private String dateString;
	private String placeString;
	private String nameString;
	private String srcPathString;
	private boolean isExifOutput = false;
	private boolean isOpenDir = false;
	private boolean isAfterDelete = false;

	public void executeUpload(MainFrame frame) {
		HashMap<String, String> savePathList = FileUtil.getSavePath(this.dateString, this.placeString);

		// progress update
		frame.setProgressVisible(true);
		frame.initDisp();

		int progressValue = 0;
		// それぞれの結果用のオブジェクト
		ExecuteResultBean jpgParam = new ExecuteResultBean();
		ExecuteResultBean rawParam = new ExecuteResultBean();
		ExecuteResultBean movParam = new ExecuteResultBean();

		frame.setStringParentProgress(ConstUtil.EXECUTE_MESSAGE);
		progressValue += 10;
		frame.updateParentProgress(progressValue);

		// アップロード元設定
		String srcPathString = this.srcPathString;

		// モード選択（デフォルトはコピー）
		String mode = FileUtil.MODE_COPY;
		if (this.isAfterDelete) {
			// 移動後削除が選択されていたらモード変更
			mode = FileUtil.MODE_MOVE;
		}

		// フォルダ作成&ファイル移動
		// jpg
		FileUtil.bulkMoveFile(frame, srcPathString, savePathList.get(ConstUtil.FILE_TYPE_JPG),
				ConstUtil.EXTENSIONS_NAME_IMAGE, jpgParam, ConstUtil.FILE_TYPE_JPG, mode);

		// exif出力設定によって進捗を変える
		if (this.isExifOutput) {
			progressValue += 20;
		} else {
			progressValue += 30;
		}
		frame.updateParentProgress(progressValue);
		frame.setResult(jpgParam, ConstUtil.FILE_TYPE_JPG);

		// raw
		FileUtil.bulkMoveFile(frame, srcPathString, savePathList.get(ConstUtil.FILE_TYPE_RAW),
				ConstUtil.EXTENSIONS_NAME_RAW, rawParam, ConstUtil.FILE_TYPE_RAW, mode);
		progressValue += 30;
		frame.updateParentProgress(progressValue);
		frame.setResult(rawParam, ConstUtil.FILE_TYPE_RAW);

		// mov
		FileUtil.bulkMoveFile(frame, srcPathString, savePathList.get(ConstUtil.FILE_TYPE_MOV),
				ConstUtil.EXTENSIONS_NAME_MOVIE, movParam, ConstUtil.FILE_TYPE_MOV, mode);
		progressValue += 30;
		frame.updateParentProgress(progressValue);
		frame.setResult(movParam, ConstUtil.FILE_TYPE_MOV);

		// 結果チェック すべて成功なら後処理へ
		if (jpgParam.isResult() && rawParam.isResult() && movParam.isResult()) {
			// exif書き出し設定があれば書き出し
			if (this.isExifOutput) {
				// プログレス
				frame.setStringChildProgress(ConstUtil.EXECUTE_EXIF_GENERATE);
				frame.updateChildProgress(0);

				// 現状jpgと同じフォルダに作成
				FileUtil.outputExifInfoFile(savePathList.get(ConstUtil.FILE_TYPE_JPG), savePathList.get("jpg"));
				frame.setStringChildProgress(ConstUtil.EXECUTE_COMPLETE);
				progressValue += 10;
				frame.updateParentProgress(progressValue);
			}

			// 条件によってアップロード先を開く
			openDirectryCheck(jpgParam, savePathList.get(ConstUtil.FILE_TYPE_JPG));
			openDirectryCheck(rawParam, savePathList.get(ConstUtil.FILE_TYPE_RAW));
			openDirectryCheck(movParam, savePathList.get(ConstUtil.FILE_TYPE_MOV));

			// 画面表示を更新
			frame.setStringParentProgress(ConstUtil.EXECUTE_COMPLETE);
			frame.setDispComp();
			JOptionPane.showMessageDialog(frame, ConstUtil.EXECUTE_MSG_UPLOAD_COMPLETE);
		} else

		{
			JOptionPane.showMessageDialog(frame, ConstUtil.ERROR_MSG_MKDR_ERROR);
		}
	}

	public void openDirectryCheck(ExecuteResultBean result, String path) {
		// フォルダを開くオプションがなければチェックしない
		if (!this.isOpenDir) {
			return;
		}
		// 実行結果があればアップロード先を開く
		Integer count = (Integer) result.getExecuteCount();
		if (count > 0) {
			FileUtil.openDirectry(path);
		}
	}

	/**
	 * dateStringを取得します。
	 *
	 * @return dateString
	 */
	public String getDateString() {
		return dateString;
	}

	/**
	 * dateStringを設定します。
	 *
	 * @param dateString
	 *            dateString
	 */
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}

	/**
	 * palceStringを取得します。
	 *
	 * @return palceString
	 */
	public String getPlaceString() {
		return placeString;
	}

	/**
	 * palceStringを設定します。
	 *
	 * @param palceString
	 *            palceString
	 */
	public void setPlaceString(String palceString) {
		this.placeString = palceString;
	}

	/**
	 * nameStringを取得します。
	 *
	 * @return palceString
	 */
	public String getNameString() {
		return nameString;
	}

	/**
	 * palceStringを設定します。
	 *
	 * @param palceString
	 *            palceString
	 */
	public void setNameString(String nameString) {
		this.nameString = nameString;
	}

	/**
	 * srcPathStringを取得します。
	 *
	 * @return srcPathString
	 */
	public String getSrcPathString() {
		return srcPathString;
	}

	/**
	 * srcPathStringを設定します。
	 *
	 * @param srcPathString
	 *            srcPathString
	 */
	public void setSrcPathString(String srcPathString) {
		this.srcPathString = srcPathString;
	}

	/**
	 * isExifOutputを取得します。
	 *
	 * @return isExifOutput
	 */
	public boolean isExifOutput() {
		return isExifOutput;
	}

	/**
	 * isExifOutputを設定します。
	 *
	 * @param isExifOutput
	 *            isExifOutput
	 */
	public void setExifOutput(boolean isExifOutput) {
		this.isExifOutput = isExifOutput;
	}

	/**
	 * isOpenDirを取得します。
	 *
	 * @return isOpenDir
	 */
	public boolean isOpenDir() {
		return isOpenDir;
	}

	/**
	 * isOpenDirを設定します。
	 *
	 * @param isOpenDir
	 *            isOpenDir
	 */
	public void setOpenDir(boolean isOpenDir) {
		this.isOpenDir = isOpenDir;
	}

	/**
	 * isAfterDeleteを取得します。
	 *
	 * @return isAfterDelete
	 */
	public boolean isAfterDelete() {
		return isAfterDelete;
	}

	/**
	 * isAfterDeleteを設定します。
	 *
	 * @param isAfterDelete
	 *            isAfterDelete
	 */
	public void setAfterDelete(boolean isAfterDelete) {
		this.isAfterDelete = isAfterDelete;
	}
}
