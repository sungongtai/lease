package com.atguigu.lease.web.app.mapper;

import com.atguigu.lease.model.entity.FacilityInfo;
import com.atguigu.lease.model.entity.RoomFacility;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author liubo
* @description 针对表【room_facility(房间&配套关联表)】的数据库操作Mapper
* @createDate 2023-07-26 11:12:39
* @Entity com.atguigu.lease.model.entity.RoomFacility
*/
public interface RoomFacilityMapper extends BaseMapper<RoomFacility> {

    List<FacilityInfo> getFacilityInfoById(Long id);
}




