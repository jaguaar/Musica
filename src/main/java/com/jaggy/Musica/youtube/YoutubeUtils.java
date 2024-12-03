package com.jaggy.Musica.youtube;


import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchProvider;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.BasicAudioPlaylist;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.track.YoutubeAudioTrack;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class YoutubeUtils {
    private final YoutubeAudioSourceManager youtubeAudioSourceManager;

    public YoutubeUtils(@Value("${refresh.token}") final String refreshToken) {
        youtubeAudioSourceManager = new YoutubeAudioSourceManager();

        youtubeAudioSourceManager.useOauth2(refreshToken, false);
    }

    public boolean isYoutubeSong(String input) {
        return input.toLowerCase().contains("youtube.com") || input.toLowerCase().contains("youtu.be");
    }

    public YoutubeAudioTrack getAudioTrack(final String song, @NotNull AudioPlayerManager manager) {
        String videoId;

        if (song.contains("?v=")) {
            videoId = song.substring(song.indexOf("?v=") + 3);
        } else {
            videoId = song.substring(song.lastIndexOf("/") + 1);
        }

        return (YoutubeAudioTrack) youtubeAudioSourceManager.loadItem(manager, new AudioReference(videoId, ""));
    }

    public AudioTrack searchSong(String song) {
        YoutubeSearchProvider youtubeSearchProvider = new YoutubeSearchProvider();
        final AudioItem audioItem = youtubeSearchProvider.loadSearchResult(song, audioTrackInfo -> new YoutubeAudioTrack(audioTrackInfo, youtubeAudioSourceManager));

        return ((BasicAudioPlaylist) audioItem).getTracks().get(0);
    }
}
