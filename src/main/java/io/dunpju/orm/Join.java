package io.dunpju.orm;

import lombok.Data;

public class Join {
    private String _table;
    private String _alias;

    public String getTable() {
        return _table;
    }

    public void setTable(String _table) {
        this._table = _table;
    }

    public String getAlias() {
        return _alias;
    }

    public Join AS(String alias) {
        this._alias = alias;
        this._table = this._table.replaceAll("\\s+AS\\s+\\w+", "");
        this._table = String.format("%s AS %s", this._table, alias);
        return this;
    }

    public String ON(String first, String operator, String second, String... other) {
        return String.format(" %s ON %s %s %s %s", this._table, first, operator, second, String.join(" ", other));
    }
}
