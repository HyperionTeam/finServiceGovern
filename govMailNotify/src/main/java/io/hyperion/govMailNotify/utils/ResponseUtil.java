package io.hyperion.govMailNotify.utils;

public class ResponseUtil {
	/********** 返回码定义 **********/
	public static final String success_code = "0";
	public static final String success_msg = "operate successfully";

	public static final String faile_code = "601";
	public static final String faile_msg = "operate failed";

	public static final String param_error_code = "602";
	public static final String param_error_msg = "param error";

	public static final String token_not_valid_code = "603";
	public static final String token_not_valid_msg = "token check fail";

	public static final String token_overtime_code = "604";
	public static final String token_overtime_msg = "token overtime";

	public static final String user_not_exist_code = "605";
	public static final String user_not_exist_msg = "user not exist";

	public static final String user_or_id_not_exist_code = "606";
	public static final String user_or_id_not_exist_msg = "user session does not exist, please re-auto-token-login.";

	public static final String user_id_incorrect_code = "607";
	public static final String user_id_incorrect_msg = "user session is incorrect, please re-auto-token-login.";

	public static final String PHONE_NOT_VALID_CODE = "608";// 电话号码格式非法
	public static final String PHONE_NOT_VALID_MSG = "phone number format is illegal";

	public static final String redirect_wlt_failed_code = "609";
	public static final String redirect_wlt_failed_msg = "redirect wlt login page failed.";

	public static final String can_not_unbind_code = "610";
	public static final String can_not_unbind_msg = "only one login way, can not unbind";

	public static final String user_or_id_is_null_code = "611";
	public static final String user_or_id_is_null_msg = "user session is null, please re-auto-token-login.";

	public static final String update_location_failed_code = "612";
	public static final String update_location_failed_msg = "update nearby location failed.";

	public static final String heartid_already_exist_code = "613";
	public static final String heartid_already_exist_msg = "heartid alreay exist.";

	public static final String version_is_null_code = "614";
	public static final String version_is_null_msg = "The version number is empty or does not exist.";

	public static final String user_already_exist_code = "615";
	public static final String user_already_exist_msg = "customId or mobilephone alreay exist.";

	public static final String can_not_use_password_login_code = "616";
	public static final String can_not_user_password_login_msg = "can't user password login because of password is null.";

	public static final String user_password_not_correct_code = "617";
	public static final String user_password_not_correct_masg = "Password verification failed";

	public static final String user_resource_not_found_code = "618";
	public static final String user_resource_not_found_msg = "resouce appId not found";

	public static final String otp_login_sms_overdu_code = "619";
	public static final String otp_login_sms_overdu_msg = "validateCode overdue.";

	public static final String validate_code_error_code = "620";
	public static final String validate_code_error_msg = "validateCode pls check.";

	public static final String sms_otp_no_error = "621";
	public static final String sms_otp_no_error_msg = "validateCode is null";

	public static final String sms_validate_code_error = "622";// 验证码错误
	public static final String sms_validate_code_msg = "validateCode error";

	public static final String sms_overdue_code = "623";// 验证码失效
	public static final String sms_overdue_msg = "validateCode failed";

	public static final String sms_count_over_code = "624";// 验证次数超数
	public static final String sms_count_over_msg = "verify th number out";

	public static final String user_exist_code = "625";// 手机号已注册
	public static final String user_exist_msg = "mobilephone registered";

	public static final String name_isnull_code = "626";// 昵称不能为空
	public static final String name_isnull_msg = "name is not null";

	public static final String sms_type_empty_error_code = "627";// 验证码类型为空
	public static final String sms_type_empty_error_msg = "smsType is null";

	public static final String name_islength_code = "628";// 昵称超长
	public static final String name_islength_msg = "name overlength";

	public static final String param_not_legitimate_code = "629";// 参数不合法。
	public static final String param_not_legitimate_msg = "Parameter is not legitimate.";

	public static final String sms_cache_error_code = "630";// 缓存错误
	public static final String sms_cache_error_msg = "redis error";

	public static final String heartid_exist_code = "631";
	public static final String heartid_exist_msg = "heartid exist.";

	public static final String heartid_not_valid_code = "632";
	public static final String heartid_not_valid_msg = "heartid_not_valid.";
	
	public static final String username_in_blacklist_code = "633";
	public static final String username_in_blacklist_msg = "发件人在收件人的黑名单里";
	
	public static final String encryptkey_isnull_code = "634";
	public static final String encryptkey_isnull_msg = "encryptkey is null, please check the param";

	public static final String user_lock_can_not_login_code = "635"; // 帐号被限制登录
	public static final String user_lock_can_not_login_msg = "user is lock, can not login";
	
	public static final String dubbo_exception_code = "636";
	public static final String dubbo_exception_msg = "dubbo service error.";
	
	public static final String upgrade_utype_exist = "637";
	public static final String upgrade_utype_exist_msg = "utype is exist.";
	
	public static final String upgrade_utype_not_change = "638";
	public static final String upgrade_utype_not_change_msg = "This utype is not be changed.";
	
	public static final String upgrade_sortnum_exist = "639";
	public static final String upgrade_sortnum_exist_msg = "sortnum is exist.";
	
	/********** 群相关返回码定义 **********/
	public static final String group_not_exist_code = "701";
	public static final String group_not_exist_msg = "group is not exist.";

	public static final String groupaff_already_exist_code = "702";
	public static final String groupaff_already_exist_msg = "group affiliation alreay exist.";

	public static final String time_format_error_code = "703";
	public static final String time_format_error_msg = "datetime format error";

	public static final String time_order_error_code = "704";
	public static final String time_order_error_msg = "starttime is not greater than endtime";
	/********** 群相关返回码定义end **********/

	public static final String validatepwd_count_to_ten = "800";
	public static final String validatepwd_count_to_ten_msg = "password locked for a while, please tautology after one hour.";

	/********** 公共返返回码定义 **********/
	public static final String request_not_https_code = "801";
	public static final String request_not_https_msg = "request is not executed using https";

	public static final String validatepwd_count_max_ten = "802";
	public static final String validatepwd_count_max_ten_msg = "incorrect password many times, the account will be locked one hour.";

	public static final String token_not_valid_ten = "803";
	public static final String token_not_valid_ten_msg = "token check failure ten times, please tautology after one hour.";

	public static final String token_not_valid_than_ten = "804";
	public static final String token_not_valid_than_ten_msg = "token check failure more than ten times, the account will be locked one hour.";

	public static final String validatecode_count_max_tm_code = "888";
	public static final String validatecode_count_max_tm_msg = "operating frequency too fast, try again after ten minutes";

	public static final String validatecode_count_max_code = "889";
	public static final String validatecode_count_max_msg = "operating frequency too fast, Wait a minute";

	/* 加解密过滤器错误码 */
	public static final String url_not_in_whitelist = "901";
	public static final String url_not_in_whitelist_msg = "you access url should be https or with v";
	// --------------------------------------------------------------------------------------------------------

	public static final String param_phone_linked_with_other_account_code = "1001";
	public static final String param_phone_linked_with_other_account_msg = "this mobilephone is Occupied，if you go to bind ,others may not login";

	public static final String param_self_bind_phone_code = "1005";
	public static final String param_self_bind_phone_msg = "user binded self ";

	public static final String param_unbind_only_login_phone_code = "1007";
	public static final String param_unbind_only_login_phone_msg = "user only has one way for login , not unbind";

	public static final String usersystem_not_same_code = "1008";
	public static final String usersystem_not_same_msg = "User system are not the same";

	public static final String user_password_not_valid_code = "1012";// 密码格式错误
	public static final String user_password_not_valid_msg = "Password format is illegal";

	public static final String user_status_qrytype_error_code = "1013";// 查询用户状态类型错误
	public static final String user_status_qrytype_error_msg = "userstatus qrytype is not exist";

	public static final String usersystem_is_null_code = "1014";
	public static final String usersystem_is_null_msg = "usersystem  is not exist";

	public static final String usersource_is_not_exist_code = "1015";
	public static final String usersource_is_not_exist_msg = "usersource is not exist";

	public static final String customeid_alreay_bind_code = "1101"; //该一账通已绑定其他天下通账号
	public static final String customeid_alreay_bind_msg = "The One Account is already bound to other world through account.";

	public static final String user_alreay_bind_customid_code = "1102"; //该天下通账号已绑定其他一账通
	public static final String user_alreay_bind_customid_msg = "The world through an account other account bound pass.";
	
	//---------------------------------------------华丽的分割线---------------------------------------------------//

	public static final String muc_member_max_code = "1103"; //超过群的最大人数限制
	public static final String muc_member_max_msg = "Exceed the maximum size limit group.";

	public static final String muc_member_exist_code = "1104"; //群成员已存在
	public static final String muc_member_exist_msg = "Group members already exists.";

	public static final String muc_not_exist_code = "1105"; //群不存在
	public static final String muc_not_exist_msg = "Group does not exist.";

	public static final String muc_member_not_exist_code = "1106"; //当前群不存在该成员
	public static final String muc_member_not_exist_msg = "The members of the current group does not exist.";

	public static final String muc_member_not_group_code = "1107"; //成员没有群列表
	public static final String muc_member_not_group_msg = "No group members list.";

	public static final String muc_group_name_same_code = "1108"; //群名称修改时与以前的相同
	public static final String muc_group_name_same_msg = "When the group name is the same as before modification.";
	
	public static final String muc_invite_member_max_code = "1109"; //邀请人数已超出邀请上限,请重新邀请
	public static final String muc_invite_member_max_msg = "The number has exceeded the upper limit of the invitation invitation, please re-invite.";
	
	public static final String muc_member_repeat_join_group_code = "1110"; //成员重复加入群  、成员已存在与群里
	public static final String muc_member_repeat_join_group_msg = "Repeat to join the group members.";
	
	public static final String muc_no_owner_privilege = "1113"; // 没有群主的权限 
	public static final String muc_no_owner_privilege_msg = "the user no owner privilege.";
	
	public static final String muc_not_kick_owner = "999";
	public static final String muc_not_kick_owner_msg = "member not kick owner";
	
	public static final String muc_group_breakup = "998";
	public static final String muc_group_breakup_msg = "the group is breakup";
	
	public static final String not_support_this_type = "997";
	public static final String not_support_this_type_msg = "the group is breakup";
	
	public static final String msg_recall_timeout_or_notmesend = "996";
	public static final String msg_recall_timeout_or_notmesend_msg = "this recall msg is timeout or is not me send,not recall";
	
	public static final String user_not_account_manager = "995";
	public static final String user_not_account_manager_msg = "the user is not account manager";
	
	public static final String can_not_delete_the_tag_code = "1234";
	public static final String can_not_delete_the_tag_msg = "the tag have relation.can't delete.";
	
	//---------------------------------------------华丽的分割线---------------------------------------------------//
	
	public static final String key_null_code = "1111";
	public static final String key_null_msg = "The loginSession gets the key for null.";
	
	public static final String uncompress_fail_code = "1112";  //报文解压失败
	public static final String uncompress_fail_msg = "Packet decompression failure.";
	
	public static final String rymticket_bindrelation_null_code = "1116";//根据任意门给的ticket拿不到用户信息
	public static final String rymticket_bindrelation_null_msg = "rym bind relation is null, please check the ticket";
	
	public static final String rym_registuser_oncetoken_isnull_code = "1114";//任意门已注册用户未携带oncetoken
	public static final String rym_registuser_oncetoken_isnull_msg = "rym registed user login request without oncetoken, please check param.";
	
	public static final String rym_logintype_isnot_yzt_code = "1115";//调用一帐通登录回调接口，但是根据任意门主Ticket取回来的用户信息loginType!=一帐通登录
	public static final String rym_logintype_isnot_yzt_msg = "the logintype of rym user was not yzt, please check.";
	
	
	//-------------------------------------------一帐通相关验证相关的返回---------------------------------------------//
	
	public static final String YZT_LOGIN_UNKOWN_CODE = "2000";
	public static final String YZT_LOGIN_UNKOWN_MSG = "yzt valid fail. yzt system error.";
	
	public static final String YZT_LOGIN_PARAM_INVALID_CODE = "2001"; // 用户名、密码、验证码为空或者验证无效
	public static final String YZT_LOGIN_PARAM_INVALID_MSG = "yzt valid back, account||password||vcode is null.";
	
	public static final String YZT_LOGIN_PWD_INVALID_CODE = "2002"; // 用户名密码不正确
	public static final String YZT_LOGIN_PWD_INVALID_MSG = "yzt valid back, username or pwd is invalid.";
	
	public static final String YZT_LOGIN_TOKEN_INVALID_CODE = "2003"; // 验签失败
	public static final String YZT_LOGIN_TOKEN_INVALID_MSG = "yzt valid back, token auth fail.";
	
	public static final String YZT_LOGIN_ACCOUNT_LOCKED_CODE = "2004"; // 账号异常
	public static final String YZT_LOGIN_ACCOUNT_LOCKED_MSG = "yzt valid back, account anomaly.";
	
	public static final String CHECK_STAFF_CODE = "2010"; // 非平安员工
	public static final String CHECK_STAFF_MSG = "is not pastaff";
	
	public static final String MAIL_SEND_COUNT_CODE = "2011"; // 邮件一天发送次数过多
	public static final String MAIL_SEND_COUNT_MSG = "mail send too many";
	
	public static final String MAIL_MEMO_UM_CODE = "2012"; // 邮件一天发送次数过多
	public static final String MAIL_MEMO_UM_MSG = "current username is not um";
	
	
	/* ==========================敏感词=======================================*/
	public static final String ASK_FORBIDDEN_WORD="2101";//含有敏感词
	public static final String ASK_FORBIDDEN_WORD_MSG="have forbidden words";//含有敏感词
	
	/* ==========================动态发表次数及时间限定=======================================*/
	public static final String MOMENTS_FREQUECY_MAX="2102";//发表评论或动态太高
	public static final String MOMENTS_FREQUECY_MAX_MSG="send moments too fast";//含有敏感词
	
	public static final String MOMENTS_PRAISE_DID="2103";//该用户已点赞
	public static final String MOMENTS_PRAISE_DID_MSG="you had praised it.";//该用户已点赞
	public static final String MOMENTS_DELETED="2104";//该动态已被删除
	public static final String MOMENTS_DELETED_MSG="the moment had be deleted.";//该动态已被删除
	public static final String MOMENTS_COMMENT_DELETED="2105";//该引用评论已被删除
	public static final String MOMENTS_COMMENT_DELETED_MSG="the quote comment had be deleted.";//该引用评论已被删除
	
	
	/* ==========================订单=======================================*/
	public static final String ORDER_STATE_NOT_EXISTS_CODE = "2201";//不存在对应状态的订单
	public static final String ORDER_STATE_IS_CREATED_CODE = "2202";//订单状态为已创建
	public static final String ORDER_STATE_IS_DEALING_CODE = "2203";//订单状态为处理中
	public static final String ORDER_STATE_IS_TO_EVALUAT_CODE = "2204";//订单状态为待评价
	public static final String ORDER_STATE_IS_EVALUATED_CODE = "2205";//订单状态为已评价
	
	
	/* ==========================askadmin=======================================*/
	public static final String account_already_exist_code = "3001";
	public static final String account_already_exist_msg = "account accountname alreay exist.";
	public static final String role_already_exist_code = "3002";
	public static final String role_already_exist_msg = "roleno or rolename alreay exist.";
	public static final String ACCOUNT_NO_PERMISSION_CODE = "3003";
	public static final String ACCOUNT_NO_PERMISSION_MSG = "accountname has no permission";
	
	public static final String SCF_EXPERT_MONTH_NO_BALANCE = "3010";
	public static final String SCF_EXPERT_MONTH_NO_BALANCE_MSG = "Part no balance ,can't operator";
	
	public static final String No_permission_code = "3020";
	public static final String No_permission_msg = "User has no permission";
	
	public static final String Login_error_code = "3030";
	public static final String Login_error_msg = "User name or password error";

	public static final String GOODS_IS_IN_USE_CODE = "3040";
	public static final String GOODS_IS_IN_USE_MSG = "goods is in use";
	
	/**
	 * 根据code获取message
	 * 
	 * @param code
	 * @return
	 */
	public static String getMsgByCode(String code) {
		String message = success_msg;
		if (code.equals(faile_code)) {
			message = faile_msg;
		} else if (code.equals(param_error_code)) {
			message = param_error_msg;
		} else if (code.equals(token_not_valid_code)) {
			message = token_not_valid_msg;
		} else if (code.equals(token_overtime_code)) {
			message = token_overtime_msg;
		} else if (code.equals(user_not_exist_code)) {
			message = user_not_exist_msg;
		} else if (code.equals(user_or_id_not_exist_code)) {
			message = user_or_id_not_exist_msg;
		} else if (code.equals(user_id_incorrect_code)) {
			message = user_id_incorrect_msg;
		} else if (code.equals(PHONE_NOT_VALID_CODE)) {
			message = PHONE_NOT_VALID_MSG;
		} else if (code.equals(redirect_wlt_failed_code)) {
			message = redirect_wlt_failed_code;
		} else if (code.equals(can_not_unbind_code)) {
			message = can_not_unbind_msg;
		} else if (code.equals(user_or_id_is_null_code)) {
			message = user_or_id_is_null_msg;
		} else if (code.equals(update_location_failed_code)) {
			message = update_location_failed_msg;
		} else if (code.equals(heartid_already_exist_code)) {
			message = heartid_already_exist_msg;
		} else if (code.equals(heartid_exist_code)) {
			message = heartid_exist_msg;
		} else if (code.equals(heartid_not_valid_code)) {
			message = heartid_not_valid_msg;
		} else if (code.equals(version_is_null_code)) {
			message = version_is_null_msg;
		} else if (code.equals(user_already_exist_code)) {
			message = user_already_exist_msg;
		} else if (code.equals(can_not_use_password_login_code)) {
			message = can_not_user_password_login_msg;
		} else if (code.equals(user_password_not_correct_code)) {
			message = user_password_not_correct_masg;
		} else if (code.equals(user_resource_not_found_code)) {
			message = user_resource_not_found_msg;
		} else if (code.equals(otp_login_sms_overdu_code)) {
			message = otp_login_sms_overdu_msg;
		} else if (code.equals(validate_code_error_code)) {
			message = validate_code_error_msg;
		} else if (code.equals(sms_otp_no_error)) {
			message = sms_otp_no_error_msg;
		} else if (code.equals(sms_validate_code_error)) {
			message = sms_validate_code_msg;
		} else if (code.equals(sms_overdue_code)) {
			message = sms_overdue_msg;
		} else if (code.equals(sms_count_over_code)) {
			message = sms_count_over_msg;
		} else if (code.equals(user_exist_code)) {
			message = user_exist_msg;
		} else if (code.equals(name_isnull_code)) {
			message = name_isnull_msg;
		} else if (code.equals(sms_type_empty_error_code)) {
			message = sms_type_empty_error_msg;
		} else if (code.equals(name_islength_code)) {
			message = name_islength_msg;
		}  else if (code.equals(param_not_legitimate_code)) {
			message = param_not_legitimate_msg;
		} else if (code.equals(sms_cache_error_code)) {
			message = sms_cache_error_msg;
		} else if (code.equals(group_not_exist_code)) {
			message = group_not_exist_msg;
		} else if (code.equals(groupaff_already_exist_code)) {
			message = groupaff_already_exist_msg;
		} else if (code.equals(time_format_error_code)) {
			message = time_format_error_msg;
		} else if (code.equals(request_not_https_code)) {
			message = request_not_https_msg;
		} else if (code.equals(user_password_not_valid_code)) {
			message = user_password_not_valid_msg;
		} else if (code.equals(param_phone_linked_with_other_account_code)) {
			message = param_phone_linked_with_other_account_msg;
		} else if (code.equals(param_self_bind_phone_code)) {
			message = param_self_bind_phone_msg;
		} else if (code.equals(param_unbind_only_login_phone_code)) {
			message = param_unbind_only_login_phone_msg;
		} else if (code.equals(user_alreay_bind_customid_code)) {
			message = user_alreay_bind_customid_msg;
		} else if (code.equals(customeid_alreay_bind_code)) {
			message = customeid_alreay_bind_msg;
		} else if (code.equals(user_status_qrytype_error_code)) {
			message = user_status_qrytype_error_msg;
		} else if (code.equals(muc_member_max_code)) {
			message = muc_member_max_msg;
		} else if (code.equals(muc_member_exist_code)) {
			message = muc_member_exist_msg;
		} else if (code.equals(usersystem_is_null_code)) {
			message = usersystem_is_null_msg;
		} else if (code.equals(muc_not_exist_code)) {
			message = muc_not_exist_msg;
		} else if (code.equals(usersystem_not_same_code)) {
			message = usersystem_not_same_msg;
		} else if (code.equals(muc_member_not_exist_code)) {
			message = muc_member_not_exist_msg;
		} else if (code.equals(muc_member_not_group_code)) {
			message = muc_member_not_group_msg;
		} else if (code.equals(muc_group_name_same_code)) {
			message = muc_group_name_same_msg;
		} else if (code.equals(usersource_is_not_exist_code)) {
			message = usersource_is_not_exist_msg;
		} else if (code.equals(validatepwd_count_max_ten)) {
			message = validatepwd_count_max_ten_msg;
		} else if (code.equals(validatecode_count_max_tm_code)) {
			message = validatecode_count_max_tm_msg;
		} else if (code.equals(validatecode_count_max_code)) {
			message = validatecode_count_max_msg;
		} else if (code.equals(validatepwd_count_to_ten)) {
			message = validatepwd_count_to_ten_msg;
		} else if (code.equals(token_not_valid_ten)) {
			message = token_not_valid_ten_msg;
		} else if (code.equals(token_not_valid_than_ten)) {
			message = token_not_valid_than_ten_msg;
		} else if (code.equals(url_not_in_whitelist)) {
			message = url_not_in_whitelist_msg;
		} else if (code.equals(key_null_code)) {
			message = key_null_msg;
		}else if(code.equals(muc_invite_member_max_code)){
			message = muc_invite_member_max_msg;
		}else if(code.equals(muc_member_repeat_join_group_code)){
			message = muc_member_repeat_join_group_msg;
		}else if (code.equals(username_in_blacklist_code)) {
			message = username_in_blacklist_msg;
		}else if (code.equals(uncompress_fail_code)) {
			message = uncompress_fail_msg;
		}else if(code.equals(muc_no_owner_privilege)){
			message = muc_no_owner_privilege_msg;
		}else if (code.equals(encryptkey_isnull_code)) {
			message = encryptkey_isnull_msg;
		}else if (code.equals(rymticket_bindrelation_null_code)) {
			message = rymticket_bindrelation_null_msg;
		}else if (code.equals(rym_registuser_oncetoken_isnull_code)) {
			message = rym_registuser_oncetoken_isnull_msg;
		}else if (code.equals(rym_logintype_isnot_yzt_code)) {
			message = rym_logintype_isnot_yzt_msg;
		}else if(code.equals(muc_not_kick_owner)){
			message = muc_not_kick_owner_msg;
		}else if (code.equals(YZT_LOGIN_PWD_INVALID_CODE)) {
			message = YZT_LOGIN_PWD_INVALID_MSG;
		}else if (code.equals(YZT_LOGIN_TOKEN_INVALID_CODE)) {
			message = YZT_LOGIN_TOKEN_INVALID_MSG;
		}else if (code.equals(YZT_LOGIN_PARAM_INVALID_CODE)) {
			message = YZT_LOGIN_PARAM_INVALID_MSG;
		}else if (code.equals(YZT_LOGIN_UNKOWN_CODE)) {
			message = YZT_LOGIN_UNKOWN_MSG;
		}else if (code.equals(YZT_LOGIN_ACCOUNT_LOCKED_CODE)) {
			message = YZT_LOGIN_ACCOUNT_LOCKED_MSG;
		}else if(code.equals(muc_group_breakup)){
			message = muc_group_breakup_msg;
		}else if(code.equals(CHECK_STAFF_CODE)){
			message = CHECK_STAFF_MSG;
		}else if(code.equals(MAIL_SEND_COUNT_CODE)){
			message = MAIL_SEND_COUNT_MSG;
		}else if(code.equals(MAIL_MEMO_UM_CODE)){
			message = MAIL_MEMO_UM_MSG;
		}else if(code.equals(user_lock_can_not_login_code)){
			message = user_lock_can_not_login_msg;
		}else if(code.equals(not_support_this_type)){
			message = not_support_this_type_msg;
		}else if(code.equals(msg_recall_timeout_or_notmesend)){
			message = msg_recall_timeout_or_notmesend_msg;
		}else if(code.equals(user_not_account_manager)){
			message = user_not_account_manager_msg;
		}else if(code.equals(ASK_FORBIDDEN_WORD)){
			message = ASK_FORBIDDEN_WORD_MSG;
		}else if(code.equals(MOMENTS_FREQUECY_MAX)){
			message= MOMENTS_FREQUECY_MAX_MSG;
		}else if(code.equals(MOMENTS_PRAISE_DID)){
			message= MOMENTS_PRAISE_DID_MSG;
		}else if(code.equals(MOMENTS_DELETED)){
			message= MOMENTS_DELETED_MSG;
		}else if(code.equals(MOMENTS_COMMENT_DELETED)){
			message= MOMENTS_COMMENT_DELETED_MSG;
		}else if(code.equals(account_already_exist_code)){
			message= account_already_exist_msg;
		}else if(code.equals(role_already_exist_code)){
			message= role_already_exist_msg;
		}else if(code.equals(SCF_EXPERT_MONTH_NO_BALANCE)){
			message= SCF_EXPERT_MONTH_NO_BALANCE_MSG;
		}else if(code.equals(No_permission_code)){
			message= No_permission_msg;
		}else if(code.equals(can_not_delete_the_tag_code)){
			message = can_not_delete_the_tag_msg;
		}else if(code.equals(Login_error_code)){
			message = Login_error_msg;
		}else if(code.equals(ACCOUNT_NO_PERMISSION_CODE)){
			message = ACCOUNT_NO_PERMISSION_MSG;
		}else if(code.equals(GOODS_IS_IN_USE_CODE)){
			message = GOODS_IS_IN_USE_MSG;
		}
		
		return message;
	}

}

