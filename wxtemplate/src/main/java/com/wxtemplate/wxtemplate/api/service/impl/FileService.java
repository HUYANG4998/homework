package com.wxtemplate.wxtemplate.api.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {
    @Async
    public void fileTran(MultipartFile file, File uploadFile){

    }
}
