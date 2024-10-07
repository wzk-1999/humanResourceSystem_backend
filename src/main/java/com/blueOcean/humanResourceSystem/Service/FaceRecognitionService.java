package com.blueOcean.humanResourceSystem.Service;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.toolkit.ImageInfo;
import com.blueOcean.humanResourceSystem.Utils.S3UploaderAndComparison;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.arcsoft.face.toolkit.ImageFactory.bufferedImage2ImageInfo;
import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;

@Service
public class FaceRecognitionService {
    //从官网获取
    @Value("${face.appId}")
    String appId;
    @Value("${face.sdkKey}")
    String sdkKey;
    @Value("${face.libPath}")
    String libPath;

    private final S3UploaderAndComparison s3UploaderAndComparison;

    public FaceRecognitionService(S3UploaderAndComparison s3UploaderAndComparison) {
        this.s3UploaderAndComparison = s3UploaderAndComparison;
    }


    public boolean compareSimilarity(String base64Photo,float similarityThreshold) {

        FaceEngine faceEngine = new FaceEngine(libPath);
        //激活引擎
        int errorCode = faceEngine.activeOnline(appId, sdkKey);

        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            System.out.println("引擎激活失败");
            return false;
        }


        ActiveFileInfo activeFileInfo = new ActiveFileInfo();
        errorCode = faceEngine.getActiveFileInfo(activeFileInfo);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            System.out.println("获取激活文件信息失败");
            return false;
        }

        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        engineConfiguration.setDetectFaceMaxNum(10);
        engineConfiguration.setDetectFaceScaleVal(16);
        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);


        //初始化引擎
        errorCode = faceEngine.init(engineConfiguration);

        if (errorCode != ErrorInfo.MOK.getValue()) {
            System.out.println("初始化引擎失败");
            return false;
        }
        // Remove the base64 prefix (data:image/png;base64,)
        if (base64Photo.startsWith("data:image")) {
            base64Photo = base64Photo.substring(base64Photo.indexOf(",") + 1);
        }
        // Decode base64 to BufferedImage
        byte[] imageBytes = Base64.getDecoder().decode(base64Photo);
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (bufferedImage == null) {
            System.out.println("Image decoding failed");
            return false;
        }

        // Convert BufferedImage to ImageInfo
        ImageInfo imageInfo = bufferedImage2ImageInfo(bufferedImage);

        // Face detection
        List<FaceInfo> faceInfoList = new ArrayList<>();
        errorCode = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);

        if (errorCode != ErrorInfo.MOK.getValue() || faceInfoList.isEmpty()) {
            System.out.println("Face detection failed");
            return false;
        }

        // Extract face feature
        FaceFeature faceFeature = new FaceFeature();
        errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);

        if (errorCode != ErrorInfo.MOK.getValue()) {
            System.out.println("Face feature extraction failed");
            return false;
        }

        //人脸检测
//        ImageInfo imageInfo2 = getRGBData(new File("C:\\Users\\10648\\Desktop\\co-op permit apply\\digital photo.jpg"));
        bufferedImage = s3UploaderAndComparison.fetchImageFromS3("elasticbeanstalk-ca-central-1-471112778577", "user-profile/image.jpg");
        // Convert BufferedImage to ImageInfo
        ImageInfo imageInfo2 = bufferedImage2ImageInfo(bufferedImage);
        List<FaceInfo> faceInfoList2 = new ArrayList<FaceInfo>();
        errorCode = faceEngine.detectFaces(imageInfo2.getImageData(), imageInfo2.getWidth(), imageInfo2.getHeight(), imageInfo2.getImageFormat(), faceInfoList2);

        //特征提取
        FaceFeature targetFaceFeature = new FaceFeature();
        errorCode = faceEngine.extractFaceFeature(imageInfo2.getImageData(), imageInfo2.getWidth(), imageInfo2.getHeight(), imageInfo2.getImageFormat(), faceInfoList2.get(0), targetFaceFeature);

        // Here, for simplicity, we'll compare the same face against itself
        FaceSimilar faceSimilar = new FaceSimilar();
        errorCode = faceEngine.compareFaceFeature(targetFaceFeature, faceFeature, faceSimilar);

        if (errorCode == ErrorInfo.MOK.getValue()) {
            System.out.println("Similarity score: " + faceSimilar.getScore());
            // Return true only if the similarity score is greater than the threshold
            return faceSimilar.getScore() >= similarityThreshold;
        } else {
            System.out.println("Face comparison failed");
            return false;
        }
    }
}
