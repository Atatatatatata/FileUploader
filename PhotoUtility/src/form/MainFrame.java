package form;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.ExecuteResultBean;
import model.UploadBean;
import util.ConstUtil;
import util.DateUtil;
import util.FileUtil;
import util.PropertyUtil;


public class MainFrame extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField txtDateValue;
	private JTextField txtPlaceValue;
	private JLabel lblMessage;
	private JLabel lblJpg;
	private JLabel lblRaw;
	private JLabel lblMov;
	private JLabel lblJpgResult;
	private JLabel lblRawResult;
	private JLabel lblMovResult;
	private JLabel lblJpgCount;
	private JLabel lblRawCount;
	private JLabel lblMovCount;
	private JProgressBar prgChildExecuteStatus;
	private JProgressBar prgParentExecuteStatus;
	private JTextField txtSrcDirPath;
	private JCheckBox cbxOverWiteFlag;
	private JCheckBox cbxExifOutputFlag;
	private JCheckBox cbxOpenDirFlag;
	private JCheckBox cbxAfterDelete;

	private static final String BTN_TEXT_UPLOAD = "実行";
	private static final String BTN_TEXT__CHOOSER = "選択";
	private JSeparator separator;
	private JSeparator separator_1;
	private JLabel lblProgress;
	private JSeparator separator_2;
	private JLabel lblNewLabel_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("ファイルアップローダ");

		// アイコン設定
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/assets/icon.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 378, 546);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblDate = new JLabel("日付:");
		lblDate.setBounds(12, 70, 50, 13);
		contentPane.add(lblDate);

		Date today = new Date();
		SimpleDateFormat formatYMD = new SimpleDateFormat("yyyyMMdd");
		txtDateValue = new JTextField(formatYMD.format(today));
		txtDateValue.setToolTipText("yyyyMMdd");
		txtDateValue.setBounds(43, 66, 98, 19);
		contentPane.add(txtDateValue);
		txtDateValue.setColumns(10);

		JLabel lblPlace = new JLabel("場所:");
		lblPlace.setBounds(12, 95, 50, 13);
		contentPane.add(lblPlace);

		txtPlaceValue = new JTextField();
		txtPlaceValue.setBounds(43, 91, 246, 19);
		contentPane.add(txtPlaceValue);
		txtPlaceValue.setColumns(10);

		lblMessage = new JLabel("");
		lblMessage.setBounds(12, 437, 360, 19);
		contentPane.add(lblMessage);

		JButton btnUpload = new JButton(BTN_TEXT_UPLOAD);
		btnUpload.addActionListener(this);
		btnUpload.setBounds(173, 476, 66, 42);
		// btnUpload.
		contentPane.add(btnUpload);

		lblJpg = new JLabel("JPG");
		lblJpg.setBounds(33, 300, 50, 13);
		contentPane.add(lblJpg);

		lblRaw = new JLabel("RAW");
		lblRaw.setBounds(33, 322, 50, 13);
		contentPane.add(lblRaw);

		lblMov = new JLabel("MOV");
		lblMov.setBounds(33, 345, 50, 13);
		contentPane.add(lblMov);

		lblJpgResult = new JLabel("");
		lblJpgResult.setBounds(82, 300, 220, 13);
		contentPane.add(lblJpgResult);

		lblRawResult = new JLabel("");
		lblRawResult.setBounds(82, 322, 220, 13);
		contentPane.add(lblRawResult);

		lblMovResult = new JLabel("");
		lblMovResult.setBounds(82, 345, 220, 13);
		contentPane.add(lblMovResult);

		lblJpgCount = new JLabel("");
		lblJpgCount.setBounds(322, 294, 50, 13);
		contentPane.add(lblJpgCount);

		lblRawCount = new JLabel("");
		lblRawCount.setBounds(322, 317, 50, 13);
		contentPane.add(lblRawCount);

		lblMovCount = new JLabel("");
		lblMovCount.setBounds(322, 342, 50, 13);
		contentPane.add(lblMovCount);

		// 進捗更新用プログレス(子)
		prgChildExecuteStatus = new JProgressBar();
		prgChildExecuteStatus.setBounds(33, 386, 308, 14);
		prgChildExecuteStatus.setValue(0);
		contentPane.add(prgChildExecuteStatus);

		// 進捗更新用プログレス(親)
		prgParentExecuteStatus = new JProgressBar();
		prgParentExecuteStatus.setBounds(33, 372, 308, 14);
		prgParentExecuteStatus.setValue(0);
		prgParentExecuteStatus.setMaximum(100);
		;
		contentPane.add(prgParentExecuteStatus);

		JButton btnCoose = new JButton(BTN_TEXT__CHOOSER);
		btnCoose.setBounds(45, 37, 66, 21);
		btnCoose.addActionListener(this);
		contentPane.add(btnCoose);

		txtSrcDirPath = new JTextField();
		txtSrcDirPath.setBounds(43, 15, 288, 19);
		contentPane.add(txtSrcDirPath);
		txtSrcDirPath.setColumns(10);

		cbxOverWiteFlag = new JCheckBox("上書きを許可");
		cbxOverWiteFlag.setBounds(33, 153, 133, 21);
		contentPane.add(cbxOverWiteFlag);

		cbxExifOutputFlag = new JCheckBox("Exif出力");
		cbxExifOutputFlag.setBounds(33, 199, 137, 21);
		cbxExifOutputFlag.setSelected(true);
		contentPane.add(cbxExifOutputFlag);

		cbxOpenDirFlag = new JCheckBox("完了後にフォルダを開く");
		cbxOpenDirFlag.setBounds(33, 222, 220, 21);
		cbxOpenDirFlag.setSelected(true);
		contentPane.add(cbxOpenDirFlag);

		JLabel lblNewLabel = new JLabel("Options");
		lblNewLabel.setBounds(12, 129, 81, 13);
		contentPane.add(lblNewLabel);

		separator = new JSeparator();
		separator.setBounds(12, 142, 329, 22);
		contentPane.add(separator);

		separator_1 = new JSeparator();
		separator_1.setBounds(12, 283, 329, 13);
		contentPane.add(separator_1);

		lblProgress = new JLabel("Progress");
		lblProgress.setBounds(12, 271, 98, 13);
		contentPane.add(lblProgress);

		separator_2 = new JSeparator();
		separator_2.setBounds(12, 412, 623, 2);
		contentPane.add(separator_2);

		lblNewLabel_1 = new JLabel("Message");
		lblNewLabel_1.setBounds(12, 412, 98, 13);
		contentPane.add(lblNewLabel_1);

		// プログレス非表示
		this.setProgressVisible(false);

		// 日付の初期値として現在日時をセット
		this.txtDateValue.setText(DateUtil.getNow("yyyyMMdd"));

		cbxAfterDelete= new JCheckBox("移動後に削除");
		cbxAfterDelete.setBounds(33, 176, 133, 21);
		contentPane.add(cbxAfterDelete);

		JLabel lblNewLabel_2 = new JLabel("取込:");
		lblNewLabel_2.setBounds(12, 19, 42, 13);
		contentPane.add(lblNewLabel_2);

		// 設定ファイルチェック
		HashMap<String, String> settingList = new HashMap<String, String>();
		try
		{
			settingList = PropertyUtil.getSetting();
		} catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
			System.exit(1);
		}
	}

	/**
	 * formのチェック エラーは面倒なので文字列で返す
	 *
	 * @return erromessage
	 */
	private String validateForm() {
		// 日付入力判定
		if (this.txtDateValue.getText().isEmpty()) {
			return ConstUtil.ERROR_MSG_DATE_EMPTY;
		}
		// 場所入力判定
		if (this.txtPlaceValue.getText().isEmpty()) {
			return ConstUtil.ERROR_MSG_PLACE_EMPTY;
		}
		// フォルダ入力判定
		if (this.txtSrcDirPath.getText().isEmpty()) {
			return ConstUtil.ERROR_MSG_SRCPATH_EMPTY;
		}

		// 日付のフォーマットチェック
		ExecuteResultBean checkResult = util.DateUtil.checkDate(this.txtDateValue.getText(), "yyyymmdd");
		if(! checkResult.isResult())
		{
			return checkResult.getMessage();
		}

		// 上書きOKなら作成ファイルの存在チェック
		if (this.cbxOverWiteFlag.isSelected()) {
			return "";
		}
		// 作成しようとするフォルダの存在チェック
		HashMap<String, String> filepaths = FileUtil.getSavePath(this.txtDateValue.getText(),
				this.txtPlaceValue.getText());
		for (String path : filepaths.values()) {
			if (new File(path).exists()) {
				return ConstUtil.ERROR_MSG_EXIST_DIRECTORY;
			}
		}
		return "";
	}

	private void uplaod() {
		UploadBean upload = new UploadBean();

		// パラメータセット
		upload.setDateString(this.txtDateValue.getText());
		upload.setPlaceString(this.txtPlaceValue.getText());
		upload.setSrcPathString(this.txtSrcDirPath.getText());

		if (this.cbxExifOutputFlag.isSelected()) {
			// exif出力指定ありの場合はセット
			upload.setExifOutput(true);
		}

		if (this.cbxOpenDirFlag.isSelected()) {
			// フォルダを開く指定ありの場合はセット
			upload.setOpenDir(true);
		}

		if (this.cbxAfterDelete.isSelected()) {
			upload.setAfterDelete(true);
		}

		// アップロード
		// 状態を表示するダイアログを表示するため、フォームのインスタンスを渡す
		upload.executeUpload(this);
	}

	// イベント・リスナー
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(BTN_TEXT_UPLOAD)) {
			// 実行ボタン選択時
			String errMsg = validateForm();
			if (errMsg.isEmpty()) {
				// 両方入力されていたら処理する。
				lblMessage.setText("");

				// 処理開始
				new Thread(new Runnable() {
					@Override
					public void run() {
						// 非同期でアップロード処理
							uplaod();
					}
				}).start();
			} else {
				// バリデーションエラーの場合はエラーメッセージ表示
				lblMessage.setText(errMsg);
			}
		} else {
			// それ以外(現状アップロード元選択)
			JFileChooser filechooser = new JFileChooser(ConstUtil.UPLOAD_SRC_PATH);
			filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int selected = filechooser.showOpenDialog(this);
			if (selected == JFileChooser.APPROVE_OPTION) {
				File file = filechooser.getSelectedFile();
				txtSrcDirPath.setText(file.getAbsolutePath());
			}
		}
	}

	/**
	 * 以下、UIを外から操作するメソッド
	 */

	/**
	 * jpg処理結果
	 *
	 * @param type
	 * @param count
	 */
	public void setResult(ExecuteResultBean param, String type) {
		String message = "";
		Integer count = (Integer) param.getExecuteCount();
		// 結果error
		if (!param.isResult()) {
			message = ConstUtil.ERROR_MSG_UPLOAD;
		}
		// 対象なし
		if (count.equals(0)) {
			message = ConstUtil.EXECUTE_MSG_MOVE_NO_EXECUTE;
		} else {
			// 対象あり
			message = ConstUtil.EXECUTE_MSG_MOVE_COMPLETE;
			// 件数表示
			switch (type) {
			case ConstUtil.FILE_TYPE_JPG:
				this.lblJpgCount.setText(count.toString());
				break;
			case ConstUtil.FILE_TYPE_RAW:
				this.lblRawCount.setText(count.toString());
				break;
			case ConstUtil.FILE_TYPE_MOV:
				this.lblMovCount.setText(count.toString());
				break;
			default:
			}
		}
		// 結果表示
		switch (type) {
		case ConstUtil.FILE_TYPE_JPG:
			this.lblJpgResult.setText(message);
			break;
		case ConstUtil.FILE_TYPE_RAW:
			this.lblRawResult.setText(message);
			break;
		case ConstUtil.FILE_TYPE_MOV:
			this.lblMovResult.setText(message);
			break;
		default:
		}
	}

	/**
	 * プログレス初期化
	 */
	public void setDispComp() {
		this.setStringChildProgress("");
		this.setStringParentProgress("");
		this.updateChildProgress(0);
		this.updateParentProgress(0);
		this.setProgressVisible(false);

		this.lblMessage.setText("処理完了");
	}

	/**
	 * プログレスバーを更新
	 *
	 * @param progress
	 */
	public void updateChildProgress(int progress) {
		this.prgChildExecuteStatus.setValue(progress);
	}

	public void updateParentProgress(int progress) {
		this.prgParentExecuteStatus.setValue(progress);
	}

	/**
	 * プログレスの文字設定
	 *
	 * @param progress
	 */
	public void setStringChildProgress(String text) {
		this.prgChildExecuteStatus.setString(text);
	}

	public void setStringParentProgress(String text) {
		this.prgParentExecuteStatus.setString(text);
	}

	/**
	 * プログレス表示制御
	 *
	 * @param visible
	 */
	public void setProgressVisible(boolean visible) {
		this.prgChildExecuteStatus.setVisible(visible);
		this.prgChildExecuteStatus.setStringPainted(visible);
		this.prgParentExecuteStatus.setVisible(visible);
		this.prgParentExecuteStatus.setStringPainted(visible);
	}

	public void setChildProgressMax(int max) {
		this.prgChildExecuteStatus.setMaximum(max);
	}

	/**
	 * 表示の初期化
	 */
	public void initDisp() {
		this.lblJpgCount.setText("");
		this.lblRawCount.setText("");
		this.lblMovCount.setText("");

		this.lblJpgResult.setText("");
		this.lblRawResult.setText("");
		this.lblMovResult.setText("");
	}
}
