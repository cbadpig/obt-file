package com.hyt.obt.file.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Configuration
@Data
@ConfigurationProperties(prefix = "monitor")
public class MonitorYml {

    @Value("${monitor.fastDfs.httpUrl}")
    private String fastDfsUlr;
    @Value("${monitor.face.appId}")
    private String faceAppId;
    @Value("${monitor.face.sdkKey}")
    private String faceSdkKey;
    @Value("${monitor.face.activeKey}")
    private String faceActiveKey;
    @Value("${monitor.face.faceQuality}")
    private Float faceFaceQuality;
    @Value("${monitor.face.faceSimilarScore}")
    private Float faceFaceSimilarScore;

    private static String staticFastDfsUlr;
    private static String staticAppId;
    private static String staticFaceSdkKey;
    private static String staticActiveKey;
    private static Float staticFaceQuality;
    private static Float staticFaceSimilarScore;

    @PostConstruct
    public void setStaticFastDfsUlr() {
        MonitorYml.staticFastDfsUlr = this.fastDfsUlr;
    }

    @PostConstruct
    public void setStaticAppId() {
        MonitorYml.staticAppId = this.faceAppId;
    }

    @PostConstruct
    public void setStaticFaceSdkKey() {
        MonitorYml.staticFaceSdkKey = this.faceSdkKey;
    }

    @PostConstruct
    public void setStaticActiveKey() {
        MonitorYml.staticActiveKey = this.faceActiveKey;
    }

    @PostConstruct
    public void setStaticFaceQuality() {
        MonitorYml.staticFaceQuality = this.faceFaceQuality;
    }

    @PostConstruct
    public void setStaticFaceSimilarScore() {
        MonitorYml.staticFaceSimilarScore = this.faceFaceSimilarScore;
    }

    public static String getStaticFastDfsUlr() {
        return staticFastDfsUlr;
    }

    public static String getStaticAppId() {
        return staticAppId;
    }

    public static String getStaticFaceSdkKey() {
        return staticFaceSdkKey;
    }

    public static String getStaticActiveKey() {
        return staticActiveKey;
    }

    public static Float getStaticFaceQuality() {
        return staticFaceQuality;
    }

    public static Float getStaticFaceSimilarScore() {
        return staticFaceSimilarScore;
    }
}
