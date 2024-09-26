package com.atguigu.lease.web.app.mapper;

import com.atguigu.lease.model.entity.PaymentType;
import com.atguigu.lease.model.entity.RoomPaymentType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author liubo
* @description 针对表【room_payment_type(房间&支付方式关联表)】的数据库操作Mapper
* @createDate 2023-07-26 11:12:39
* @Entity com.atguigu.lease.model.entity.RoomPaymentType
*/
public interface RoomPaymentTypeMapper extends BaseMapper<RoomPaymentType> {

    List<PaymentType> getPaymentTypeById(Long id);
}




