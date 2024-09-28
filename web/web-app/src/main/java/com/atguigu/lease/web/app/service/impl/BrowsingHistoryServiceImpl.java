package com.atguigu.lease.web.app.service.impl;

import com.atguigu.lease.model.entity.BrowsingHistory;
import com.atguigu.lease.web.app.mapper.BrowsingHistoryMapper;
import com.atguigu.lease.web.app.service.BrowsingHistoryService;
import com.atguigu.lease.web.app.vo.history.HistoryItemVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author liubo
 * @description 针对表【browsing_history(浏览历史)】的数据库操作Service实现
 * @createDate 2023-07-26 11:12:39
 */
@Service
public class BrowsingHistoryServiceImpl extends ServiceImpl<BrowsingHistoryMapper, BrowsingHistory>
        implements BrowsingHistoryService {
    @Autowired
    private BrowsingHistoryMapper browsingHistoryMapper;

    @Override
    public IPage<HistoryItemVo> pageItemByUserId(Page<HistoryItemVo> historyItemVoPage, Long userId) {
        return browsingHistoryMapper.pageItemByUserId(historyItemVoPage, userId);
    }

    @Override
    @Async
    public void saveHistory(Long userId, Long id) {
        LambdaQueryWrapper<BrowsingHistory> browsingHistoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        browsingHistoryLambdaQueryWrapper.eq(BrowsingHistory::getUserId, userId);
        browsingHistoryLambdaQueryWrapper.eq(BrowsingHistory::getRoomId, id);
        BrowsingHistory browsingHistory = browsingHistoryMapper.selectOne(browsingHistoryLambdaQueryWrapper);
        if (browsingHistory != null){
            browsingHistory.setBrowseTime(new Date());
            browsingHistoryMapper.updateById(browsingHistory);
        }else {
            BrowsingHistory browsingHistory1 = new BrowsingHistory();
            browsingHistory1.setUserId(userId);
            browsingHistory1.setRoomId(id);
            browsingHistory1.setBrowseTime(new Date());
            browsingHistoryMapper.insert(browsingHistory1);
        }
    }
}