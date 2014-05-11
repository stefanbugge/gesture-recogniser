package dk.loeschcke.av.video;

import dk.loeschcke.av.AVService;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 05/09/13
 * Time: 12.35
 * To change this template use File | Settings | File Templates.
 */
public class VideoService implements AVService {


    private DirectMediaPlayer mediaPlayer;

    private String[] videoList = new String[] {
        "/Users/sbugge/Movies/test.mp4",
        "/Users/sbugge/Movies/test2.mp4"
    };
    private int currentVideo = 0;

    public VideoService(DirectMediaPlayer mediaPlayer) {

        this.mediaPlayer = mediaPlayer;
    }

    public void loadMedia() {
        mediaPlayer.playMedia(videoList[currentVideo]);
    }

    @Override
    public void play() {
        if (!mediaPlayer.isPlayable()) { // then load a video file
            loadMedia();
        } else if (!mediaPlayer.isPlaying()) {
            mediaPlayer.play();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.play();
        }
    }

    @Override
    public void volumeUp(double amount) {
        int volume = mediaPlayer.getVolume();
        volume = (int) (volume + amount);
        mediaPlayer.setVolume(volume);
    }

    @Override
    public void volumeDown(double amount) {
        int volume = mediaPlayer.getVolume();
        volume = (int) (volume + amount);
        mediaPlayer.setVolume(volume);
    }

    @Override
    public void skipForward(double amount) {
        if (currentVideo < videoList.length-1) currentVideo++;
        else currentVideo = 0;
        mediaPlayer.stop();
        loadMedia();
    }

    @Override
    public void skipBackward(double amount) {
        if (currentVideo > 0) currentVideo--;
        else currentVideo = videoList.length-1;
        mediaPlayer.stop();
        loadMedia();
    }
}
