package com.cf.model;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Dmitriy on 06.03.2015.
 */
public class Model {

    private AtomicInteger totalMessages = new AtomicInteger(0);
    private ConcurrentMap<String, AtomicDouble> amountsSell = new ConcurrentHashMap<>();
    private ConcurrentMap<String, AtomicDouble> amountsBuy = new ConcurrentHashMap<>();


    public void incTotalMessages() {
        totalMessages.incrementAndGet();
    }

    public int getTotalMessages() {
        return totalMessages.get();
    }

    public ConcurrentMap<String, AtomicDouble> getAmountsSell() {
        return amountsSell;
    }

    public ConcurrentMap<String, AtomicDouble> getAmountsBuy() {
        return amountsBuy;
    }
}
