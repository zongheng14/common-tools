package com.human.tools.enums;

/**
 * 示例：保单状态枚举类
 *
 * @author shaonan.hu
 * @version V1.0
 * @Time 2019/4/9
 */
public enum PolicyStatusEnum {

    NO_INSURANCE("0", "未承保/投保受理中"),
    WAIT_FOR_PAY("1", "待支付"),
    SUCCESS_PAY("2", "已支付/待确认"),
    ACCEPT_INSURANCE("3", "已承保"),
    POLICY_STATUSSE("4", "已失效"),
    FAIL_INSURANCE("5", "承保失败"),
    POLICY_STATUSFI("6", "承保中"),
    POLICY_STATUSSI("7", "缴费逾期"),
    RETURN_INSURANCE("8", "已退保"),
    UNDERWRITING_FIELD("9", "核保失败"),
    UNDERWRITING_EXCEPTION("12", "核保异常"),
    PAY_FIELD("10", "支付失败"),
    PROTECTING("11", "保障中");

    private PolicyStatusEnum(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private String status;
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
