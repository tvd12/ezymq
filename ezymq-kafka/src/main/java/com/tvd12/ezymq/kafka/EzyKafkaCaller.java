package com.tvd12.ezymq.kafka;

import java.util.concurrent.TimeoutException;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;
import com.tvd12.ezyfox.exception.EzyTimeoutException;
import com.tvd12.ezyfox.exception.InternalServerErrorException;
import com.tvd12.ezyfox.message.EzyMessageTypeFetcher;
import com.tvd12.ezyfox.util.EzyCloseable;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezymq.kafka.endpoint.EzyKafkaClient;

public class EzyKafkaCaller extends EzyLoggable implements EzyCloseable {

	protected final EzyKafkaClient client;
	protected final EzyEntityCodec entityCodec;

	public EzyKafkaCaller(
			EzyKafkaClient client, EzyEntityCodec entityCodec) {
        this.client = client;
        this.entityCodec = entityCodec;
    }
	
	public void send(Object data) {
        if (!(data instanceof EzyMessageTypeFetcher))
            throw new IllegalArgumentException("data class must implement 'EzyMessageTypeFetcher'");
        EzyMessageTypeFetcher mdata = (EzyMessageTypeFetcher)data;
        send(mdata.getMessageType(), data);
    }

    public void send(String cmd, Object data) {
        byte[] requestMessage = entityCodec.serialize(data);
        rawSend(cmd, requestMessage);
    }
	
    protected void rawSend(String cmd, byte[] requestMessage) {
    		try {
			client.send(cmd, requestMessage);
		} 
    	catch (TimeoutException e) {
			throw new EzyTimeoutException("call request: " + cmd + " timeout", e);
		}
    	catch (Exception e) {
    		throw new InternalServerErrorException(e.getMessage(), e);
		}
    }
    
    @Override
    public void close() {
    	client.close();
    }
    
    public static Builder builder() {
    	return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyKafkaCaller> {

    	protected EzyKafkaClient client;
    	protected EzyEntityCodec entityCodec;
    	
    	public Builder client(EzyKafkaClient client) {
    		this.client = client;
    		return this;
    	}
    	
    	public Builder entityCodec(EzyEntityCodec entityCodec) {
    		this.entityCodec = entityCodec;
    		return this;
    	}
    	
		@Override
		public EzyKafkaCaller build() {
			return new EzyKafkaCaller(client, entityCodec);
		}
    	
    }
    
}
