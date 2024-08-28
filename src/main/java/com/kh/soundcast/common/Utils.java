package com.kh.soundcast.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {
	// 파일 저장 함수
	// 파일을 저장시키면서 파일명을 함께 수정한 후, 수정된 파일명을 반환
	// path 매개변수에 경로를 넘길 때 반드시 절대 경로로 넘겨야함
	public static String saveFile(MultipartFile upfile, String path) {
		
		// 랜덤 파일명 생성하기
		String originName = upfile.getOriginalFilename();
		String currentTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
		int random = (int) (Math.random() * 90000 + 10000); // 10000~99999(5자리 랜덤값)
		String ext = originName.substring(originName.lastIndexOf(".")); // test.jpg -> .jpg
		
		String changeName = "soundcast-" + currentTime + random + ext;
		
		// 파일 저장
		try {
			upfile.transferTo(new File(path + changeName));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		
		return changeName;
	}
	
	public static String urlSaveFile(String fileUrl, String path) {
		 String changeName = null;
		
		 try {
	            // URL에서 파일 다운로드
	            URL url = new URL(fileUrl);
	            URLConnection connection = url.openConnection();
	            String mimeType = connection.getContentType(); // MIME 타입 가져오기
	            InputStream in = connection.getInputStream();
	            
	            // MIME 타입에 따른 확장자 설정
	            String ext = getExtensionFromMimeType(mimeType);
	            
	            // 랜덤 파일명 생성하기
	            String currentTime = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
	            int random = (int) (Math.random() * 90000 + 10000); // 10000~99999(5자리 랜덤값)
	            changeName = "soundcast-" + currentTime + random + ext;

	            // 파일 저장
	            log.info("img path={}", path);
	            OutputStream out = new FileOutputStream(new File(path + changeName));
	            IOUtils.copy(in, out);
	            
	            in.close();
	            out.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return changeName;
	    }
	
	 private static String getExtensionFromMimeType(String mimeType) {
	        switch (mimeType) {
	            case "image/jpeg":
	                return ".jpg";
	            case "image/png":
	                return ".png";
	            case "image/gif":
	                return ".gif";
	            case "image/bmp":
	                return ".bmp";
	            case "image/webp":
	                return ".webp";
	            default:
	                return ""; // 확장자를 알 수 없는 경우 빈 문자열 반환
	        }
	    }
	
	
	
	
	// XSS 크로스 사이트 스크립트 공격 방지 메소드
	public static String XSSHandling(String content) {
		if(content != null) {
			content = content.replaceAll("&", "&amp;");
			content = content.replaceAll("<", "&lt;");
			content = content.replaceAll(">", "&gt;");
			content = content.replaceAll("\"", "&quot;");
		}
		return content;
	}
	
	// 개행처리
	// textArea -> \n, p -> <br>
	public static String newLineHandling(String content) {
		return content.replaceAll("(\r\n|\n|\r|\n\r)", "<br>");
	}
	
	// 개행처리 해제
	public static String newLineClear(String content) {
		return content.replaceAll("(<br>)", "\n");
	}
	
}
