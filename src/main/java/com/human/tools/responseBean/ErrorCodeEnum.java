package com.human.tools.responseBean;

public enum ErrorCodeEnum {
    ERR_200(200, "成功", "ok"),
    ERR_500(500, "系统异常", "Server Internal Error"),
    ERR_1001(1001, "请求参数缺少必填值！", "The parameter value is empty"),
    ERR_1002(1002, "请求参数缺少必要参数", "Missing transfer parameters"),
    ERR_1003(1003, "接口未授权", "api unauthorized"),
    ERR_1004(1004, "不允许重复操作", "");

    private int errCode;
    private String errName;
    private String errName_en;

    private ErrorCodeEnum(int errCode, String errName, String errName_en) {
        this.errCode = errCode;
        this.errName = errName;
        this.errName_en = errName_en;
    }

    public int getErrCode() {
        return this.errCode;
    }

    public String getErrName() {
        return this.errName;
    }

    public String getErrName_en() {
        return this.errName_en;
    }
}
