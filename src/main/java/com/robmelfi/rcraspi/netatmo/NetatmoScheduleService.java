package com.robmelfi.rcraspi.netatmo;

import com.robmelfi.rcraspi.netatmo.api.NetatmoHttpClient;
import com.robmelfi.rcraspi.netatmo.api.model.Home;

import com.robmelfi.rcraspi.netatmo.api.model.Module;
import com.robmelfi.rcraspi.service.ControllerService;
import com.robmelfi.rcraspi.service.NetatmoService;
import com.robmelfi.rcraspi.service.RemoteControllerService;
import com.robmelfi.rcraspi.service.dto.ControllerDTO;
import com.robmelfi.rcraspi.service.dto.NetatmoDTO;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NetatmoScheduleService {

    private final Logger log = LoggerFactory.getLogger(NetatmoScheduleService.class);

    private final RemoteControllerService remoteControllerService;
    private final ControllerService controllerService;
    private final NetatmoService netatmoService;

    public NetatmoScheduleService(RemoteControllerService remoteControllerService, ControllerService controllerService, NetatmoService netatmoService) {
        this.remoteControllerService = remoteControllerService;
        this.controllerService = controllerService;
        this.netatmoService = netatmoService;
    }

    @Scheduled(fixedDelay = 1000 * 60)
    public void getNetatmoStatus() throws OAuthSystemException, OAuthProblemException {

        NetatmoDTO netatmoDTO = getNetatmoCredentials();
        if (netatmoDTO != null) {

            List<ControllerDTO> conrtrollerList = controllerService.findAll();

            for (ControllerDTO controllerDTO : conrtrollerList) {
                if (controllerDTO.isNetatmo()) {
                    log.debug(" ------> {}, {}", controllerDTO.getPinName());
                    boolean boilerStatus = getBoilerStatus(netatmoDTO) == 1;
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

    private NetatmoDTO getNetatmoCredentials() {
        NetatmoDTO result = null;

        List<NetatmoDTO> netatmoDTOList = netatmoService.findAll();
        if (netatmoDTOList.size() == 1) {
            result = netatmoDTOList.get(0);
        }
        return result;
    }

    private int getBoilerStatus(NetatmoDTO netatmoDTO) throws OAuthProblemException, OAuthSystemException {
        NetatmoHttpClient client = new NetatmoHttpClient(netatmoDTO.getClientId(), netatmoDTO.getClientSecret());
        OAuthJSONAccessTokenResponse token = client.login(netatmoDTO.getEmail(), netatmoDTO.getPassword());
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
