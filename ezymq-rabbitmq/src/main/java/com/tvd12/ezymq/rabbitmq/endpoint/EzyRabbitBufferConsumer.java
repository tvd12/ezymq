package com.tvd12.ezymq.rabbitmq.endpoint;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.LinkedBlockingQueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class EzyRabbitBufferConsumer extends DefaultConsumer {

	protected volatile Exception exception;
    protected final BlockingQueue<Delivery> queue;
    protected static final Delivery POISON = new Delivery(null, null, null);

    public EzyRabbitBufferConsumer(Channel channel) {
    	super(channel);
        this.queue = new LinkedBlockingQueue<>();
    }

    public Delivery nextDelivery() throws Exception {
        Delivery delivery = queue.take();
        if(delivery == POISON)
        	throw exception;
        return delivery;
    }

    @Override
    public void handleShutdownSignal(
    		String consumerTag, ShutdownSignalException sig) {
    	this.exception = sig;
        this.queue.add(POISON);
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
    	this.exception = new CancellationException(
    			"consumer: " + consumerTag + " has cancelled");
        this.queue.add(POISON);
    }

    @Override
    public void handleDelivery(String consumerTag,
        Envelope envelope,
        AMQP.BasicProperties properties,
        byte[] body) throws IOException {
        this.queue.add(new Delivery(envelope, properties, body));
    }

}