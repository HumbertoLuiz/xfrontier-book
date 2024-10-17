package br.com.xfrontier.book.core.email;

import jakarta.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Validated
@Getter
@Setter
@Component
@ConfigurationProperties("com.nofrontier.email")
public class EmailProperties {

	private Implementation impl = Implementation.FAKE;
	
	@NotNull
	private String sender;
	
	private Sandbox sandbox = new Sandbox();
	
	public enum Implementation {
		SMTP, FAKE, SANDBOX
	}
	
	@Getter
	@Setter
	public class Sandbox {
		
		private String addressee;
		
	}
	
}
