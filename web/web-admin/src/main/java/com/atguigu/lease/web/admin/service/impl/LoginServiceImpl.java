package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.common.constant.RedisConstant;
import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.result.ResultCodeEnum;
import com.atguigu.lease.common.utils.JwtUtil;
import com.atguigu.lease.model.entity.SystemUser;
import com.atguigu.lease.model.enums.BaseStatus;
import com.atguigu.lease.web.admin.mapper.SystemUserMapper;
import com.atguigu.lease.web.admin.service.LoginService;
import com.atguigu.lease.web.admin.vo.login.CaptchaVo;
import com.atguigu.lease.web.admin.vo.login.LoginVo;
import com.atguigu.lease.web.admin.vo.system.user.SystemUserInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wf.captcha.SpecCaptcha;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    private SystemUserMapper systemUserMapper;

    @Override
    public CaptchaVo getCaptcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);

        String code = specCaptcha.text().toLowerCase();

        String key = RedisConstant.ADMIN_LOGIN_PREFIX + UUID.randomUUID();

        stringRedisTemplate.opsForValue().set(key, code, RedisConstant.ADMIN_LOGIN_CAPTCHA_TTL_SEC, TimeUnit.SECONDS);

        return new CaptchaVo(specCaptcha.toBase64(), key);

    }

    @Override
    public String login(LoginVo loginVo) {
        // 验证码校验
        if (loginVo.getCaptchaCode() == null){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND);
        }

        String code = stringRedisTemplate.opsForValue().get(loginVo.getCaptchaKey());

        if (code == null){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_EXPIRED);
        }

        if (!code.equals(loginVo.getCaptchaCode().toLowerCase())){
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_ERROR);
        }

        // 用户名校验
        SystemUser systemUser = systemUserMapper.selectOneByUsername(loginVo.getUsername());

        if (systemUser == null) {
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
        }

        if(systemUser.getStatus() == BaseStatus.DISABLE){
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_DISABLED_ERROR);
        }

        // 密码校验
        if (!systemUser.getPassword().equals(DigestUtils.md5Hex(loginVo.getPassword()))){
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);
        }


        return JwtUtil.createToken(systemUser.getId(), systemUser.getUsername());

    }

    @Override
    public SystemUserInfoVo getLoginUserInfoById(Long userId) {
        SystemUser systemUser = systemUserMapper.selectById(userId);

        SystemUserInfoVo systemUserInfoVo = new SystemUserInfoVo();
        systemUserInfoVo.setName(systemUser.getName());
        systemUserInfoVo.setAvatarUrl(systemUser.getAvatarUrl());
        return systemUserInfoVo;
    }
}
