package com.tvd12.ezymq.kafka.setting;

import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;

import com.tvd12.ezymq.kafka.handler.EzyKafkaActionInterceptor;
import com.tvd12.ezymq.kafka.handler.EzyKafkaRequestHandler;
import com.tvd12.ezymq.kafka.handler.EzyKafkaRequestHandlers;

import lombok.Getter;

@Getter
@SuppressWarnings("rawtypes")
public class EzyKafkaHandlerSetting extends EzyKafkaEndpointSetting {

	protected final long pollTimeOut;
	protected final Consumer consumer;
	protected final int threadPoolSize;
	protected final EzyKafkaRequestHandlers requestHandlers;
	protected final EzyKafkaActionInterceptor actionInterceptor;
	
	public EzyKafkaHandlerSetting(
			String topic,
			Consumer consumer, 
			long poolTimeOut,
			int threadPoolSize,
			EzyKafkaRequestHandlers requestHandlers,
			EzyKafkaActionInterceptor actionInterceptor,
			Map<String, Object> properties) {
		super(topic, properties);
		this.consumer = consumer;
		this.pollTimeOut = poolTimeOut;
		this.threadPoolSize = threadPoolSize;
		this.requestHandlers = requestHandlers;
		this.actionInterceptor = actionInterceptor;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyKafkaEndpointSetting.Builder<Builder> {
		
		protected Consumer consumer;
		protected int threadPoolSize = 3;
		protected long pollTimeOut = 100;
		protected EzyKafkaRequestHandlers requestHandlers;
		protected EzyKafkaActionInterceptor actionInterceptor;
		protected EzyKafkaSettings.Builder parent;
		
		public Builder() {
			this(null);
		}
		
		public Builder(EzyKafkaSettings.Builder parent) {
			super();
			this.parent = parent;
		}
		
		public Builder pollTimeOut(long pollTimeOut) {
			this.pollTimeOut = pollTimeOut;
			return this;
		}
		
		public Builder threadPoolSize(int threadPoolSize) {
			this.threadPoolSize = threadPoolSize;
			return this;
		}
		
		public Builder consumer(Consumer consumer) {
			this.consumer = consumer;
			return this;
		}
		
		public Builder requestHandlers(EzyKafkaRequestHandlers requestHandlers) {
			this.requestHandlers = requestHandlers;
			return this;
		}
		
		public Builder actionInterceptor(EzyKafkaActionInterceptor actionInterceptor) {
			this.actionInterceptor = actionInterceptor;
			return this;
		}
		
		public Builder addRequestHandler(String cmd, EzyKafkaRequestHandler handler) {
			if(requestHandlers == null)
				requestHandlers = new EzyKafkaRequestHandlers();
			requestHandlers.addHandler(cmd, handler);
			return this;
	    }
		
		public Builder addRequestHandler(Map<String, EzyKafkaRequestHandler> handlers) {
			for(String cmd : handlers.keySet()) {
				EzyKafkaRequestHandler handler = handlers.get(cmd);
				addRequestHandler(cmd, handler);
			}
			return this;
	    }
		
		public EzyKafkaSettings.Builder parent() {
			return parent;
		}
		
		@Override
		public EzyKafkaHandlerSetting build() {
			if(requestHandlers == null)
				throw new NullPointerException("requestHandlers can not be null");
			return new EzyKafkaHandlerSetting(
					topic,
					consumer,
					pollTimeOut,
					threadPoolSize,
					requestHandlers,
					actionInterceptor,
					properties);
		}
		
	}

}
