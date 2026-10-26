package net.dushyant.journalApp.service;

import net.dushyant.journalApp.api.response.WeatherResponse;
import net.dushyant.journalApp.cache.AppCache;
import net.dushyant.journalApp.constants.Placeholders;
import net.dushyant.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

//*-The process of converting json to pojo is called deserilazing

@Component
public class WeatherService {
    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RestTemplate restTemplate;//Process http request and gives us a response

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city){
        String weatherApiTemplate = appCache.appCache.get(AppCache.keys.WEATHER_API.toString());
        if (apiKey == null || apiKey.trim().isEmpty() || weatherApiTemplate == null || weatherApiTemplate.trim().isEmpty()) {
            return null;
        }

        WeatherResponse weatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if(weatherResponse!=null){
            return weatherResponse;
        }
        else{
            String finalAPI = weatherApiTemplate.replace(Placeholders.CITY,city).replace(Placeholders.API_KEY,apiKey);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);//*
            WeatherResponse body = response.getBody();
            if(body!=null){
                redisService.set("weather_of_"+ city,body,300);
            }
            return body;
        }

    }

    //How to integrate post api calls from another source code
//    public WeatherResponse postWeather(String city){
//        String finalAPI = API.replace("CITY",city).replace("API_KEY",apiKey);
//        String requestBody = "{\n"+
//                "   \"userName\":\"ram\",\n"+
//                "   \"password\":\"ram\", \n" +
//                "}   ";
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("key", "value");
//        User user = User.builder().userName("Dushyant").password("Dushyant").build();
//        HttpEntity<User> httpEntity =  new HttpEntity<>(user,httpHeaders);
//        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.POST, httpEntity, WeatherResponse.class);//*
//        WeatherResponse body = response.getBody();
//        return body;
//    }

}

