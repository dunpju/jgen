package io.dunpju.orm;

import java.io.Serializable;

public interface IModel extends Serializable {
    public String tableName();
}
