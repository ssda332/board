package yj.board.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yj.board.domain.article.dto.ArticleWriteDto;
import yj.board.domain.file.FileDto;
import yj.board.domain.file.FileWriteDto;
import yj.board.repository.FileRepository;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Uploader {

    @Qualifier("myBatisFileRepository")
    private final FileRepository fileRepository;
    private final AmazonS3Client client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-kk:mm:ss");
        String strDate = dateFormat.format(Calendar.getInstance().getTime());
        String originFileNm = file.getOriginalFilename();
        String tempPath = "temp/";

        //STRE_FILE_NM
        String streFileNm = tempPath + strDate + "-" + originFileNm;
        String fileUrl = client.getUrl(bucket, streFileNm).toString();

        ObjectMetadata metadata= new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            client.putObject(bucket, streFileNm, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileUrl;
    }

    public void copyFile(ArticleWriteDto articleDto) {
        String[] tempFiles = articleDto.getTempFiles();

        Arrays.stream(tempFiles)
                .forEach(file -> {
                    String orgKey = extractS3Key(file);
                    String prefixToRemove = "temp/";
                    // "temp/" 부분을 없애기
                    String copyKey = orgKey.replace(prefixToRemove, "");

                    // 파일 정보 DB에 저장
                    FileWriteDto fileWriteDto = makeFileWriteDto(copyKey, articleDto.getAtcNum());
                    fileRepository.insertFile(fileWriteDto);

                    // S3 버킷에서 이미지 카피
                    CopyObjectRequest copyObjectRequest = new CopyObjectRequest(bucket, orgKey, bucket, copyKey);
                    client.copyObject(copyObjectRequest);

                });
    }

    public ArrayList<FileDto> findFile(String atcNum) {
        return fileRepository.findFile(atcNum);
    }

    public FileWriteDto makeFileWriteDto(String imgUrl, String atcNum) {
        FileWriteDto build = FileWriteDto.builder()
                .imgUrl(imgUrl)
                .atcNum(atcNum)
                .build();

        return build;
    }

    private static String extractS3Key(String s3ObjectUrl) {
        // URL에서 S3 버킷 이름 이후의 부분을 추출
        String bucketName = "boardimagebucket.s3.ap-northeast-2.amazonaws.com/";
        int index = s3ObjectUrl.indexOf(bucketName);

        if (index != -1) {
            // S3 버킷 이후의 부분을 추출하여 디코딩
            String keyEncoded = s3ObjectUrl.substring(index + bucketName.length());
            return urlDecode(keyEncoded);
        }

        // 매칭되는 부분이 없을 경우
        throw new IllegalArgumentException("Invalid S3 object URL");
    }

    private static String urlDecode(String encoded) {
        try {
            return java.net.URLDecoder.decode(encoded, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URL decoding failed", e);
        }
    }

    public void deleteFile(ArrayList<FileDto> files, String atcNum) {
        // 삭제 요청이 들어온 게시글에 대해 전체 삭제
        for (int i = 0; i < files.size(); i++) {
            FileDto target = files.get(i);
            client.deleteObject(bucket, target.getImgUrl());
        }

        fileRepository.deleteFile(atcNum);
    }
}
