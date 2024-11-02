package com.nine.dingtalk;

import com.dingtalk.open.app.api.GenericEventListener;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.message.GenericOpenDingTalkEvent;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.dingtalk.open.app.stream.protocol.event.EventAckStatus;
import shade.com.alibaba.fastjson2.JSONObject;

public class DingtalkServerTest {

    static String clientId = "";
    static String clientSecret = "";

    /**
     * clientId 企业内部应用 AppKey/第三方企业应用 SuiteKey。
     * clientSecret 企业内部应用 AppSecret/第三方企业应用 SuiteSecret。
     */
    public static void main(String[] args) throws Exception {
        OpenDingTalkStreamClientBuilder
                .custom()
                .credential(new AuthClientCredential("${clientId}", "${clientSecret}"))
                //注册事件监听
                .registerAllEventListener(new GenericEventListener() {
                    public EventAckStatus onEvent(GenericOpenDingTalkEvent event) {
                        try {
                            //事件的唯一Id
                            String eventId = event.getEventId();
                            //事件类型
                            String eventType = event.getEventType();
                            //事件产生时间
                            Long bornTime = event.getEventBornTime();
                            //获取事件体
                            JSONObject bizData = event.getData();
                            //处理事件
                            process(bizData);
                            //消费成功
                            return EventAckStatus.SUCCESS;
                        } catch (Exception e) {
                            //消费失败
                            return EventAckStatus.LATER;
                        }
                    }
                })
                .build().start();
    }


    private static void process(JSONObject bizData) {
        System.out.println(bizData);
    }

}
