package com.example.springscaffold2.api.controller;

import com.example.springscaffold2.api.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @GetMapping("/notran")
    public ResponseEntity<?> getNoTran(){
        return ResponseEntity.ok(testService.getNoTran());
    }

    @GetMapping("/tran")
    public ResponseEntity<?> getTran(){
        return ResponseEntity.ok(testService.getTran());
    }

    @GetMapping("/notran/rollback")
    public ResponseEntity<?> getNoTranRollback(){
        return ResponseEntity.ok(testService.getNoTranRb());
    }

    @GetMapping("/tran/rollback")
    public ResponseEntity<?> getTranRollback(){
        return ResponseEntity.ok(testService.getTranRb());
    }
}
