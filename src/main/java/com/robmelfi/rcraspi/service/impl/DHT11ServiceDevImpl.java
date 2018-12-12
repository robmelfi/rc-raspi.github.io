package com.robmelfi.rcraspi.service.impl;

import com.robmelfi.rcraspi.domain.Humidity;
import com.robmelfi.rcraspi.domain.Temperature;
import com.robmelfi.rcraspi.repository.HumidityRepository;
import com.robmelfi.rcraspi.repository.TemperatureRepository;
import com.robmelfi.rcraspi.sensor.dto.DHT11DataDTO;
import com.robmelfi.rcraspi.service.DHT11Service;
import io.github.jhipster.config.JHipsterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    private final TemperatureRepository temperatureRepository;

    private final HumidityRepository humidityRepository;

    public DHT11ServiceDevImpl(TemperatureRepository temperatureRepository, HumidityRepository humidityRepository) {
        this.temperatureRepository = temperatureRepository;
        this.humidityRepository = humidityRepository;
    }

    @Override
    public DHT11DataDTO readTempHum(int pin) {
        return getMockDHT11DataDTO();
    }

    @Override
    public void storeTempHum(int pin) {
        DHT11DataDTO result = this.getMockDHT11DataDTO();
        ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.systemDefault());
        Temperature temperature = new Temperature()
            .value(result.getTemperature())
            .timestamp(timestamp);
        Humidity humidity = new Humidity()
            .value(result.getHumidity())
            .timestamp(timestamp);
        log.debug("temperature {} - humidity {}", temperature, humidity);
        this.temperatureRepository.save(temperature);
        this.humidityRepository.save(humidity);
    }

    private DHT11DataDTO getMockDHT11DataDTO() {
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
