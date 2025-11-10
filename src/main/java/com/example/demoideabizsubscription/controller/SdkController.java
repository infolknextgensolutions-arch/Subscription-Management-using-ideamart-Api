package com.example.demoideabizsubscription.controller;

import com.example.demoideabizsubscription.service.IdeamartSdkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/sdk")
@CrossOrigin
public class SdkController {

    private final IdeamartSdkService ideamartSdkService;

    public SdkController(IdeamartSdkService ideamartSdkService) {
        this.ideamartSdkService = ideamartSdkService;
    }

    @GetMapping("/authorize")
    public ResponseEntity<String> getAuthorizeUrl() {
        System.out.println("Authorization request received");
        URI url = ideamartSdkService.buildSubscriptionAuthorizeUrl();
        return ResponseEntity.ok(url.toString());
    }
}