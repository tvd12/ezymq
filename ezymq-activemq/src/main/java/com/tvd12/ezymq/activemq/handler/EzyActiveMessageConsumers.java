package com.tvd12.ezymq.activemq.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tvd12.ezyfox.util.EzyLoggable;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EzyActiveMessageConsumers extends EzyLoggable {
	
	protected final Map<String, List<EzyActiveMessageConsumer>> consumers;

	public EzyActiveMessageConsumers() {
		 this.consumers = new HashMap<>();
	}
	
    public void addConsumer(String cmd, EzyActiveMessageConsumer consumer) {
    	synchronized (consumers) {
    		List<EzyActiveMessageConsumer> consumerList 
    			= consumers.computeIfAbsent(cmd, k -> new ArrayList<>());
    		consumerList.add(consumer);
		}
    }

    public List<EzyActiveMessageConsumer> getComsumers(String cmd) {
    	List<EzyActiveMessageConsumer> answer = null;
    	synchronized (consumers) {
			answer = consumers.get(cmd);
		}
        if (answer != null)
            return answer;
        return Collections.EMPTY_LIST;
    }

	public void consume(String cmd, Object message) {
		List<EzyActiveMessageConsumer> consumerList = getComsumers(cmd);
		for(EzyActiveMessageConsumer consumer : consumerList) {
			try {
				consumer.consume(message);
			}
			catch (Exception e) {
				logger.warn("consume command: {} message: {} error", cmd, message);
			}
		}
    }
}
