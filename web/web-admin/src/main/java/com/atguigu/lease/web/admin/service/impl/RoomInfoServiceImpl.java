package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.model.entity.*;
import com.atguigu.lease.model.enums.ItemType;
import com.atguigu.lease.web.admin.mapper.*;
import com.atguigu.lease.web.admin.service.*;
import com.atguigu.lease.web.admin.vo.attr.AttrValueVo;
import com.atguigu.lease.web.admin.vo.graph.GraphVo;
import com.atguigu.lease.web.admin.vo.room.RoomDetailVo;
import com.atguigu.lease.web.admin.vo.room.RoomItemVo;
import com.atguigu.lease.web.admin.vo.room.RoomQueryVo;
import com.atguigu.lease.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {

    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;

    @Autowired
    private GraphInfoMapper graphInfoMapper;

    @Autowired
    private RoomAttrValueMapper roomAttrValueMapper;

    @Autowired
    private RoomFacilityMapper roomFacilityMapper;

    @Autowired
    private RoomLabelMapper roomLabelMapper;

    @Autowired
    private RoomPaymentTypeMapper roomPaymentTypeMapper;

    @Autowired
    private RoomLeaseTermMapper roomLeaseTermMapper;

    @Autowired
    private GraphInfoService graphInfoService;

    @Autowired
    private RoomAttrValueService roomAttrValueService;

    @Autowired
    private RoomFacilityServiceImpl roomFacilityService;

    @Autowired
    private RoomLabelService roomLabelService;

    @Autowired
    private RoomPaymentTypeService roomPaymentTypeService;

    @Autowired
    private RoomLeaseTermService roomLeaseTermService;

    @Override
    public void saveOrUpdateRoom(RoomSubmitVo roomSubmitVo) {
        boolean isUpdate = roomSubmitVo.getId() != null;
        super.saveOrUpdate(roomSubmitVo);

        if (isUpdate) {
            // 删除图片列表
            LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.ROOM).eq(GraphInfo::getItemId, roomSubmitVo.getId());
            graphInfoService.remove(graphInfoLambdaQueryWrapper);

            // 删除属性信息列表
            LambdaQueryWrapper<RoomAttrValue> roomAttrValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomAttrValueLambdaQueryWrapper.eq(RoomAttrValue::getRoomId, roomSubmitVo.getId());
            roomAttrValueService.remove(roomAttrValueLambdaQueryWrapper);

            // 删除配套信息列表
            LambdaQueryWrapper<RoomFacility> roomFacilityLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomFacilityLambdaQueryWrapper.eq(RoomFacility::getRoomId, roomSubmitVo.getId());
            roomFacilityService.remove(roomFacilityLambdaQueryWrapper);

            // 删除标签信息列表
            LambdaQueryWrapper<RoomLabel> roomLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomLabelLambdaQueryWrapper.eq(RoomLabel::getRoomId, roomSubmitVo.getId());
            roomLabelService.remove(roomLabelLambdaQueryWrapper);

            // 删除支付方式列表
            LambdaQueryWrapper<RoomPaymentType> roomPaymentTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomPaymentTypeLambdaQueryWrapper.eq(RoomPaymentType::getRoomId, roomSubmitVo.getId());
            roomPaymentTypeService.remove(roomPaymentTypeLambdaQueryWrapper);

            // 删除可选租期列表
            LambdaQueryWrapper<RoomLeaseTerm> roomLeaseTermLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roomLeaseTermLambdaQueryWrapper.eq(RoomLeaseTerm::getRoomId, roomSubmitVo.getId());
            roomLeaseTermService.remove(roomLeaseTermLambdaQueryWrapper);
        }

        // 更新图片列表
        List<GraphVo> graphVoList = roomSubmitVo.getGraphVoList();
        if(!CollectionUtils.isEmpty(graphVoList)){
            List<GraphInfo> graphInfoList = new ArrayList<>();
            for (GraphVo graphVo : graphVoList) {
                GraphInfo graphInfo = new GraphInfo();
                graphInfo.setName(graphVo.getName());
                graphInfo.setUrl(graphVo.getUrl());
                graphInfo.setItemId(roomSubmitVo.getId());
                graphInfo.setItemType(ItemType.ROOM);
                graphInfoList.add(graphInfo);
            }
            graphInfoService.saveBatch(graphInfoList);
        }



        // 更新属性信息列表
        List<Long> roomSubmitVoAttrValueIds = roomSubmitVo.getAttrValueIds();
        if(!CollectionUtils.isEmpty(roomSubmitVoAttrValueIds)){
            List<RoomAttrValue> list = new ArrayList<>();
            for (Long attrValueId : roomSubmitVoAttrValueIds) {
                RoomAttrValue roomAttrValue = RoomAttrValue.builder().attrValueId(attrValueId).roomId(roomSubmitVo.getId()).build();
                list.add(roomAttrValue);
            }
            roomAttrValueService.saveBatch(list);
        }

        // 更新配套信息列表
        List<Long> facilityInfoIds = roomSubmitVo.getFacilityInfoIds();
        if (!CollectionUtils.isEmpty(facilityInfoIds)){
            List<RoomFacility> list = new ArrayList<>();
            for(Long facilityInfoId : facilityInfoIds){
                RoomFacility roomFacility = RoomFacility.builder().facilityId(facilityInfoId).roomId(roomSubmitVo.getId()).build();
                list.add(roomFacility);
            }
            roomFacilityService.saveBatch(list);
        }

        // 更新标签信息列表
        List<Long> labelInfoIds = roomSubmitVo.getLabelInfoIds();
        if (!CollectionUtils.isEmpty(labelInfoIds)){
            List<RoomLabel> list = new ArrayList<>();
            for(Long labelInfoId : labelInfoIds){
                RoomLabel roomLabel = RoomLabel.builder().labelId(labelInfoId).roomId(roomSubmitVo.getId()).build();
                list.add(roomLabel);
            }
            roomLabelService.saveBatch(list);
        }

        // 更新支付方式列表
        List<Long> paymentTypeIds = roomSubmitVo.getPaymentTypeIds();
        if (!CollectionUtils.isEmpty(paymentTypeIds)){
            List<RoomPaymentType> list = new ArrayList<>();
            for(Long paymentTypeId : paymentTypeIds){
                RoomPaymentType roomPaymentType = RoomPaymentType.builder().paymentTypeId(paymentTypeId).roomId(roomSubmitVo.getId()).build();
                list.add(roomPaymentType);
            }
            roomPaymentTypeService.saveBatch(list);
        }

        // 更新可选租期列表
        List<Long> leaseTermIds = roomSubmitVo.getLeaseTermIds();
        if (!CollectionUtils.isEmpty(leaseTermIds)){
            List<RoomLeaseTerm> list = new ArrayList<>();
            for(Long leaseTermId : leaseTermIds){
                RoomLeaseTerm roomLeaseTerm = RoomLeaseTerm.builder().leaseTermId(leaseTermId).roomId(roomSubmitVo.getId()).build();
                list.add(roomLeaseTerm);
            }
            roomLeaseTermService.saveBatch(list);
        }

    }

    @Override
    public IPage<RoomItemVo> pageItem(Page<RoomItemVo> roomItemVoPage, RoomQueryVo queryVo) {
        return roomInfoMapper.pageItem(roomItemVoPage, queryVo);
    }

    @Override
    public RoomDetailVo getDetailById(Long id) {
        RoomDetailVo roomDetailVo = new RoomDetailVo();

        RoomInfo roomInfo = super.getById(id);
        BeanUtils.copyProperties(roomInfo, roomDetailVo);

        // 获取公寓信息
        LambdaQueryWrapper<ApartmentInfo> apartmentInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        apartmentInfoLambdaQueryWrapper.eq(ApartmentInfo::getId, roomInfo.getApartmentId());
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectOne(apartmentInfoLambdaQueryWrapper);
        roomDetailVo.setApartmentInfo(apartmentInfo);

        // 获取图片列表
        List<GraphVo> graphVoList = graphInfoMapper.selectByItemTypeAndId(ItemType.ROOM, id);
        roomDetailVo.setGraphVoList(graphVoList);

        // 获取属性
        List<AttrValueVo> attrValueVoList = roomAttrValueMapper.getAttrValueVoByRoomId(id);
        roomDetailVo.setAttrValueVoList(attrValueVoList);

        // 获取配套
        List<FacilityInfo> facilityInfoList = roomFacilityMapper.getFacilityInfoByRoomId(id);
        roomDetailVo.setFacilityInfoList(facilityInfoList);

        // 获取标签
        List<LabelInfo> labelInfoList = roomLabelMapper.getLabelInfoByRoomId(id);
        roomDetailVo.setLabelInfoList(labelInfoList);
        
        // 获取支付方式
        List<PaymentType> paymentTypeList = roomPaymentTypeMapper.getPaymentTypeByRoomId(id);
        roomDetailVo.setPaymentTypeList(paymentTypeList);

        // 获取可选租期
        List<LeaseTerm> leaseTermList = roomLeaseTermMapper.getLeaseTermByRoomId(id);
        roomDetailVo.setLeaseTermList(leaseTermList);

        return roomDetailVo;
    }

    @Override
    public void removeRoomById(Long id) {
        // 删除图片列表
        LambdaQueryWrapper<GraphInfo> graphInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        graphInfoLambdaQueryWrapper.eq(GraphInfo::getItemType, ItemType.ROOM).eq(GraphInfo::getItemId, id);
        graphInfoService.remove(graphInfoLambdaQueryWrapper);

        // 删除属性信息列表
        LambdaQueryWrapper<RoomAttrValue> roomAttrValueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomAttrValueLambdaQueryWrapper.eq(RoomAttrValue::getRoomId, id);
        roomAttrValueService.remove(roomAttrValueLambdaQueryWrapper);

        // 删除配套信息列表
        LambdaQueryWrapper<RoomFacility> roomFacilityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomFacilityLambdaQueryWrapper.eq(RoomFacility::getRoomId, id);
        roomFacilityService.remove(roomFacilityLambdaQueryWrapper);

        // 删除标签信息列表
        LambdaQueryWrapper<RoomLabel> roomLabelLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomLabelLambdaQueryWrapper.eq(RoomLabel::getRoomId, id);
        roomLabelService.remove(roomLabelLambdaQueryWrapper);

        // 删除支付方式列表
        LambdaQueryWrapper<RoomPaymentType> roomPaymentTypeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomPaymentTypeLambdaQueryWrapper.eq(RoomPaymentType::getRoomId, id);
        roomPaymentTypeService.remove(roomPaymentTypeLambdaQueryWrapper);

        // 删除可选租期列表
        LambdaQueryWrapper<RoomLeaseTerm> roomLeaseTermLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roomLeaseTermLambdaQueryWrapper.eq(RoomLeaseTerm::getRoomId, id);
        roomLeaseTermService.remove(roomLeaseTermLambdaQueryWrapper);

        // 删除房间
        super.removeById(id);

    }
}




