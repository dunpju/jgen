package io.dunpju.orm;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serial;
import java.io.Serializable;

public abstract class BaseModel implements Serializable {
    @Serial
    @TableField(select = false)
    private static final long serialVersionUID = 1L;
    @TableField(select = false)
    private String _tableAlias;

    public String getTableAlias() {
        return _tableAlias;
    }

    public void setTableAlias(String alias) {
        this._tableAlias = alias;
    }
}
