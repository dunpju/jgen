package io.dunpju.orm;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.dunpju.utils.CommonUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author liuguoqiang
 * @Date 2025/4/27
 * @Version 1.0
 */
public class ServiceImpl<M extends IMapper<T>, T>
        extends com.baomidou.mybatisplus.extension.service.impl.ServiceImpl<M, T> implements IService<T> {

    private final static Integer DEFAULT_BATCH_SIZE = 500;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer insertOrUnionKeyUpdate(List<T> list, Integer batchSize) {
        if (CollectionUtils.isEmpty(list) || batchSize <= 0) {
            return 0;
        }
        List<List<T>> groupList = CommonUtils.splitList(list, batchSize);
        AtomicInteger result = new AtomicInteger();
        groupList.forEach(l -> result.addAndGet(this.baseMapper.insertOrUnionKeyUpdate(l)));
        return result.get();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer insertOrUnionKeyUpdate(List<T> list) {
        return insertOrUnionKeyUpdate(list, DEFAULT_BATCH_SIZE);
    }
}
