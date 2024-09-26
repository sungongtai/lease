package com.atguigu.lease.web.app.mapper;

import com.atguigu.lease.model.entity.LabelInfo;
import com.atguigu.lease.model.entity.RoomLabel;
import com.atguigu.lease.web.app.vo.attr.AttrValueVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author liubo
* @description 针对表【room_label(房间&标签关联表)】的数据库操作Mapper
* @createDate 2023-07-26 11:12:39
* @Entity com.atguigu.lease.model.entity.RoomLabel
*/
public interface RoomLabelMapper extends BaseMapper<RoomLabel> {

    List<LabelInfo> getLabelInfoByRoomId(Long id);
}




