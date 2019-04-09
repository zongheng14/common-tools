package com.human.tools.responseBean;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;

public class ResponseData {
    private int errcode;
    private String errmsg;
    private Object data;

    private ResponseData(ErrorCodeEnum errorCodeEnum, Object data) {
        this.errcode = errorCodeEnum.getErrCode();
        this.errmsg = errorCodeEnum.getErrName();
        this.data = data;
    }

    public static ResponseData success(Object data) {
        return new ResponseData(ErrorCodeEnum.ERR_200, data);
    }

    public static ResponseData fail(ErrorCodeEnum errorCodeEnum) {
        return new ResponseData(errorCodeEnum, (Object)null);
    }

    public static ResponseData fail(ErrorCodeEnum errorCodeEnum, Object errorData) {
        return new ResponseData(errorCodeEnum, errorData);
    }

    public int getErrcode() {
        return this.errcode;
    }

    public String getErrmsg() {
        return this.errmsg;
    }

    public Object getData() {
        return this.data;
    }

    public String toString() {
        if (this.data == null) {
            this.data = new ArrayList();
        }

        if (this.data != null && this.data instanceof String) {
            return "{\"errcode\":" + this.errcode + ",\"errmsg\":\"" + this.errmsg + '"' + ", \"data\":\"" + this.data + "\"}";
        } else {
            if (!(this.data instanceof String)) {
                this.data = JSON.toJSON(this.data);
            }

            return "{\"errcode\":" + this.errcode + ",\"errmsg\":\"" + this.errmsg + '"' + ", \"data\":" + this.data + '}';
        }
    }
}

