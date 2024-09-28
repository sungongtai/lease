package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.common.login.LoginUserHolder;
import com.atguigu.lease.model.entity.*;
import com.atguigu.lease.web.app.mapper.*;
import com.atguigu.lease.web.app.service.ApartmentInfoService;
import com.atguigu.lease.web.app.service.ApartmentLabelService;
import com.atguigu.lease.web.app.service.BrowsingHistoryService;
import com.atguigu.lease.web.app.service.RoomInfoService;
import com.atguigu.lease.web.app.vo.apartment.ApartmentItemVo;
import com.atguigu.lease.web.app.vo.attr.AttrValueVo;
import com.atguigu.lease.web.app.vo.fee.FeeValueVo;
import com.atguigu.lease.web.app.vo.graph.GraphVo;
import com.atguigu.lease.web.app.vo.room.RoomDetailVo;
import com.atguigu.lease.web.app.vo.room.RoomItemVo;
import com.atguigu.lease.web.app.vo.room.RoomQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【room_info(房间信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
@Slf4j
public class RoomInfoServiceImpl extends ServiceImpl<RoomInfoMapper, RoomInfo>
        implements RoomInfoService {
    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Autowired
    private ApartmentInfoService apartmentInfoService;

    @Autowired
    private ApartmentLabelMapper apartmentLabelMapper;

    @Autowired
    private GraphInfoMapper graphInfoMapper;

    @Autowired
    private RoomLabelMapper roomLabelMapper;

    @Autowired
    private RoomFacilityMapper roomFacilityMapper;

    @Autowired
    private RoomAttrValueMapper roomAttrValueMapper;

    @Autowired
    private RoomPaymentTypeMapper roomPaymentTypeMapper;

    @Autowired
    private FeeValueMapper feeValueMapper;

    @Autowired
    private RoomLeaseTermMapper roomLeaseTermMapper;

    @Autowired
    private BrowsingHistoryService browsingHistoryService;

    @Override
    public IPage<RoomItemVo> pageItem(Page<RoomItemVo> page, RoomQueryVo queryVo) {
        return roomInfoMapper.pageItem(page, queryVo);
    }

    @Override
    public RoomDetailVo getDetailById(Long id) {
        // 房间信息
        RoomInfo roomInfo = roomInfoMapper.selectById(id);

        // 公寓信息
        ApartmentInfo apartmentInfo = apartmentInfoService.getById(roomInfo.getApartmentId());
        ApartmentItemVo apartmentItemVo = new ApartmentItemVo();
        BeanUtils.copyProperties(apartmentInfo, apartmentItemVo);

        List<LabelInfo> labelInfoList = apartmentLabelMapper.getByApartmentId(apartmentInfo.getId());
        apartmentItemVo.setLabelInfoList(labelInfoList);

        List<GraphVo> graphVoList = graphInfoMapper.getGraphVoByApartmentId(apartmentInfo.getId());
        apartmentItemVo.setGraphVoList(graphVoList);

        BigDecimal minRent = roomInfoMapper.getMinRentByApartmentId(apartmentInfo.getId());
        apartmentItemVo.setMinRent(minRent);

        // 图片信息
        List<GraphVo> graphVoList2 = graphInfoMapper.getGraphVoByRoomId(id);

        // 属性信息
        List<AttrValueVo> attrValueVoList = roomAttrValueMapper.getAttrValueVoByRoomId(id);

        // 配套信息
        List<FacilityInfo> facilityInfoList = roomFacilityMapper.getFacilityInfoById(id);

        // 标签信息
        List<LabelInfo> labelInfoList1 = roomLabelMapper.getLabelInfoByRoomId(id);

        // 支付方式
        List<PaymentType> paymentTypeList = roomPaymentTypeMapper.getPaymentTypeById(id);

        // 杂费
        List<FeeValueVo> feeValueVoList = feeValueMapper.getFeeValueVoByApartmentId(apartmentInfo.getId());

        // 租期
        List<LeaseTerm> leaseTermList = roomLeaseTermMapper.getLeaseTermByRoomId(id);


        RoomDetailVo roomDetailVo = new RoomDetailVo();

        BeanUtils.copyProperties(roomInfo, roomDetailVo);
        roomDetailVo.setApartmentItemVo(apartmentItemVo);
        roomDetailVo.setGraphVoList(graphVoList2);
        roomDetailVo.setAttrValueVoList(attrValueVoList);
        roomDetailVo.setFacilityInfoList(facilityInfoList);
        roomDetailVo.setLabelInfoList(labelInfoList1);
        roomDetailVo.setPaymentTypeList(paymentTypeList);
        roomDetailVo.setFeeValueVoList(feeValueVoList);
        roomDetailVo.setLeaseTermList(leaseTermList);

        // 保存浏览历史
        browsingHistoryService.saveHistory(LoginUserHolder.getLoginUser().getUserId(), id);

        return roomDetailVo;
    }

    @Override
    public IPage<RoomItemVo> pageItemByApartmentId(Page<RoomItemVo> page, Long id) {
        return roomInfoMapper.pageItemByApartmentId(page, id);
    }
}




