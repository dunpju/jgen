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

    public Code Format(String tips) {
        return new Code(this.code, String.format(this.message, tips));
    }

    public Code Format() {
        return new Code(this.code, this.message);
    }

    public void Throw(String tips) {
        throw new CodeException(this.code, String.format(this.message, tips));
    }

    public void Throw() {
        throw new CodeException(this.code, this.message);
    }
}
