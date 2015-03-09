package com.cf;

import com.cf.dto.TradeMessage;
import com.cf.model.Model;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Dmitriy on 06.03.2015.
 */
@SpringBootApplication
public class ApplicationConfig {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfig.class, args);
    }

    @Bean
    public Model model() {
        return new Model();
    }

    @Bean(name = "messageQueue")
    public BlockingQueue<TradeMessage> blockingQueue() {
        return new LinkedBlockingQueue<>(100);
    }

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        adapter.setMessageConverters(Collections.nCopies(1,converter));
        return adapter;
    }
}
