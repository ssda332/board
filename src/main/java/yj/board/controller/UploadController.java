package yj.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yj.board.service.S3Uploader;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    private final S3Uploader s3Uploader;

    @PostMapping("")
    public ResponseEntity<String> uploadFile(@RequestParam("image") MultipartFile file){
        String fileUrl = s3Uploader.uploadFile(file);
        /*s3Uploader.savePath(fileUrl);*/

        return ResponseEntity.ok(fileUrl);
    }

}
