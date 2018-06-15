package com;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

public class StringUtil {
	/**
	 * 和暦(9YYMMDD)を西暦(YYYYMMDD)に変換する
	 * @param value レセプトファイル内の和暦(先頭一バイトが和暦の数字)
	 * @return YYYYMMDD形式の文字列
	 */
	public String rekiToYMD(String value) {
        //和暦の名施用設定用
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("1", "明治");
        map.put("2", "大正");
        map.put("3", "昭和");
        map.put("4", "平成");

    	String reki = value.substring(0, 1);
    	//和暦の名称に変換したもの(平成MMdd)
    	String waymd = map.get(reki) + value.substring(1);
    	DateTimeFormatter f = DateTimeFormatter.ofPattern("GyMMdd").withChronology(JapaneseChronology.INSTANCE);
    	DateTimeFormatter f2 = DateTimeFormatter.ofPattern("YYYYMMdd");
    	//和暦を西暦に変換
    	JapaneseDate d = JapaneseDate.from(f.parse(waymd));

    	//西暦のフォーマットを変更
//    	System.out.println("rekiToYMD=" + f2.format(d));

		return f2.format(d);
	}


	/**
	 * 文字列の性別をコードに変換し戻す
	 * @param value (想定される文字列、男性、女性、男、女)
	 * @return 1:男、2:女
	 */
	public String strSexToCode(String value) {
		if (value==null) {
			System.out.println("strSexToCode 性別未入力");
			return"";
		}

        HashMap<String,String> map = new HashMap<String,String>();
        map.put("男性", "1");
        map.put("男", "1");
        map.put("女性", "2");
        map.put("女", "2");

		return map.get(value);
	}

	/**
	 * 生年月日のフォーマットを変換し戻す(YYYYMMDD)
	 * @param value 変換前生年月日(YYYY/MM/DD)
	 * @return 変換後の生年月日(YYYYMMDD)
	 */
	public String birthFmModify(String value) {
		System.out.println("birthFmModify 変換前 value= " + value);

		if (value==null) {
			System.out.println("birthFmModify 生年月日未入力");
			return"";
		}

//		//test
//		value = "2018/06/11";

		Date date =null;
		String strDate =null;
		try {
//			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
			//文字列を日付型に変換する
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
	        date = sdFormat.parse(value);

	        //日付のフォーマットを変更する
	        strDate = new SimpleDateFormat("yyyyMMdd").format(date);

            System.out.println("String型 strDate= " + strDate);

		}catch( ParseException e) {
			e.printStackTrace();
			return"";
		}

		return strDate;
	}




}
