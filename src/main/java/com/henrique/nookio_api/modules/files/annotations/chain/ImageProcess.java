package com.henrique.nookio_api.modules.files.annotations.chain;

import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.DetectModerationLabelsRequest;
import software.amazon.awssdk.services.rekognition.model.DetectModerationLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Image;

@NoArgsConstructor
public class ImageProcess extends ValidateProcess{

    private static RekognitionClient rekognitionClient = RekognitionClient.create();

    @Override
    protected boolean validate(MultipartFile file) {
        try {
            DetectModerationLabelsRequest request =
                    DetectModerationLabelsRequest.builder()
                            .image(
                                    Image.builder()
                                            .bytes(
                                                    SdkBytes.fromInputStream(
                                                            file.getInputStream()
                                                    )
                                            )
                                            .build()
                            )
                            .minConfidence(80F)
                            .build();


            DetectModerationLabelsResponse response =
                    rekognitionClient.detectModerationLabels(request);


            return response.moderationLabels()
                    .isEmpty();


        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao analisar imagem",
                    e
            );
        }
    }
}
