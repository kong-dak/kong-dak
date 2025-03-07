package com.kongdak.global.S3;

import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 단일 파일 업로드
     */
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }

        String fileName = createFileName(file.getOriginalFilename());
        String contentType = file.getContentType();

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return generateUrl(fileName);
        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    /**
     * 다중 파일 업로드
     */
    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                urls.add(uploadFile(file));
            }
        }

        return urls;
    }

    /**
     * 접두어가 있는 다중 파일 업로드 (트랜잭션 처리용)
     */
    public List<String> uploadFilesWithPrefix(String prefix, List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = prefix + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

                try {
                    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(fileName)
                            .contentType(file.getContentType())
                            .build();

                    s3Client.putObject(putObjectRequest,
                            RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

                    urls.add(generateUrl(fileName));
                } catch (IOException e) {
                    log.error("파일 업로드 중 오류 발생: {}", e.getMessage(), e);
                    throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
                }
            }
        }

        return urls;
    }

    /**
     * 파일 삭제
     */
    public void deleteFile(String fileUrl) {
        try {
            String fileName = extractFileNameFromUrl(fileUrl);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            log.error("파일 삭제 중 오류 발생: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.FILE_DELETE_ERROR);
        }
    }

    /**
     * 접두어로 파일 삭제 (트랜잭션 롤백용)
     */
    public void deleteFilesByPrefix(String prefix) {
        try {
            s3Client.listObjectsV2(builder -> builder
                            .bucket(bucket)
                            .prefix(prefix)
                            .build())
                    .contents()
                    .forEach(object -> deleteFile(generateUrl(object.key())));
        } catch (Exception e) {
            log.error("접두어로 파일 삭제 중 오류 발생: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.FILE_DELETE_ERROR);
        }
    }

    /**
     * URL에서 파일명 추출
     */
    public String extractFileNameFromUrl(String fileUrl) {
        if (fileUrl.contains(bucket + ".s3.")) {
            // URL 형식이 https://bucket-name.s3.region.amazonaws.com/filename
            return fileUrl.substring(fileUrl.indexOf(bucket) + bucket.length() + 5);
        } else {
            // 다른 URL 형식
            return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        }
    }

    /**
     * 고유한 파일명 생성
     */
    private String createFileName(String originalFileName) {
        return "diary/" + UUID.randomUUID() + "_" + originalFileName;
    }

    /**
     * S3 객체 URL 생성
     */
    private String generateUrl(String fileName) {
        GetUrlRequest request = GetUrlRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        return s3Client.utilities().getUrl(request).toString();
    }

    /**
     * 만료되는 프리사인 URL 생성 (필요시)
     */
    public String generatePresignedUrl(String fileName, Duration duration) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(duration)
                .getObjectRequest(b -> b.bucket(bucket).key(fileName).build())
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

//    /**
//     * 썸네일 생성 ( 이미지 처리 라이브러리 사용 필요)
//     */
//    public String createThumbnail(String originalUrl) {
//        // 실제로는 이미지를 다운로드하고, 리사이징한 후 다시 업로드해야 함
//        // 지금은 단순히 썸네일 경로만 반환
//        String fileName = extractFileNameFromUrl(originalUrl);
//        String thumbnailFileName = "thumbnails/" + fileName;
//
//        // 여기서는 원본 파일의 경로를 기반으로 썸네일 URL만 생성
//        return originalUrl.replace(fileName, thumbnailFileName);
//    }
}
