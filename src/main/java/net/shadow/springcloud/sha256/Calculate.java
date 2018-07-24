package net.shadow.springcloud.sha256;

import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class Calculate {

    private static String guavaSHA256(String originalString) {
        return Hashing.sha256()
                .hashString(originalString, StandardCharsets.UTF_8)
                .toString();
    }

    public static void main(String[] args) {
        log.info(guavaSHA256("从前有座山，山上有个庙"));

        Algorithm algorithm = new Algorithm();
        byte[] input = "从前有座山，山上有个庙".getBytes(StandardCharsets.UTF_8);
        algorithm.PAD(input);
    }
}
