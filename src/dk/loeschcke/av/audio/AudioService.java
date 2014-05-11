package dk.loeschcke.av.audio;

import dk.loeschcke.av.AVService;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;

/**
 * Created with IntelliJ IDEA.
 * User: sbugge
 * Date: 06/09/13
 * Time: 10.00
 * To change this template use File | Settings | File Templates.
 */
public class AudioService implements AVService {


    private DirectMediaPlayer mediaPlayer;

    private String[] audioList = new String[] {
            "/Users/sbugge/Music/The Eraser/01 The Eraser.mp3",
            "/Users/sbugge/Music/Trolle Siebenhaar Couple Therapy/03 Sweet Dogs.mp3"
    };
    private int currentAudio = 0;

    public AudioService(DirectMediaPlayer mediaPlayer) {

        this.mediaPlayer = mediaPlayer;
    }

    public void loadMedia() {
        mediaPlayer.playMedia(audioList[currentAudio]);
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
        volume = (int) (volume * amount);
        System.out.println("volume: " + volume);
        mediaPlayer.setVolume(volume);
    }

    @Override
    public void volumeDown(double amount) {
        int volume = mediaPlayer.getVolume();
        volume = (int) (volume * amount);
        System.out.println("volume: " + volume);
        mediaPlayer.setVolume(volume);
    }

    @Override
    public void skipForward(double amount) {
        if (currentAudio < audioList.length-1) currentAudio++;
        else currentAudio = 0;
        mediaPlayer.stop();
        loadMedia();
    }

    @Override
    public void skipBackward(double amount) {
        if (currentAudio > 0) currentAudio--;
        else currentAudio = audioList.length-1;
        mediaPlayer.stop();
        loadMedia();
    }
}
