package com.yumi.db.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.dunpju.annotations.Message;
import io.dunpju.orm.BaseField;
import io.dunpju.orm.BaseModel;
import lombok.Data;

import java.io.Serial;


@TableName("ts_coin")
@Message(value = "")
@Data
public class Coin extends BaseModel {

    @Serial
    private static final long serialVersionUID = 1L;

    public enum FIELD implements BaseField {
        id,
        uname,
        coin;
    }

    @Message("")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @Message("")
    private String uname;
    @Message("")
    private Integer coin;

    @Override
    public String toString() {
        return "Coin{" +
            "id = " + id +
            ", uname = " + uname +
            ", coin = " + coin +
        "}";
    }
}