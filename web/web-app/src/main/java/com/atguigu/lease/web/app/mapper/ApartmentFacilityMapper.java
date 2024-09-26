package com.atguigu.lease.web.app.mapper;

import com.atguigu.lease.model.entity.ApartmentFacility;
import com.atguigu.lease.model.entity.FacilityInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author liubo
* @description 针对表【apartment_facility(公寓&配套关联表)】的数据库操作Mapper
* @createDate 2023-07-26 11:12:39
* @Entity com.atguigu.lease.model.entity.ApartmentFacility
*/
public interface ApartmentFacilityMapper extends BaseMapper<ApartmentFacility> {

    List<FacilityInfo> getFacilityByApartmentId(Long id);
}




