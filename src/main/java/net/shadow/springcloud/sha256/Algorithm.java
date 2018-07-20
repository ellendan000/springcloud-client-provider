package net.shadow.springcloud.sha256;

class Algorithm {

    private long[] k64 = new long[]{
            0x428a2f98L, 0x71374491L, 0xb5c0fbcfL, 0xe9b5dba5L,
            0x3956c25bL, 0x59f111f1L, 0x923f82a4L, 0xab1c5ed5L,
            0xd807aa98L, 0x12835b01L, 0x243185beL, 0x550c7dc3L,
            0x72be5d74L, 0x80deb1feL, 0x9bdc06a7L, 0xc19bf174L,
            0xe49b69c1L, 0xefbe4786L, 0x0fc19dc6L, 0x240ca1ccL,
            0x2de92c6fL, 0x4a7484aaL, 0x5cb0a9dcL, 0x76f988daL,
            0x983e5152L, 0xa831c66dL, 0xb00327c8L, 0xbf597fc7L,
            0xc6e00bf3L, 0xd5a79147L, 0x06ca6351L, 0x14292967L,
            0x27b70a85L, 0x2e1b2138L, 0x4d2c6dfcL, 0x53380d13L,
            0x650a7354L, 0x766a0abbL, 0x81c2c92eL, 0x92722c85L,
            0xa2bfe8a1L, 0xa81a664bL, 0xc24b8b70L, 0xc76c51a3L,
            0xd192e819L, 0xd6990624L, 0xf40e3585L, 0x106aa070L,
            0x19a4c116L, 0x1e376c08L, 0x2748774cL, 0x34b0bcb5L,
            0x391c0cb3L, 0x4ed8aa4aL, 0x5b9cca4fL, 0x682e6ff3L,
            0x748f82eeL, 0x78a5636fL, 0x84c87814L, 0x8cc70208L,
            0x90befffaL, 0xa4506cebL, 0xbef9a3f7L, 0xc67178f2L,
    };

    private long[] h8 = new long[]{
            0x6a09e667L, 0xbb67ae85L, 0x3c6ef372L, 0xa54ff53aL,
            0x510e527fL, 0x9b05688cL, 0x1f83d9abL, 0x5be0cd19L
    };

    void PAD(byte[] bytes) {
        int length = bytes.length;
        int restComplementLength = Math.abs(55 - length) % 64;
        int groups = (length + 8) / 64 + 1;
        int wholeLength = length + 1 + restComplementLength + 8;

        byte[] temp = new byte[wholeLength];
        System.arraycopy(bytes, 0, temp, 0, bytes.length);
        temp[length] = (byte) 0x80;
        for (int i = length + 1; i < length + restComplementLength + 1; i++) {
            temp[i] = (byte) 0x00;
        }

        for (int i = 1; i <= 8; i++) {
            temp[(groups * 64) - i] = (byte) (8L * length >> (i - 1) * 8);
        }

        long T1 = 0, T2 = 0, T3 = 0, T4 = 0;
        long[] word = new long[wholeLength / 4];
        for (int i = 0; i < wholeLength / 4; i++) {
            for (int j = 0; j < 4; j++) {
                switch (j) {
                    case 0:
                        T1 = temp[4 * i + j] & 0xFF;
                        break;
                    case 1:
                        T2 = temp[4 * i + j] & 0xFF;
                        break;
                    case 2:
                        T3 = temp[4 * i + j] & 0xFF;
                        break;
                    case 3:
                        T4 = temp[4 * i + j] & 0xFF;
                        break;
                }
            }
            word[i] = (T1 << 24) + (T2 << 16) + (T3 << 8) + T4;
        }

        long[] row = new long[16];
        for (int i = 0; i < groups; i++) {
            System.arraycopy(word, i * 16, row, 0, 16);
            compress(row);
        }
    }

    private void compress(long[] row) {
        long[] rowResult = deal(row);
        for (int i = 0; i < 8; i++) {
            System.out.print(String.format("%08x", rowResult[i]));
        }
        System.out.println();
    }

    private long[] deal(long[] m16) {
        long[] word64 = new long[64];
        System.arraycopy(m16, 0, word64, 0, 16);
        for (int i = 16; i < 64; i++) {
            word64[i] = (SSigma_1(word64[i - 2]) + word64[i - 7] + SSigma_0(word64[i - 15]) + word64[i - 16]) & 0xFFFFFFFFL;
        }

        long A, B, C, D, E, F, G, H, T1, T2;
        A = h8[0];
        B = h8[1];
        C = h8[2];
        D = h8[3];
        E = h8[4];
        F = h8[5];
        G = h8[6];
        H = h8[7];

        for (int i = 0; i < 64; i++) {
            T1 = (H + LSigma_1(E) + Conditional(E, F, G) + k64[i] + word64[i]) & 0xFFFFFFFFL;
            T2 = (LSigma_0(A) + Majority(A, B, C)) & 0xFFFFFFFFL;
            H = G;
            G = F;
            F = E;
            E = (D + T1) & 0xFFFFFFFFL;
            D = C;
            C = B;
            B = A;
            A = (T1 + T2) & 0xFFFFFFFFL;
        }

        h8[0] = (h8[0] + A) & 0xFFFFFFFFL;
        h8[1] = (h8[1] + B) & 0xFFFFFFFFL;
        h8[2] = (h8[2] + C) & 0xFFFFFFFFL;
        h8[3] = (h8[3] + D) & 0xFFFFFFFFL;
        h8[4] = (h8[4] + E) & 0xFFFFFFFFL;
        h8[5] = (h8[5] + F) & 0xFFFFFFFFL;
        h8[6] = (h8[6] + G) & 0xFFFFFFFFL;
        h8[7] = (h8[7] + H) & 0xFFFFFFFFL;
        return h8;
    }

    private long ROTR(long word, int n) {
        return (word >> n) | ((word << (32 - n)) & 0xFFFFFFFFL);
    }

    private long ROTL(long word, int n) {
        return ((word << n) & 0xFFFFFFFFL) | (word >> (32 - n));
    }

    private long SHR(long word, int n) {
        return word >> n;
    }

    private long Conditional(long x, long y, long z) {
        return (x & y) ^ ((~x) & z);
//        return z ^ (x & (y ^ z));
    }

    private long Majority(long x, long y, long z) {
        return (x & y) ^ (x & z) ^ (y & z);
//        return (x & y) | (z & (x | y));
    }

    private long LSigma_0(long x) {
        return ROTL(x, 30) ^ ROTL(x, 19) ^ ROTL(x, 10);
//        return (x >> 2 | x << 30) ^ (x >> 13 | x << 19) ^ (x >> 22 | x << 10);
    }

    private long LSigma_1(long x) {
        return ROTL(x, 26) ^ ROTL(x, 21) ^ ROTL(x, 7);
//        return (x >> 6 | x << 26) ^ (x >> 11 | x << 21) ^ (x >> 25 | x << 7);
    }

    private long SSigma_0(long x) {
        return ROTL(x, 25) ^ ROTL(x, 14) ^ SHR(x, 3);
//        return (x >> 7 | x << 25) ^ (x >> 18 | x << 14) ^ (x >> 3);
    }

    private long SSigma_1(long x) {
        return ROTL(x, 15) ^ ROTL(x, 13) ^ SHR(x, 10);
//        return (x >> 17 | x << 15) ^ (x >> 19 | x << 13) ^ (x >> 10);
    }
}
