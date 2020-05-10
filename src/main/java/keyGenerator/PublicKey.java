package keyGenerator;

import java.io.Serializable;
import java.math.BigInteger;

public interface PublicKey extends Serializable {
    BigInteger[] encrypt(String text);
}
