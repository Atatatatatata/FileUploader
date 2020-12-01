package model;

import java.beans.Beans;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;

/**
 * ファイルのExifを保持するモデル
 */

public class ExifInfoBean extends Beans {

	/**
	 * カメラ名
	 */
	private String modelName;

	/**
	 * シャッタースピード(露光時間)
	 */
	private String exposeTime;

	/**
	 * F値
	 */
	private String fNumber;

	/**
	 * ISO感度
	 */
	private String isoNumber;

	/**
	 * 焦点距離(画角)
	 */
	private String focalLength;

	public void setExifInfo(String fullFilePath) {
		// ファイルオブジェクト作成
		File jpgFile = new File(fullFilePath);
		Metadata metadata;
		try {
			//metadata = ImageMetadataReaderte.readMetadata(jpgFile);
			metadata = JpegMetadataReader.readMetadata(jpgFile);
			Collection<ExifDirectoryBase> exifDirectories = metadata.getDirectoriesOfType(ExifDirectoryBase.class);
			//Collection<XmpDirectory> xmpDirectories = metadata.getDirectoriesOfType(XmpDirectory.class);
			//Collection<ExifSubIFDDirectory> exifSubDirectories = metadata.getDirectoriesOfType(ExifSubIFDDirectory.class);

			for (ExifDirectoryBase exifInfo : exifDirectories) {
				if (exifInfo.getString(ExifDirectoryBase.TAG_EXPOSURE_TIME) != null) {
					this.exposeTime = exifInfo.getString(ExifDirectoryBase.TAG_EXPOSURE_TIME);
					// TODO コンバート処理
				}
				if (exifInfo.getString(ExifDirectoryBase.TAG_FNUMBER) != null) {
					this.fNumber = exifInfo.getString(ExifDirectoryBase.TAG_FNUMBER);
				}
				if (exifInfo.getString(ExifDirectoryBase.TAG_FOCAL_LENGTH) != null) {
					this.focalLength = exifInfo.getString(ExifDirectoryBase.TAG_FOCAL_LENGTH);
				}
				if (exifInfo.getString(ExifDirectoryBase.TAG_MODEL) != null) {
					this.modelName = exifInfo.getString(ExifDirectoryBase.TAG_MODEL);
				}
				if (exifInfo.getString(ExifDirectoryBase.TAG_ISO_EQUIVALENT) != null) {
					this.isoNumber = exifInfo.getString(ExifDirectoryBase.TAG_ISO_EQUIVALENT);
				}
			}
		} catch (ImageProcessingException e) {
			// 現状エラーになってもファイル移動は成功しているので続行
			e.printStackTrace();
		} catch (IOException e) {
			// 現状エラーになってもファイル移動は成功しているので続行
			e.printStackTrace();
		}
	}

	/**
	 * exif情報を成型する
	 *
	 * @return
	 */
	public String getExifInfoFormatted() {
		return "カメラ:" + this.modelName + ", SS:" + this.exposeTime + "s, 絞り:f/" + this.fNumber + ", 焦点距離:"
				+ this.focalLength + "mm, ISO:" + this.isoNumber;
	}

	/**
	 * シャッタースピードを1/xに統一する
	 *
	 * @return
	 */
	public String convertExposeTime() {
		if (this.exposeTime.startsWith("0.")) {
			// ０．０１などの数値で来るケースがあるので、1/xに変換
			String sNumber = this.exposeTime.replace(".", "");
			int digit = sNumber.length();
			int den = (int) Math.pow(10, (digit - 1));

			int num = (int) (Double.parseDouble(this.exposeTime) * den);
			String test = num + "/" + den;
			return test;
		} else {
			return this.exposeTime;
		}
	}

	/**
	 * シャッタースピード(露光時間)を設定します。
	 * @param exposeTime シャッタースピード(露光時間)
	 */
	public void setExposeTime(String exposeTime) {
	    this.exposeTime = exposeTime;
	}
}
