package com.hyt.obt.file.util;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectModel;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.hyt.obt.file.entity.MonitorYml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FaceARCUtil {

    private static FaceEngine faceEngine = null;
    private static String appId = MonitorYml.getStaticAppId();
    private static String sdkKey = MonitorYml.getStaticFaceSdkKey();
    private static String activeKey = MonitorYml.getStaticActiveKey();
    private static float faceQuality = MonitorYml.getStaticFaceQuality();
    private static float faceSimilarScore = MonitorYml.getStaticFaceSimilarScore();

    static {
        //人脸识别引擎库存放路径
        faceEngine = new FaceEngine("");
        //激活引擎
        int errorCode = faceEngine.activeOnline(appId, sdkKey, activeKey);
        System.out.println("=====人脸识别=====引擎激活errorCode:" + errorCode);

        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        engineConfiguration.setDetectFaceMaxNum(30);
        engineConfiguration.setDetectFaceScaleVal(16);
        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportImageQuality(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);

        //初始化引擎
        errorCode = faceEngine.init(engineConfiguration);
        System.out.println("=====人脸识别=====引擎初始化结果，errorCode:" + errorCode);

    }

    public static String faceARC(FaceFeature firstFaceFeature, InputStream targetImg) {

        try {

            if (firstFaceFeature == null || targetImg.available() <= 0) {
                return "100";
            }

            ImageInfo imageInfo = ImageFactory.getRGBData(targetImg);
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
            int errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
            if (errorCode !=0 ) {
                return "100";
            }

            //无人
            if (faceInfoList == null || faceInfoList.size() == 0) {
                return "103";
            }

            //多人
            if (faceInfoList.size() > 1) {
                return "102";
            }

            List<ImageQuality> imageQualityList = new ArrayList<>();
            errorCode = faceEngine.imageQualityDetect(imageInfo, DetectModel.ASF_DETECT_MODEL_RGB, faceInfoList, imageQualityList);
            if (errorCode !=0 ) {
                return "100";
            }
            //人脸模糊
            if (imageQualityList.get(0).getFaceQuality() < faceQuality) {
                return "104";
            }

            //图片比对
            FaceFeature faceFeature = new FaceFeature();
            errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature,2);
            if (errorCode !=0 ) {
                return "100";
            }

            FaceFeature targetFaceFeature = new FaceFeature();
            targetFaceFeature.setFeatureData(faceFeature.getFeatureData());
            FaceFeature sourceFaceFeature = new FaceFeature();
            sourceFaceFeature.setFeatureData(firstFaceFeature.getFeatureData());

            FaceSimilar faceSimilar = new FaceSimilar();
            errorCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);

            if (errorCode !=0 ) {
                return "100";
            }
            if (faceSimilar.getScore() < faceSimilarScore) {
                return "101";
            }
        } catch (Exception e) {
            System.out.println("人脸识别出错");
            e.printStackTrace();
            return "100";
        }

        return "100";
    }

    public static FaceFeature getFaceFeature(InputStream firstImg) {

        ImageInfo firstImageInfo = ImageFactory.getRGBData(firstImg);
        List<FaceInfo> firstFaceInfoList = new ArrayList<FaceInfo>();
        int errorCode = faceEngine.detectFaces(firstImageInfo.getImageData(), firstImageInfo.getWidth(), firstImageInfo.getHeight(), firstImageInfo.getImageFormat(), firstFaceInfoList);
        if (errorCode !=0 ) {
            return null;
        }

        //特征提取
        FaceFeature firstFaceFeature = new FaceFeature();
        errorCode = faceEngine.extractFaceFeature(firstImageInfo.getImageData(), firstImageInfo.getWidth(), firstImageInfo.getHeight(), firstImageInfo.getImageFormat(), firstFaceInfoList.get(0), firstFaceFeature);
        if (errorCode !=0 ) {
            return null;
        }

        return firstFaceFeature;
    }

}
