package io.dunpju.errcode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CodeException extends RuntimeException {
    private int code;
    private String msg;

    public CodeException(String msg) {
        super(msg);
        this.code = 0;
        this.msg = msg;
    }

    public CodeException(Code code) {
        super(code.getMessage());
        this.setCode(code.getCode());
        this.setMsg(code.getMessage());
    }

    public CodeException(int code, String msg) {
        super(msg);
        this.setCode(code);
        this.setMsg(msg);
    }
}
