package com.kaiqkt.springtools.healthcheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthcheckController {

    private final BuildProperties buildProperties;

    @Autowired
    public HealthcheckController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<Map<String, Object>> check(){
        HashMap<String, Object> data = new HashMap<>();
        data.put("application_name", buildProperties.getName());
        data.put("application_version", buildProperties.getVersion());

        return ResponseEntity.ok(data);
    }
}
