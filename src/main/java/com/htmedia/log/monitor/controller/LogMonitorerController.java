package com.htmedia.log.monitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.htmedia.log.monitor.service.ILogMonitorerService;

/**
 * The Class LogMonitorerController.
 */
@RestController
public class LogMonitorerController {


    /** The log monitorer service. */
    @Autowired
    private ILogMonitorerService logMonitorerService;


    /**
     * Gets the applications health.
     *
     * @return the applications health
     */
    @RequestMapping(path = "/tail/log", method = RequestMethod.GET)
    public ResponseEntity<String> tailLogFile() {
        this.logMonitorerService.tailLogFile();
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
