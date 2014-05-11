package dk.loeschcke.av;


/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 05/09/13
 * Time: 12.45
 * To change this template use File | Settings | File Templates.
 */
public interface AVService {

    void play();

    void pause();

    void volumeUp(double amount);

    void volumeDown(double amount);

    void skipForward(double amount);

    void skipBackward(double amount);

    void loadMedia();
}
