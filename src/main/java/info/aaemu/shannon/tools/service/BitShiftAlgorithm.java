package info.aaemu.shannon.tools.service;

/**
 * @author Shannon
 */
public interface BitShiftAlgorithm {

    String shift(String constant, int aesKeySize);

    String shift(String constant);
}
