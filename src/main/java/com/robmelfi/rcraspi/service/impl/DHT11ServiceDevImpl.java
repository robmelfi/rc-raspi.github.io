package com.robmelfi.rcraspi.service.impl;

import com.robmelfi.rcraspi.sensor.dto.DHT11DataDTO;
import com.robmelfi.rcraspi.service.DHT11Service;
import io.github.jhipster.config.JHipsterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
@Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
public class DHT11ServiceDevImpl implements DHT11Service {

    final float c_min = -20;
    final float c_max = 50;

    final float h_min = 0;
    final float h_max = 50;

    private final Logger log = LoggerFactory.getLogger(DHT11ServiceDevImpl.class);

    @Override
    public DHT11DataDTO readTempHum(int pin) {

        Random r = new Random();
        DHT11DataDTO dht11DataDTO = new DHT11DataDTO();
        dht11DataDTO.setTemperature(getMockTemp(r));
        dht11DataDTO.setHumidity(getMockHum(r));
        return dht11DataDTO;
    }

    private float getMockTemp(Random r) {
        return c_min + r.nextFloat() * (c_max - c_min);
    }

    private float getMockHum(Random r) {
        return h_min + r.nextFloat() * (h_max - h_min);
    }
}
