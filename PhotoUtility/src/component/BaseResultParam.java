package component;

import java.beans.Beans;


public class BaseResultParam extends Beans{

	/**
	 * 処理メッセージ
	 */
	private String message;

	/**
	 * 処理結果
	 */
	private boolean result;

	/**
	 * 処理メッセージを取得します。
	 * @return 処理メッセージ
	 */
	public String getMessage() {
	    return message;
	}

	/**
	 * 処理メッセージを設定します。
	 * @param message 処理メッセージ
	 */
	public void setMessage(String message) {
	    this.message = message;
	}

	/**
	 * 処理結果を取得します。
	 * @return 処理結果
	 */
	public boolean isResult() {
	    return result;
	}

	/**
	 * 処理結果を設定します。
	 * @param result 処理結果
	 */
	public void setResult(boolean result) {
	    this.result = result;
	}
}
