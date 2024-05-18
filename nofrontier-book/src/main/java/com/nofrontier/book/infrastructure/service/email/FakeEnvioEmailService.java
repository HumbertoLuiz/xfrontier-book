//package com.algaworks.algafood.infrastructure.service.email;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.algaworks.algafood.domain.service.EnvioEmailService;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class FakeEnvioEmailService implements EnvioEmailService {
//
//	@Autowired
//	private ProcessadorEmailTemplate processadorEmailTemplate;
//
//	@Override
//	public void enviar(Mensagem mensagem) {
//		String corpo = processadorEmailTemplate.processarTemplate(mensagem);
//
//		log.info("[FAKE E-MAIL] Para: {}\n{}", mensagem.getDestinatarios(), corpo);
//	}
//
//}
