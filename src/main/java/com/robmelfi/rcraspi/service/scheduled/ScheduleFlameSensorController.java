package com.robmelfi.rcraspi.service.scheduled;

import com.pi4j.io.gpio.Pin;
import com.robmelfi.rcraspi.domain.User;
import com.robmelfi.rcraspi.repository.UserRepository;
import com.robmelfi.rcraspi.service.FlameSensorService;
import com.robmelfi.rcraspi.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
public class ScheduleFlameSensorController {

    private final Logger log = LoggerFactory.getLogger(ScheduleFlameSensorController.class);

    private final FlameSensorService flameSensorService;

    private final TaskScheduler taskScheduler;

    private ScheduledFuture scheduledFuture;

    private final MailService mailService;

    private final UserRepository userRepository;

    public ScheduleFlameSensorController(FlameSensorService flameSensorService, TaskScheduler taskScheduler, MailService mailService, UserRepository userRepository) {
        this.flameSensorService = flameSensorService;
        this.taskScheduler = taskScheduler;
        this.mailService = mailService;
        this.userRepository = userRepository;
    }

    public void setPin(Pin pin) {
        this.flameSensorService.setPin(pin);
    }

    public void start(long rate) {
        scheduledFuture = taskScheduler.scheduleAtFixedRate(flameDetected(), rate);
    }

    public void stop() {
        scheduledFuture.cancel(false);
    }

    private Runnable flameDetected() {
        return this::task;
    }

    private void task() {
        boolean flame = this.flameSensorService.flameDetected();
        log.info("Flame Detected: {}", flame);
        if (flame) {
            List<User> userList = this.userRepository.findAll();
            for (User user : userList)
                if (user.getActivated() && !user.getLogin().equals("anonymoususer")) this.mailService.sendAlertMail(user);
        }
    }
}
