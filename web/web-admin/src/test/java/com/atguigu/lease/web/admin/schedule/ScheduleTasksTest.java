package com.atguigu.lease.web.admin.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ScheduleTasksTest {

    @Autowired
    private ScheduleTasks scheduleTasks;

    @Test
    public void test(){
        scheduleTasks.checkLeaseStatus();
    }

}