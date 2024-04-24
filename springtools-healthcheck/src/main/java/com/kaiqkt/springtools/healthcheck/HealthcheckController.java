package com.kaiqkt.springtools.healthcheck;

import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController("/healthcheck")
public class HealthcheckController {

    private BuildProperties buildProperties;

    @GetMapping
    public ResponseEntity<Map<String, Object>> check(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("application_name", buildProperties.getName());
        data.put("application_version", buildProperties.getVersion());

        return ResponseEntity.ok(data);
    }
}
