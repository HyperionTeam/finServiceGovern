package io.hyperion.managerPlatform.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;


@Document(collection = "appStrategyConfig")
@Component("AppStrategyConfigDTO")
@CompoundIndexes({
//    @CompoundIndex(background=true,unique=true,name ="app_strategy_key_uidx", def ="{'key': -1}")//////////////去掉注释，且修改数据库
})
public class AppStrategyConfigDTO {
	
	private String _id;
	
	private String key;	//策略名，全局唯一
//	private String name;	//策略名，全局唯一/////待废弃
	private String description;	//策略说明
	private String command;	//策略执行命令，相当于回调
	private String opTime;	//最近操作时间
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getOpTime() {
		return opTime;
	}
	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	

}
