package com.yumi.enums;

/**
 * 是否
 */
public enum EnumYesOrNo {
    NO(1, "否"),
    YES(2, "是");

    private final int code;
    private final String message;

    EnumYesOrNo(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

}