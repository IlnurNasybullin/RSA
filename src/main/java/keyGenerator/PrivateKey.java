package keyGenerator;

import java.math.BigInteger;

public interface PrivateKey {
    String decrypt(BigInteger[] symbols);
}
