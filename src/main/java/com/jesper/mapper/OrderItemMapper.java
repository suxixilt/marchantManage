package com.jesper.mapper;

import com.jesper.model.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderItemMapper {

    int deleteByPrimaryKey(String id);

    int insert(OrderItem record);

    OrderItem selectByPrimaryKey(String id);

    OrderItem selectByPrimaryOrderKey(String orderId);

    List<OrderItem> selectAll();

    int updateByPrimaryKey(OrderItem record);
}