package com.gelael.interapp.service;

import com.gelael.interapp.domain.ExtPath;
import com.gelael.interapp.domain.OldMasterItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by edo on 12/01/2016.
 */
@Service
public class TokenService {
    @Autowired
    ExtPath extPath;

    public String getToken(String subject) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> re = restTemplate.getForEntity(extPath.getMdwUrl() + "/token/get/" + subject + "/" + extPath.getOutletCode(), String.class);
        HttpStatus acc = re.getStatusCode();
        if (acc == HttpStatus.OK) {
            return re.getBody();
        } else {
            return "";
        }
    }
}
