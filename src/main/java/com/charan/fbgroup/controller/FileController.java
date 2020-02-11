package com.charan.fbgroup.controller;

import com.charan.fbgroup.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class FileController {
    @Autowired
    FileService fileService;

    @PostMapping("/appendfiles")
    public String appendFiles(@RequestBody Map<String, String> input) {
        try {
            return fileService.appendFiles(input.get("folderPath"),
                    input.get("filePrefix"),
                    input.get("combinedFilePrefix"));
        } catch (IOException e) {
            e.printStackTrace();
            return "failed: " + e.getLocalizedMessage();
        }
    }

}
