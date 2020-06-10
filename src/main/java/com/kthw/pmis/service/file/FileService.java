package com.kthw.pmis.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {


    public void upload(MultipartFile multipartFile);
    public void download(MultipartFile multipartFile);
}
