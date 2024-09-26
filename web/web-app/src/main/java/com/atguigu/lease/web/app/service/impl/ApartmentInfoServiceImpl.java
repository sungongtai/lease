package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.model.entity.ApartmentInfo;
import com.atguigu.lease.model.entity.FacilityInfo;
import com.atguigu.lease.model.entity.LabelInfo;
import com.atguigu.lease.web.app.mapper.*;
import com.atguigu.lease.web.app.service.ApartmentInfoService;
import com.atguigu.lease.web.app.vo.apartment.ApartmentDetailVo;
import com.atguigu.lease.web.app.vo.graph.GraphVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    @Autowired
    private RoomInfoMapper roomInfoMapper;

    @Autowired
    private GraphInfoMapper graphInfoMapper;

    @Autowired
    private ApartmentLabelMapper apartmentLabelMapper;

    @Autowired
    private ApartmentFacilityMapper apartmentFacilityMapper;

    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        ApartmentInfo apartmentInfo = super.getById(id);

        List<GraphVo> graphVoByApartmentId = graphInfoMapper.getGraphVoByApartmentId(id);

        List<LabelInfo> labelInfoList = apartmentLabelMapper.getByApartmentId(id);

        List<FacilityInfo> facilityInfoList = apartmentFacilityMapper.getFacilityByApartmentId(id);

        BigDecimal minRentByApartmentId = roomInfoMapper.getMinRentByApartmentId(id);

        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
        BeanUtils.copyProperties(apartmentInfo, apartmentDetailVo);
        apartmentDetailVo.setGraphVoList(graphVoByApartmentId);
        apartmentDetailVo.setLabelInfoList(labelInfoList);
        apartmentDetailVo.setFacilityInfoList(facilityInfoList);
        apartmentDetailVo.setMinRent(minRentByApartmentId);

        return apartmentDetailVo;
    }
}




