package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.model.entity.ApartmentInfo;
import com.atguigu.lease.web.app.mapper.ApartmentInfoMapper;
import com.atguigu.lease.web.app.service.ApartmentInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {
}




