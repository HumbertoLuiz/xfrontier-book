package br.com.xfrontier.book.config;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.jsonwebtoken.io.IOException;

@Configuration
public class JacksonConfig {

    @Bean
    ObjectMapper objectMapper() {
    	
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer (Long.class , ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        // Adiciona o custom serializer para OffsetDateTime
        simpleModule.addSerializer(OffsetDateTime.class, new JsonSerializer<OffsetDateTime>() {
            @Override
            public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException, java.io.IOException {
                // Define o formato desejado para serialização
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                gen.writeString(value.format(formatter));
            }
        });
        
        // Adiciona o custom deserializer para OffsetDateTime
        simpleModule.addDeserializer(OffsetDateTime.class, new JsonDeserializer<OffsetDateTime>() {
            @Override
            public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, java.io.IOException {
                // Define o formato esperado para desserialização
                String date = p.getText();
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                return OffsetDateTime.parse(date, formatter);
            }
        });

        
        ObjectMapper mapper = new ObjectMapper();

        // Registrar módulo para manipulação de Java Time
        mapper.registerModule(new JavaTimeModule());
        
        mapper.registerModule(simpleModule);

        // Configurações de StreamWriteConstraints
        mapper.getFactory().setStreamWriteConstraints(StreamWriteConstraints.builder()
                .maxNestingDepth(5000) // Ajuste a profundidade máxima de aninhamento
                .build());

        // Configurações de serialização e desserialização
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        
        mapper.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        mapper.findAndRegisterModules();

        return mapper;
    }
}
