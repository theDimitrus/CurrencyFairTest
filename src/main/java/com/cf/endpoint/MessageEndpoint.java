package com.cf.endpoint;

import com.cf.dto.TradeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dmitriy on 06.03.2015.
 */
@RestController
@RequestMapping("/message")
public class MessageEndpoint {

    @Resource(name = "messageQueue")
    public BlockingQueue<TradeMessage> messageQueue;

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> handleMessage(@RequestBody TradeMessage message) {
        try {
            if (! messageQueue.offer(message, 5, TimeUnit.SECONDS)) {
                return new ResponseEntity<Object>(HttpStatus.TOO_MANY_REQUESTS);
            }
        } catch (InterruptedException e) {
            return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

}
