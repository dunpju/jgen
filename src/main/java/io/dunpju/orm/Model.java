package io.dunpju.orm;

public interface Model {
    public void setTableAlias(String alias);

    public String getTableAlias();
}
