package toughcircle.shop.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import toughcircle.shop.model.dto.response.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Tag(name = "Image controller", description = "이미지 저장을 위한 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/uploads")
public class ImageController {

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("image")
    public ResponseEntity<Response> uploadImage(@RequestParam("file")MultipartFile file) {
        if (file.isEmpty()) {
            Response response = new Response("No file selected for upload.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadPath, fileName);

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                boolean mkdir = uploadDir.mkdirs();
            }

            Files.write(filePath, file.getBytes());

            String fileUrl = "/uploads/" + fileName;
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            Response response = new Response("Failed to upload file.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
