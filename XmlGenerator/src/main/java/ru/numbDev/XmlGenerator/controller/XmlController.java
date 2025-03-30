package ru.numbDev.XmlGenerator.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ru.numbDev.XmlGenerator.service.DelegateGeneratorService;


@RestController
@RequestMapping("/xml")
@RequiredArgsConstructor
public class XmlController {

    private final DelegateGeneratorService delegateGeneratorService;

    @PostMapping("/add")
    public ResponseEntity<String> addXmlResource(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(delegateGeneratorService.generateForProcess(file.getOriginalFilename(), file.getBytes()));
    }

    @GetMapping("/test/{message}")
    public String checkGigachat(@PathVariable("message") String param) {
        return delegateGeneratorService.testRequestMessage(param);
    }
    
} 