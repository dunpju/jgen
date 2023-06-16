package com.dunpju.orm;

import java.io.Serializable;

public interface BaseField extends Serializable {
    default public String Pre(String prefix) {
        return String.format("%s.%s", prefix, this);
    }

    default public String As(String alias) {
        return String.format("%s AS %s", this, alias);
    }

    default public String ASC() {
        return String.format("%s %s", this, "ASC");
    }

    default public String DESC() {
        return String.format("%s %s", this, "DESC");
    }
}
