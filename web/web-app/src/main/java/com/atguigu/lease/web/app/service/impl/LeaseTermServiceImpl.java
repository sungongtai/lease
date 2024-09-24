package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.model.entity.LeaseTerm;
import com.atguigu.lease.web.app.mapper.LeaseTermMapper;
import com.atguigu.lease.web.app.service.LeaseTermService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【lease_term(租期)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class LeaseTermServiceImpl extends ServiceImpl<LeaseTermMapper, LeaseTerm>
        implements LeaseTermService {

}




