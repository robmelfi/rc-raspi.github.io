package com.robmelfi.rcraspi.service.impl;

import com.robmelfi.rcraspi.domain.Humidity;
import com.robmelfi.rcraspi.domain.Temperature;
import com.robmelfi.rcraspi.repository.HumidityRepository;
import com.robmelfi.rcraspi.repository.TemperatureRepository;
import com.robmelfi.rcraspi.sensor.DHT11;
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

@Service
@Transactional
@Profile(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
public class DHT11ServiceImpl implements DHT11Service {

    private final Logger log = LoggerFactory.getLogger(DHT11ServiceImpl.class);

    private final DHT11 dht11;

    private final TemperatureRepository temperatureRepository;

    private final HumidityRepository humidityRepository;

    public DHT11ServiceImpl(DHT11 dht11, TemperatureRepository temperatureRepository, HumidityRepository humidityRepository) {
        this.dht11 = dht11;
        this.temperatureRepository = temperatureRepository;
        this.humidityRepository = humidityRepository;
    }

    @Override
    public DHT11DataDTO readTempHum(int pin) {
        DHT11DataDTO result = dht11.getTempHum(pin);
        // TODO check if result is null
        return result;
    }

    @Override
    public void storeTempHum(int pin) {
        DHT11DataDTO result = dht11.getTempHum(pin);
        if(result != null) {
            ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.systemDefault());
            Temperature temperature = new Temperature()
                .value(result.getTemperature())
                .timestamp(timestamp);
            Humidity humidity = new Humidity()
                .value(result.getHumidity())
                .timestamp(timestamp);

            this.temperatureRepository.save(temperature);
            this.humidityRepository.save(humidity);
        }
    }
}
