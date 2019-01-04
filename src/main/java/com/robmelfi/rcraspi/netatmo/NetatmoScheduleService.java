package com.robmelfi.rcraspi.netatmo;

import com.robmelfi.rcraspi.netatmo.api.NetatmoHttpClient;

import com.robmelfi.rcraspi.netatmo.api.model.Home;
import com.robmelfi.rcraspi.netatmo.api.model.Module;
import com.robmelfi.rcraspi.service.ControllerService;
import com.robmelfi.rcraspi.service.RemoteControllerService;
import com.robmelfi.rcraspi.service.dto.ControllerDTO;
import com.robmelfi.rcraspi.service.dto.NetatmoDTO;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
public class NetatmoScheduleService {

    private final Logger log = LoggerFactory.getLogger(NetatmoScheduleService.class);

    private final RemoteControllerService remoteControllerService;
    private final ControllerService controllerService;
    private final NetatmoHttpClient netatmoHttpClient;
    private final TaskScheduler taskScheduler;
    private ScheduledFuture scheduledFuture;
    private OAuthJSONAccessTokenResponse token;
    private Home home;
    private Long expireAt;
    private NetatmoDTO netatmoDTO;

    public NetatmoScheduleService(RemoteControllerService remoteControllerService, ControllerService controllerService, NetatmoHttpClient netatmoHttpClient, TaskScheduler taskScheduler) {
        this.remoteControllerService = remoteControllerService;
        this.controllerService = controllerService;
        this.netatmoHttpClient = netatmoHttpClient;
        this.taskScheduler = taskScheduler;
    }

    public void initNetatmoHttpClient(NetatmoDTO netatmoDTO) {
        if (netatmoDTO.isEnabled()) {
            this.netatmoDTO = netatmoDTO;
            this.netatmoHttpClient.setClientId(this.netatmoDTO.getClientId());
            this.netatmoHttpClient.setClientSecret(this.netatmoDTO.getClientSecret());
            try {
                this.token = this.netatmoHttpClient.login(this.netatmoDTO.getEmail(), this.netatmoDTO.getPassword());
                this.setExpireAt(this.token);
                List<Home> homeList = netatmoHttpClient.getHomesdata(token);
                this.home = homeList.get(0);
            } catch (OAuthSystemException e) {
                e.printStackTrace();
            } catch (OAuthProblemException e) {
                e.printStackTrace();
            }
        } else {
            this.resetNetatmoHttpClient();
        }
    }

    public void resetNetatmoHttpClient() {
        this.netatmoDTO = null;
        this.token = null;
        this.home = null;
        this.stopReadNetatmoStatus();
    }

    private void setExpireAt(OAuthJSONAccessTokenResponse token) {
        this.expireAt = new Date().getTime() + token.getExpiresIn() * 1000;
    }

    public void startReadNetatmoStatus() {
        final long FIXED_DELAY = 1000 * 60 * 5; // every five minutes
        if (netatmoDTO != null && netatmoDTO.isEnabled()) {
            this.scheduledFuture = this.taskScheduler.scheduleWithFixedDelay(task(), FIXED_DELAY);
        }
    }

    private void stopReadNetatmoStatus() {
        if (this.scheduledFuture != null)
            this.scheduledFuture.cancel(false);
    }

    private Runnable task() {
        return () -> getNetatmoStatus();
    }

    private void getNetatmoStatus() {
        if (netatmoDTO != null) {
            if (netatmoDTO.isEnabled()){
                List<ControllerDTO> conrtrollerList = controllerService.findAll();

                for (ControllerDTO controllerDTO : conrtrollerList) {
                    if (controllerDTO.isNetatmo()) {
                        boolean boilerStatus = false;
                        try {
                            boilerStatus = getBoilerStatus() == 1;
                        } catch (OAuthProblemException e) {
                            e.printStackTrace();
                        } catch (OAuthSystemException e) {
                            e.printStackTrace();
                        }
                        if (boilerStatus) {
                            if (controllerDTO.isState())
                                this.remoteControllerService.setLow(controllerDTO.getPinName());
                        } else {
                            if (!controllerDTO.isState())
                                this.remoteControllerService.setHigh(controllerDTO.getPinName());
                        }
                    }
                }
            }
        }
    }

    private int getBoilerStatus() throws OAuthProblemException, OAuthSystemException {
        if (System.currentTimeMillis() > this.expireAt) {
            this.token = netatmoHttpClient.refreshToken(this.token, this.netatmoDTO.getEmail(), this.netatmoDTO.getPassword());
            this.setExpireAt(this.token);
        }
        this.home = netatmoHttpClient.getHomestatus(this.token, home);
        for (Module m : this.home.getModules()) {
            if (m.getType().equals(Module.TYPE_NA_THERM_1)) {
                return m.isBoilerStatus() ? 1 : 0;
            }
        }
        return 0;
    }
}
