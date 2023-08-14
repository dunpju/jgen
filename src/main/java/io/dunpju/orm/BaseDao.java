package io.dunpju.orm;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
@EqualsAndHashCode(callSuper=false)
public class BaseDao <M extends IMapper<T>, T extends BaseModel> extends ServiceImpl<M, T> {
    protected T model;

    // @see https://baomidou.com/pages/f84a74/#%E6%8F%92%E5%85%A5%E6%88%96%E6%9B%B4%E6%96%B0%E7%9A%84%E5%AD%97%E6%AE%B5%E6%9C%89-%E7%A9%BA%E5%AD%97%E7%AC%A6%E4%B8%B2-%E6%88%96%E8%80%85-null
    protected LambdaUpdateWrapper<T> wrapper;

    private Builder<M, T> builder;


    public Builder<M, T> model() {
        if (null == builder) {
            builder = new Builder<>(this.baseMapper, this.model).FROM(getEntityClass());
        }
        return builder;
    }

    public Builder<M, T> AS(String alias) {
        return this.model().AS(alias);
    }
}
