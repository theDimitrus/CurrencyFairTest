package com.cf.processor;

import com.cf.dto.TradeMessage;
import com.cf.model.Model;
import com.google.common.util.concurrent.AtomicDouble;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Dmitriy on 06.03.2015.
 */
@Component
public class TradeMessageProcessor implements ApplicationListener<ContextStoppedEvent> {

    @Resource(name = "messageQueue")
    public BlockingQueue<TradeMessage> messageQueue;

    @Autowired
    public Model model;

    private ExecutorService executorService;

    @PostConstruct
    public void start() {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            executorService.submit(new MessageHandler());
        }
    }


    @Override
    public void onApplicationEvent(ContextStoppedEvent contextStoppedEvent) {
        executorService.shutdownNow();
    }

    private class MessageHandler implements Runnable {
        @Override
        public void run() {
            while (! Thread.currentThread().isInterrupted()) {
                TradeMessage message;
                try {
                    message = messageQueue.take();
                } catch (InterruptedException e) {
                    return;
                }
                handleMessage(message);
            }
        }

        private void handleMessage(TradeMessage message) {
            model.incTotalMessages();

            AtomicDouble a = new AtomicDouble(0);
            AtomicDouble p = model.getAmountsBuy().putIfAbsent(message.getCurrencyFrom(),a);
            if (p != null) a = p;
            a.addAndGet(message.getAmountBuy());

            a = new AtomicDouble(0);
            p = model.getAmountsSell().putIfAbsent(message.getCurrencyTo(),a);
            if (p != null) a = p;
            a.addAndGet(message.getAmountSell());
        }
    }
}
