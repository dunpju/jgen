package com.yumi.db.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.dunpju.annotations.Message;
import io.dunpju.orm.BaseField;
import io.dunpju.orm.BaseModel;
import lombok.Data;

import java.io.Serial;


@TableName("ts_user")
@Message(value = "")
@Data
public class User extends BaseModel {

    @Serial
    private static final long serialVersionUID = 1L;

    public enum FIELD implements BaseField {
        id,
        uname,
        u_tel,
        score;
    }

    @Message("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @Message("用户姓名")
    private String uname;
    @Message("")
    private String uTel;
    @Message("")
    private Integer score;

    @Override
    public String toString() {
        return "User{" +
            "id = " + id +
            ", uname = " + uname +
            ", uTel = " + uTel +
            ", score = " + score +
        "}";
    }
}