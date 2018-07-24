package net.shadow.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/hello")
public class HelloController {
    @Value("${from}")
    private String from;

    @Autowired
    private Registration registration;

    @GetMapping("/registration")
    public String index(){
        log.info(MessageFormat.format("/hello, host: {0}, service_id: {1}",
                registration.getHost(), registration.getServiceId()));
        return "hello cloud";
    }

    @GetMapping("/from")
    public String from(){
        return this.from;
    }
}
