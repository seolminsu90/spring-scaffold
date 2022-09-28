package com.admin.tool.api.error;

import com.admin.tool.common.model.ApiResponse;
import com.admin.tool.common.model.Code;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {
    @GetMapping("/error")
    public ResponseEntity<ApiResponse<String>> xaTransactionTest() {
        return ResponseEntity.ok(ApiResponse.of(Code.FAIL.code, "404 Not Found"));
    }
}
