package com.nofrontier.book.config;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import java.util.HashMap;
import java.util.Map;

public class PasswordEncoderConfig {

    public PasswordEncoder passwordEncoder() {
        // Configure the PBKDF2 encoder with a random salt and suitable parameters
        Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
            "",  // Salt is managed internally by the encoder
            8,   // Hashing iterations
            185000,  // Iterations
            Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
        );

        // Define the prefix and add the encoder to the map
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder);

        // Create a DelegatingPasswordEncoder with "pbkdf2" as the default
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);

        return passwordEncoder;
    }
}