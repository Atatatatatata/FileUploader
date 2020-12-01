package model;

import component.BaseResultParam;

/**
 * 処理結果をまとめるモデル
 */

public class ExecuteResultBean extends BaseResultParam{

	/**
	 * 処理件数
	 */
	private int executeCount = 0;

	/**
	 * 処理件数を取得します。
	 * @return 処理件数
	 */
	public int getExecuteCount() {
	    return executeCount;
	}

	/**
	 * 処理件数を設定します。
	 * @param executeCount 処理件数
	 */
	public void setExecuteCount(int executeCount) {
	    this.executeCount = executeCount;
	}
}
