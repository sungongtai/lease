package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.model.entity.ApartmentInfo;
import com.atguigu.lease.model.entity.LeaseAgreement;
import com.atguigu.lease.model.entity.LeaseTerm;
import com.atguigu.lease.model.entity.PaymentType;
import com.atguigu.lease.model.enums.LeaseStatus;
import com.atguigu.lease.web.app.mapper.LeaseAgreementMapper;
import com.atguigu.lease.web.app.service.*;
import com.atguigu.lease.web.app.vo.agreement.AgreementDetailVo;
import com.atguigu.lease.web.app.vo.agreement.AgreementItemVo;
import com.atguigu.lease.web.app.vo.apartment.ApartmentDetailVo;
import com.atguigu.lease.web.app.vo.room.RoomDetailVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class LeaseAgreementServiceImpl extends ServiceImpl<LeaseAgreementMapper, LeaseAgreement>
        implements LeaseAgreementService {

    @Autowired
    private LeaseAgreementMapper leaseAgreementMapper;

    @Autowired
    private RoomInfoService roomInfoService;

    @Autowired
    private ApartmentInfoService apartmentInfoService;

    @Autowired
    private PaymentTypeService paymentTypeService;

    @Autowired
    private LeaseTermService leaseTermService;

    @Override
    public List<AgreementItemVo> listItemByPhone(String phone) {
        return leaseAgreementMapper.listItemByPhone(phone);
    }

    @Override
    public AgreementDetailVo getDetailById(Long id) {
        AgreementDetailVo result = new AgreementDetailVo();
        LeaseAgreement leaseAgreement = leaseAgreementMapper.selectById(id);

        RoomDetailVo roomDetailVo = roomInfoService.getDetailById(leaseAgreement.getRoomId());
        ApartmentDetailVo apartmentDetailVo = apartmentInfoService.getDetailById(leaseAgreement.getApartmentId());
        PaymentType paymentType = paymentTypeService.getById(leaseAgreement.getPaymentTypeId());
        LeaseTerm leaseTerm = leaseTermService.getById(leaseAgreement.getLeaseTermId());

        BeanUtils.copyProperties(leaseAgreement, result);
        result.setApartmentName(apartmentDetailVo.getName());
        result.setApartmentGraphVoList(apartmentDetailVo.getGraphVoList());
        result.setRoomNumber(roomDetailVo.getRoomNumber());
        result.setRoomGraphVoList(roomDetailVo.getGraphVoList());
        result.setPaymentTypeName(paymentType.getName());
        result.setLeaseTermMonthCount(leaseTerm.getMonthCount());
        result.setLeaseTermUnit(leaseTerm.getUnit());

        return result;
    }

    @Override
    public void updateStatusById(Long id, LeaseStatus leaseStatus) {
        LambdaUpdateWrapper<LeaseAgreement> leaseAgreementLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        leaseAgreementLambdaUpdateWrapper.eq(LeaseAgreement::getId, id).set(LeaseAgreement::getStatus, leaseStatus);
        super.update(leaseAgreementLambdaUpdateWrapper);
    }
}




