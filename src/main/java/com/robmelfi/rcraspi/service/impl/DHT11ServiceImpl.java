package com.robmelfi.rcraspi.service.impl;

import com.robmelfi.rcraspi.sensor.DHT11;
import com.robmelfi.rcraspi.sensor.dto.DHT11DataDTO;
import com.robmelfi.rcraspi.service.DHT11Service;
import io.github.jhipster.config.JHipsterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Profile(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
public class DHT11ServiceImpl implements DHT11Service {

    private final Logger log = LoggerFactory.getLogger(DHT11ServiceImpl.class);

    private final DHT11 dht11;

    public DHT11ServiceImpl(DHT11 dht11) {
        this.dht11 = dht11;
    }

    @Override
    public DHT11DataDTO readTempHum(int pin) {
        DHT11DataDTO result = dht11.getTempHum(pin);
        // TODO check if result is null
        return result;
    }
}
