package dk.loeschcke.av;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import javafx.application.Application;
import javafx.stage.Stage;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 05/09/13
 * Time: 12.42
 * To change this template use File | Settings | File Templates.
 */
public class AVApplication extends Application  {

    private Stage primaryStage;
    private AVFX mainWindow;
    private final DirectMediaPlayer mediaPlayer;

    private AVService avService;

    public static void main(final String[] args) {
        // Load native vlc library
        NativeLibrary.addSearchPath(
                RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib"
        );
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

        // Launch JavaFX app
        Application.launch();
    }

    public AVApplication() {

        mainWindow = new AVFX();
        mediaPlayer = mainWindow.getMediaPlayerComponent().getMediaPlayer();
        // Start server socket and video service
        Connector connector = new Connector(mediaPlayer);
        new Thread(connector).start();
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        mainWindow.start(primaryStage);
    }

    @Override
    public void stop() throws Exception {
        mediaPlayer.stop();
        mediaPlayer.release();
    }

}
