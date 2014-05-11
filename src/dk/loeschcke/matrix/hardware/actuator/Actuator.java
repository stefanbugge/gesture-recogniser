package dk.loeschcke.matrix.hardware.actuator;

public interface Actuator {

    void connect();

    void disconnect();

    void sendFeedback(int type, int repeats, int value);
}
