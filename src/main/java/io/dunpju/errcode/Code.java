package io.dunpju.errcode;

import lombok.Data;

@Data
public class Code {
    private final Integer code;
    private final String message;

    public Code(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String format(String tips) {
        return String.format(this.message, tips);
    }
}
