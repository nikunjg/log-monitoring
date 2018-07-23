package com.htmedia.log.monitor.service;

/**
 * The Interface ILogMonitorerService.
 */
public interface ILogMonitorerService {

    /**
     * Tails a log file specified in the config file or create one if not present. It continuously check for changes in
     * the given file. If any change is made, same is pushed using socket.
     */
    void tailLogFile();

}
