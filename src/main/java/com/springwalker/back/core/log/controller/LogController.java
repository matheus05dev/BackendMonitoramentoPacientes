package com.springwalker.back.core.log.controller;

import com.springwalker.back.core.log.model.Log;
import com.springwalker.back.core.log.repository.LogRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/logs")
@AllArgsConstructor
public class LogController {


    private final LogRepository logRepository;

    @GetMapping
    public List<Log> listAllLogs() {
        return logRepository.findAll();
    }
}
