package net.shadow.springcloud.sha256;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class Calculate {

    private static String guavaSHA256(String originalString) {
        return Hashing.sha256()
                .hashString(originalString, StandardCharsets.UTF_8)
                .toString();
    }

    public static void main(String[] args) {
        System.out.println(guavaSHA256("��ǰ����ɽ��ɽ���и���"));

        Algorithm algorithm = new Algorithm();
        byte[] input = "��ǰ����ɽ��ɽ���и���".getBytes(StandardCharsets.UTF_8);
        algorithm.PAD(input);
    }
}
