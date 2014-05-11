package dk.loeschcke.av;

import dk.loeschcke.av.audio.AudioService;
import dk.loeschcke.av.helper.Helper;
import dk.loeschcke.av.video.VideoService;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 06/09/13
 * Time: 09.09
 * To change this template use File | Settings | File Templates.
 */
public class Connector implements Runnable {

    private DirectMediaPlayer mediaPlayer;
    private AVService service = null;
    private AVService aService;
    private AVService vService;

    public Connector(DirectMediaPlayer mediaPlayer) {
        this.aService = new AudioService(mediaPlayer);
        this.vService = new VideoService(mediaPlayer);
        //this.mainWindow = mainWindow;
        this.mediaPlayer = mediaPlayer;
    }


    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(-1);
        }
        try {
            Socket socket = serverSocket.accept();
            System.out.println("socket opened: " + socket);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("received: " + inputLine);
                try {
                    String[] split = inputLine.split(",");
                    String cmd = split[0].trim().toUpperCase();
                    double exp = Double.parseDouble(split[1]);
                    double amount = Helper.mapRange(95, 140, 1.05, 1.5, exp);
                    Command command = Command.valueOf(cmd);
                    System.out.println("cmd: " + command + "; amount: " + amount);
                    if (command == Command.SET_VIDEO) {
                        service = vService;
                        service.loadMedia();
                    } else if (command == Command.SET_AUDIO) {
                        service = aService;
                        service.loadMedia();
                    } else if (service != null) {
                        switch (command) {
                            case PLAY: service.play(); break;
                            case PAUSE: service.pause(); break;
                            case VOLUME_UP: service.volumeUp(amount); break;
                            case VOLUME_DOWN: service.volumeDown(amount); break;
                            case SKIP_FORWARD: service.skipForward(amount); break;
                            case SKIP_BACKWARD: service.skipBackward(amount); break;
                            default: break;
                        }
                    }
                } catch (IllegalArgumentException e) {
                    // never mind
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            out.close();
            in.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
