package io.dunpju.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.dunpju.errcode.ICode;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

@Data
public class Response<T> implements Serializable {
    private Integer code;
    private T data;
    private String msg;

    public static <T> Response<T> error(Response<?> result) {
        return error(result.getCode(), result.getMsg());
    }

    public static <T> Response<T> error(Integer code, String message) {
        Response<T> result = new Response<>();
        result.code = code;
        result.msg = message;
        return result;
    }

    public static <T> Response<T> error(ICode iCode) {
        return error(iCode.getCode(), iCode.getMsg());
    }

    public static <T> Response<T> success(T data) {
        Response<T> result = new Response<>();
        result.code = 200;
        result.data = data;
        result.msg = "成功";
        return result;
    }

    public static <T> Response<T> success() {
        Response<T> result = new Response<>();
        result.code = 200;
        result.msg = "成功";
        return result;
    }

    public static <T, R> Response<R> success(T data, Function<T, R> mapper) {
        return success(mapper.apply(data));
    }

    public static boolean isSuccess(Integer code) {
        return Objects.equals(code, 200);
    }

    @JsonIgnore
    public boolean isSuccess() {
        try {
            return isSuccess(code);
        } catch (Exception e) {
            return false;
        }
    }

    @JsonIgnore
    public boolean isError() {
        return !isSuccess();
    }
}
