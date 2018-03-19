package io.projectriff.sample.twitter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author David Turanski
 **/
@ConfigurationProperties(prefix = "riff.gateway")
public class RiffGatewayProperties {
	private String host;
	private int port;
	private String topic;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String url() {
		return String.format("http://%s:%d/messages/%s",host,port,topic);
	}

}
