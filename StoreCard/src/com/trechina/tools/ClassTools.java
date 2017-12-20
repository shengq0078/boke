package com.trechina.tools;

/***
 * 
 * @author sq
 *
 */
public class ClassTools {
	public static String SplitName(int i, String strName) {
		String nameSp = null;
		String[] name = strName.split(" |　");
		if (name.length >= 1) {
			nameSp = name[i];
		}
		return nameSp;
	}

	public static boolean Namejudge(String strName) {
		boolean bo;
		String[] name = strName.split(" |　");
		if (name.length > 1) {
			bo = true;
		} else {
			bo = false;
		}
		return bo;
	}
}
