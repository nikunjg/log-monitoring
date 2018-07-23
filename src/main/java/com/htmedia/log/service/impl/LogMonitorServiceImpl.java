package com.htmedia.log.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.htmedia.log.monitor.service.ILogMonitorerService;
import com.htmedia.log.monitor.websocket.MessageSender;

/**
 * The Class LogMonitorereServiceImpl.
 */
@Service
public class LogMonitorServiceImpl implements ILogMonitorerService {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LogMonitorServiceImpl.class);

    /** The last position. */
    private long lastPosition;

    /** The log file. */
    private File logFile;

    /** The log file path. */
    @Value("${logfile.path:}")
    private String logFilePath;

    /** The stop thread. */
    private volatile boolean stopThread = false;

    /** The stop thread. */
    private boolean isTailInProgress = false;

    /** The message sender. */
    @Autowired
    private MessageSender<String> messageSender;

    private ExecutorService executor;

    /**
     * Inits.
     */
    @PostConstruct
    public void init() {
        this.logFilePath = StringUtils.isEmpty(this.logFilePath) ? this.getClass().getProtectionDomain().getCodeSource()
            .getLocation().getPath() + "/test.log" : this.logFilePath;
        // Path of log file can also be loaded from command line argument
        this.logFile = new File(this.logFilePath);
        this.executor = Executors.newFixedThreadPool(2);
    }

    @Override
    public void tailLogFile() {
        synchronized (this) {
            if (!this.isTailInProgress) {
                this.isTailInProgress = true;
                this.executor.execute(() -> this.tail());
                this.executor.execute(() -> this.appendDataToLogFile());
                this.executor.shutdown();
            }
        }
    }

    /**
     * Tail.
     */
    private void tail() {
        // using try with resources
        while (!this.stopThread) {
            try (RandomAccessFile readWriteFileAccess = new RandomAccessFile(this.logFile, "rw")) {
                Thread.sleep(1000);
                long fileLength = this.logFile.length();

                if (fileLength < this.lastPosition) {
                    this.lastPosition = 0;
                }
                if (fileLength > this.lastPosition) {
                    readWriteFileAccess.seek(this.lastPosition);
                    this.readFileAndSendData(readWriteFileAccess);
                    this.lastPosition = readWriteFileAccess.getFilePointer();
                }
            } catch (InterruptedException | IOException e) {
                LOGGER.error("Some Exception occurred while Reading Log file", e);
            }
        }
    }

    /**
     * Read file and send data.
     *
     * @param readWriteFileAccess the read write file access
     * @param isEnter the is enter
     * @return true, if successful
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void readFileAndSendData(final RandomAccessFile readWriteFileAccess)
        throws IOException {
        String nextLine = null;
        while ((nextLine = readWriteFileAccess.readLine()) != null) {
            this.messageSender.sendMessage(nextLine + "\n", "/log-monitoring-broker/log");
        }
    }


    /**
     * Append data to log file.
     *
     * @param args the args
     */
    private void appendDataToLogFile() {
        while (!this.stopThread) {
            try (BufferedWriter bufferWritter = new BufferedWriter(new FileWriter(new File(this.logFilePath)
                .getAbsolutePath(), true))) {

                Thread.sleep(1000);
                String data = "\nRandom content : - " + Math.random();
                bufferWritter.write(data);

            } catch (InterruptedException | IOException e) {
                LOGGER.error("Some Exception occurred while Writing into Log file", e);
            }
        }

    }

}
