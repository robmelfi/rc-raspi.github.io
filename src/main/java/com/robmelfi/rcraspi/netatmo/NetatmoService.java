package com.robmelfi.rcraspi.netatmo;

import com.robmelfi.rcraspi.netatmo.api.NetatmoHttpClient;
import com.robmelfi.rcraspi.netatmo.api.model.Home;

import com.robmelfi.rcraspi.netatmo.api.model.Module;
import com.robmelfi.rcraspi.service.ControllerService;
import com.robmelfi.rcraspi.service.RemoteControllerService;
import com.robmelfi.rcraspi.service.dto.ControllerDTO;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NetatmoService {

    private final Logger log = LoggerFactory.getLogger(NetatmoService.class);

    private final RemoteControllerService remoteControllerService;
    private final ControllerService controllerService;

    public final String CLIENT_ID = "*";
    public final String CLIENT_SECRET = "*";
    public final String E_MAIL = "*";
    public final String PASSWORD = "*";

    public NetatmoService(RemoteControllerService remoteControllerService, ControllerService controllerService) {
        this.remoteControllerService = remoteControllerService;
        this.controllerService = controllerService;
    }

    @Scheduled(fixedDelay = 1000 * 60)
    public void getNetatmoStatus() throws OAuthSystemException, OAuthProblemException {

        List<ControllerDTO> conrtrollerList = controllerService.findAll();

        for (ControllerDTO controllerDTO : conrtrollerList) {
            // TODO check if controller is bind to netatmo
            boolean boilerStatus = getBoilerStatus() == 1;
            if (boilerStatus) {
                if (controllerDTO.isState())
                    this.remoteControllerService.setLow(controllerDTO.getPinName());
            } else {
                if (!controllerDTO.isState())
                    this.remoteControllerService.setHigh(controllerDTO.getPinName());
            }
        }
    }

    private int getBoilerStatus() throws OAuthProblemException, OAuthSystemException {
        NetatmoHttpClient client = new NetatmoHttpClient(CLIENT_ID, CLIENT_SECRET);
        OAuthJSONAccessTokenResponse token = client.login(E_MAIL, PASSWORD);
        List<Home> homeList = client.getHomesdata(token);
        Home home = homeList.get(0);
        home = client.getHomestatus(token, home);

        for (Module m : home.getModules()) {
            if (m.getType().equals(Module.TYPE_NA_THERM_1)) {
                return m.isBoilerStatus() ? 1 : 0;
            }
        }
        return 0;
    }
}
