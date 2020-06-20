package com.example.test.conroller;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.MQProducer;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.test.entity.TunnelReconTest;
import com.example.test.service.TunnelReconTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author chengsp
 * @date 2019/11/19 15:10
 */
@Controller
public class TestController {
    @Autowired
    private ThreadPoolExecutor executor;
    @Autowired
    private MQProducer mqProducer;
    @Autowired
    private TunnelReconTestService tunnelReconTestService;

    @RequestMapping("/test")
    @ResponseBody
    public Object test(String s) {
        /*executor.execute(() -> {
            try {
                Message msg = new Message("testTopic",s.getBytes());
                mqProducer.send(msg);
            } catch (MQClientException e) {
                e.printStackTrace();
            } catch (RemotingException e) {
                e.printStackTrace();
            } catch (MQBrokerException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });*/
        Page<TunnelReconTest> page = new Page<>(1,10);
        return tunnelReconTestService.page(page).getRecords();
    }
}
