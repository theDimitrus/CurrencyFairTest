package com.cf.controller;

import com.cf.model.Model;
import com.google.common.util.concurrent.AtomicDouble;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Dmitriy on 07.03.2015.
 */
@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private Model model;


    @RequestMapping(value = "/{currency}", method = RequestMethod.GET)
    public Object getCurrencyInfo(@PathVariable("currency") String currency) {
        AtomicDouble b = model.getAmountsBuy().getOrDefault(currency,new AtomicDouble(0));
        AtomicDouble s = model.getAmountsSell().getOrDefault(currency,new AtomicDouble(0));
        Map<String, Object> m = new HashMap<>();
        m.put("currency", currency);
        m.put("amountSell", s.doubleValue());
        m.put("amountBuy", b.doubleValue());
        return m;
    }

}
