package com.scott.chat.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scott.chat.model.Messagelog;
import com.scott.chat.repository.PostFileRepository;

@RestController
@RequestMapping("/saveData")
public class PostFileController {

    @Autowired
    private PostFileRepository postFileRepository;

    @PostMapping("/savePostData/{postId}")
    public ResponseEntity<String> savePostData(@PathVariable Integer postId,@RequestBody List<Object> dataList) {
        try {
            // 轉換 JSON 為字串
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(dataList);
            // 存入資料庫
            Messagelog postFile = postFileRepository.findBysupermessagelogid(postId).orElse(new Messagelog());
            postFile.setSupermessagelogid(postId);
            postFile.setMessagefile(jsonString.getBytes(StandardCharsets.UTF_8));
            postFileRepository.save(postFile);

            return ResponseEntity.ok("");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("儲存失敗：" + e.getMessage());
        }
    }
}
