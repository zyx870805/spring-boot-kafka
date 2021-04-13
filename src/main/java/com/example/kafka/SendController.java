package com.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class SendController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    private volatile boolean flag = false;

    private  ExecutorService executorService = Executors.newFixedThreadPool(1);

     {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (flag) {
                        String msg = UUID.randomUUID().toString();
                        kafkaProducerService.sendMessage("test",msg);
                        log.info("send msg : " + msg);
                    }
                }
            }
        });
    }

    @RequestMapping("/send")
    public Object send() {
        if (!flag)
            flag = true;
        else flag = false;
        return flag ? "开始发送" : "停止发送";
    }
}
