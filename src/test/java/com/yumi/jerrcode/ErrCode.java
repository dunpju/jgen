package com.yumi.jerrcode;

import io.dunpju.errcode.Code;

public class ErrCode {
    public final static Code SUCCESS = new Code(200, "成功");
    public final static Code FAIL = new Code(400, "失败");
    public final static Code NEED_LOGIN = new Code(401, "need_login错误");
    public final static Code SERVER_ERROR = new Code(500, "服务器开小差了");
    public final static Code PRIMARY_ID_ERROR = new Code(200000, "主键%s错误");
    public final static Code NOT_EXIST_ERROR = new Code(200001, "%s不存在");
    public final static Code SMS_TEMPLE_CODE_ERROR = new Code(200002, "短信模板code未定义");
    public final static Code SMS_CODE_ERROR = new Code(200003, "短信码错误");
    public final static Code SMS_SEND_ERROR = new Code(200004, "短信码发送失败");
    public final static Code SMS_SEND_LIMIT_ERROR = new Code(200005, "获取验证码太频繁");
    public final static Code AFFILIATION_ERROR = new Code(200006, "%s不归属您,无权限修改");
    public final static Code DESC_ERROR = new Code(200007, "desc错误");

}
