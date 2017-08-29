package io.hyperion.managerPlatform.utils;

public class Const {

	public static final int DISABLE = 0; // 表示:1)策略禁用,2)数据不持久化
	public static final int ENABLE = 1; // 表示:1)策略启用,2)数据持久化
	public static final int PERSISTENCE_EXPIRETIME = 0;   //默认超过多长时间进行持久化
	
	public enum BaseInfoEnum {
        APPID, OS, APPVERSION;
    }
}
