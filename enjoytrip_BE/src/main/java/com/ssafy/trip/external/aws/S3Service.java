package com.ssafy.trip.external.aws;

import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {
	
	 @Value("${cloud.aws.s3.bucket}")
	 private String bucket;
	 
	 private final AmazonS3 amazonS3;
	 
	 public String getPresignedUrl(String prefix, String fileName) {
		 if(!prefix.isEmpty()) {
			 fileName = createPath(prefix, fileName);
		 }
		 
		 GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(bucket, fileName);
		 URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
		 return url.toString();
	 }
	 
	 private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {
	        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fileName).withMethod(HttpMethod.PUT).withExpiration(getPreSignedUrlExpiration());
	        generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());
	        return generatePresignedUrlRequest;
	    }
	 
	 private Date getPreSignedUrlExpiration() {
		 Date expiration = new Date();
	        long expTimeMillis = expiration.getTime();
	        expTimeMillis += 1000 * 60 * 2;
	        expiration.setTime(expTimeMillis);
	        return expiration;
	 }
	 
	 private String createPath(String prefix, String fileName) {
		 return String.format("%s/%s%s", prefix, UUID.randomUUID().toString(), fileName);
	 }
	
}
