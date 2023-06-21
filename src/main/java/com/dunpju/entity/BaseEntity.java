package com.dunpju.entity;

import lombok.Data;

@Data
public abstract class BaseEntity implements IEntity {
    protected IFlag $_flag;

    public IFlag Flag() {
        return $_flag;
    }

    public void SetFlag(IFlag flag) {
        this.$_flag = flag;
    }
}
