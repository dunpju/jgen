package io.dunpju.orm;

import java.io.Serial;
import java.io.Serializable;

public abstract class BaseModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    protected String _tableAlias;

    public String getTableAlias() {
        return _tableAlias;
    }

    public void setTableAlias(String alias) {
        this._tableAlias = alias;
    }
}
