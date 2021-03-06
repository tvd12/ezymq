package com.tvd12.ezymq.activemq.setting;

import javax.jms.Destination;
import javax.jms.Session;

import lombok.Getter;

@Getter
public class EzyActiveTopicSetting extends EzyActiveEndpointSetting {

	protected final String topicName;
	protected final Destination topic;
	protected final boolean clientEnable;
	protected final boolean serverEnable;
	protected final int serverThreadPoolSize;
	
	public EzyActiveTopicSetting(
			Session session,
			String topicName,
			Destination topic,
			boolean clientEnable,
			boolean serverEnable,
			int serverThreadPoolSize) {
		super(session);
		this.topic = topic;
		this.topicName = topicName;
		this.clientEnable = clientEnable;
		this.serverEnable = serverEnable;
		this.serverThreadPoolSize = serverThreadPoolSize;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyActiveEndpointSetting.Builder<Builder> {

		protected String topicName;
		protected Destination topic;
		protected boolean clientEnable;
		protected boolean serverEnable;
		protected int serverThreadPoolSize;
		protected EzyActiveSettings.Builder parent;
		
		public Builder() {
			this(null);
		}
		
		public Builder(EzyActiveSettings.Builder parent) {
			this.parent = parent;
		}
		
		public Builder topic(Destination topic) {
			this.topic = topic;
			return this;
		}
		
		public Builder topicName(String topicName) {
			this.topicName = topicName;
			return this;
		}
		
		public Builder clientEnable(boolean clientEnable) {
			this.clientEnable = clientEnable;
			return this;
		}
		
		public Builder serverEnable(boolean serverEnable) {
			this.serverEnable = serverEnable;
			return this;
		}
		
		public Builder serverThreadPoolSize(int serverThreadPoolSize) {
			this.serverThreadPoolSize = serverThreadPoolSize;
			return this;
		}
		
		public EzyActiveSettings.Builder parent() {
			return parent;
		}
		
		@Override
		public EzyActiveEndpointSetting build() {
			return new EzyActiveTopicSetting(
					session,
					topicName,
					topic,
					clientEnable,
					serverEnable,
					serverThreadPoolSize
			);
		}
	}

}
