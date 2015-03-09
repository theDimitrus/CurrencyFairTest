package com.cf.processor;

import com.cf.dto.TradeMessage;
import com.cf.model.Model;

import static org.junit.Assert.*;

import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Dmitriy on 07.03.2015.
 */
public class TradeMessageProcessorTest {

    private TradeMessageProcessor processor;
    private BlockingQueue<TradeMessage> queue;
    private Model model;

    @Before
    public void init() {
        queue = new ArrayBlockingQueue<TradeMessage>(100);
        processor = new TradeMessageProcessor();
        processor.messageQueue = queue;
        processor.start();
    }


    @Test
    public void shouldHandleMessageCorrectly() throws Exception {
        model = new Model();
        processor.model = model;
        TradeMessage message = new TradeMessage();
        message.setAmountBuy(100D);
        message.setAmountSell(50D);
        message.setCurrencyFrom("EUR");
        message.setCurrencyTo("GBP");

        queue.put(message);

        Thread.sleep(100);

        assertThat(model.getAmountsBuy().get("EUR").doubleValue(), is(100D));
        assertThat(model.getAmountsSell().get("GBP").doubleValue(), is(50D));
        assertNull(model.getAmountsBuy().get("GBP"));
        assertNull(model.getAmountsSell().get("EUR"));
    }


    @Test
    public void shouldHandleMessagesCorrectlyInFewThreads() {
        model = new Model();
        processor.model = model;

        CountDownLatch l1 = new CountDownLatch(3);
        CountDownLatch l2 = new CountDownLatch(3);


        new Thread(new Runnable() {
            @Override
            public void run() {
                l1.countDown();
                try {
                    l1.await();
                } catch (InterruptedException e) {

                }
                TradeMessage message = new TradeMessage();
                message.setAmountBuy(100D);
                message.setAmountSell(50D);
                message.setCurrencyFrom("EUR");
                message.setCurrencyTo("GBP");
                try {
                    queue.put(message);
                } catch (InterruptedException e) {

                }
                l2.countDown();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                l1.countDown();
                try {
                    l1.await();
                } catch (InterruptedException e) {

                }
                TradeMessage message = new TradeMessage();
                message.setAmountBuy(200D);
                message.setAmountSell(100D);
                message.setCurrencyFrom("EUR");
                message.setCurrencyTo("GBP");
                try {
                    queue.put(message);
                } catch (InterruptedException e) {

                }
                l2.countDown();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                l1.countDown();
                try {
                    l1.await();
                } catch (InterruptedException e) {

                }
                TradeMessage message = new TradeMessage();
                message.setAmountBuy(300D);
                message.setAmountSell(150D);
                message.setCurrencyFrom("EUR");
                message.setCurrencyTo("GBP");
                try {
                    queue.put(message);
                } catch (InterruptedException e) {

                }
                l2.countDown();
            }
        }).start();

        try {
            l2.await();
        } catch (InterruptedException e) {

        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {

        }

        assertThat(model.getAmountsBuy().get("EUR").doubleValue(), is(600D));
        assertThat(model.getAmountsSell().get("GBP").doubleValue(), is(300D));
        assertNull(model.getAmountsBuy().get("GBP"));
        assertNull(model.getAmountsSell().get("EUR"));
    }

}
