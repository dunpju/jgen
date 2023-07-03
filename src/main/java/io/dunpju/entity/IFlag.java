package io.dunpju.entity;

public interface IFlag {
    default void Apply(IEntity entity) {
        entity.SetFlag(this);
    }
}
