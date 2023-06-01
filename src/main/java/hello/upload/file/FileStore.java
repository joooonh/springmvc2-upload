package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    // 파일 저장 경로(fullPath) 생성
    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    // 파일 여러개 저장
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                // 서버에 파일 저장하는 메소드 (밑에 생성)
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    // 서버에 파일 저장 ( 파일 정보 반환 )
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();  // image.png
        String storeFileName = createStoreFileName(originalFilename);   // dfw-dwf-dwf.png
        multipartFile.transferTo(new File(getFullPath(storeFileName))); // 서버에 저장된 파일명으로 실제 파일 생성
        return new UploadFile(originalFilename, storeFileName);
    }

    // 서버에 저장되는 파일명 생성 메소드
    private String createStoreFileName(String originalFilename) {
        // 확장자 추출
        String ext = extractExt(originalFilename);
        // UUID로 서버에 저장되는 파일명 생성 + 확장자
        String uuid = UUID.randomUUID().toString();
        // 서버에 저장되는 파일명 반환 (dfw-dwf-dwf.png)
        return uuid + "." + ext;
    }

    // 확장자 추출 메소드
    private String extractExt(String originalFilename) {
        // "."의 마지막 인덱스 위치
        int pos = originalFilename.lastIndexOf(".");
        // 그 "."의 다음 글자부터 출력 (=확장자)
        return originalFilename.substring(pos + 1);

    }
}
