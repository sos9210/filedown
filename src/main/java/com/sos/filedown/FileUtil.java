package com.sos.filedown;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Controller
public class FileUtil {
    private static final String FILE_DIR = "C:\\Users\\Sung\\Pictures\\testImage\\";
    private long io_count = 1;
    private long nio_count = 1;

    @GetMapping("/io/filedown/{fileName}")
    public ResponseEntity<String> ioFileDownload(@PathVariable String fileName,HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        String realPath = FILE_DIR+fileName;
        File file1 = new File(realPath);
        if (!file1.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("실패");
        }

        // 파일명 지정
        response.setHeader("Content-Disposition", "attachment; filename=\"download.jpg\"");
        try (
                OutputStream os = response.getOutputStream();
                FileInputStream fis = new FileInputStream(realPath);
        ){

            int ncount = 0;
            byte[] bytes = new byte[8192];

            while ((ncount = fis.read(bytes)) != -1 ) {
                os.write(bytes, 0, ncount);
            }

//            Files.deleteIfExists(Paths.get("C:\\Users\\Sung\\Downloads\\download.jpg"));
        } catch (FileNotFoundException ex) {
            log.error("FileNotFoundException paht : {}",realPath,ex);
        } catch (IOException ex) {
            log.error("IOException paht : {}",realPath);
        }
        long endTime = System.currentTimeMillis();
        log.info("io-{} 소요시간 : {}",io_count,endTime - startTime); // 실행 시간 계산
        io_count++;
        return ResponseEntity.ok("성공");
    }

    @GetMapping("/nio/filedown/{fileName}")
    public ResponseEntity<String> nioFileDownload(@PathVariable String fileName,HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();

        // 다운로드할 파일 경로 생성
        Path filePath = Paths.get(FILE_DIR+fileName);
        Resource resource = new FileSystemResource(filePath.toString());

        if (resource.exists()) {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            try (WritableByteChannel out = Channels.newChannel(response.getOutputStream());
                 ReadableByteChannel in = Channels.newChannel(resource.getInputStream())) {
                ByteBuffer buffer = ByteBuffer.allocateDirect(8192);
                while (in.read(buffer) != -1) {
                    buffer.flip();
                    out.write(buffer);
                    buffer.clear();
                }
            } catch (Exception e) {
                log.error("fileDownload Ex : ",e);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        long endTime = System.currentTimeMillis();
        log.info("nio-{} 소요시간 : {}",nio_count,endTime - startTime); // 실행 시간 계산
        nio_count++;

        return ResponseEntity.ok("성공");
    }
}
