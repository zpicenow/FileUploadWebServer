package com.zp.client.upload;

public interface UploadService {
    void service(String absolutePath, String filePath) throws Exception;
}
