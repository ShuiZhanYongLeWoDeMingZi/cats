package com.kingyee.cats.enums;

import com.kingyee.common.model.IEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：短信类型枚举类
 * @author ph
 * @CreateTime 2017-4-5
 */
public enum SmsTypeEnum implements IEnum {
	/** 注册:<b style="font:bold;"> 0 </b> */
	REG("reg"),
    /** 登录:<b style="font:bold;"> 0 </b> */
    LOGIN("login");

	private String text;

    SmsTypeEnum(String text) {
		this.text = text;
	}

	@Override
	public String text() {
		return this.text;
	}

	@Override
	public String value() {
		return this.ordinal() + "";
	}
	
	@Override
	public String toString() {
		return this.ordinal() + "(" + this.text + ")";
	}

	/**
	 * 根据value值获取对应类型的英文名称
	 * @param ordinal
	 * @return
	 */
	public static String getTextByOrdinal(String ordinal) {
		for(SmsTypeEnum model : SmsTypeEnum.values()) {
			if(model.value().equals(ordinal)) {
				return model.text();
			}
		}
		return "";
	}

    /**
     * 根据value值获取对应类型的英文名称
     * @return
     */
    public static List<String> textList() {
        List<String> list = new ArrayList<String>();
        for(SmsTypeEnum model : SmsTypeEnum.values()) {
            list.add(model.text());
        }
        return list;
    }

}
