package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.ExecuteResultBean;

public class DateUtil {

	public static ExecuteResultBean checkDate(String dateStr, String format){
		ExecuteResultBean exResult = new ExecuteResultBean();

		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date result;
		try {
			result = formatter.parse(dateStr);
			formatter.setLenient(false);
			String reverse = formatter.format(result);
			if (dateStr.equals(reverse) == false) {
				exResult.setResult(false);
				exResult.setMessage(ConstUtil.ERROR_MSG_DATE_FOMATE);
				return exResult;
			}
			else
			{
				// チェックOK
				exResult.setResult(true);
				return exResult;
			}
		} catch (ParseException e) {
			exResult.setResult(false);
			exResult.setMessage(ConstUtil.ERROR_MSG_DATE_FOMATE);;
			return exResult;
		}
	}

	/**
	 * 現在日時をformatに従って返します
	 * @param format
	 * @return　String
	 */
	public static String getNow(String format)
	{
		Date d = new Date();
		SimpleDateFormat sFormat = new SimpleDateFormat(format);
		return sFormat.format(d);
	}
}
