//package com.nofrontier.book.configs;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.nofrontier.book.dto.v1.ErrorResponse;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//public class JacksonConfigTest {
//
//    @Test
//    public void testSerialization() throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//
//        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setStatus(400);
//        errorResponse.setTimestamp(LocalDateTime.now());
//        errorResponse.setMessage("Error message");
//        errorResponse.setPath("/api/path");
//
//        String json = mapper.writeValueAsString(errorResponse);
//        assertNotNull(json);
//        System.out.println(json);
//    }
//}
