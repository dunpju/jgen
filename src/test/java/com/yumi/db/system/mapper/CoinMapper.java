package com.yumi.db.system.mapper;

import com.dunpju.orm.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.yumi.db.system.model.Coin;

@Mapper
public interface CoinMapper extends BaseMapper<Coin> {
}