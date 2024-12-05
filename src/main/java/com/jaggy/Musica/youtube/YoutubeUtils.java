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

import java.util.List;

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

    public List<AudioTrack> getAudioTracks(final String url, @NotNull AudioPlayerManager manager) {
        AudioItem audioItem = youtubeAudioSourceManager.loadItem(manager, new AudioReference(url, ""));

        if (audioItem instanceof BasicAudioPlaylist playlist) {
            return playlist.getTracks();
        } else if (audioItem instanceof YoutubeAudioTrack youtubeAudioTrack) {
            return List.of(youtubeAudioTrack);
        } else {
            throw new IllegalStateException("Unkown type " + audioItem);
        }
    }

    public AudioTrack searchSong(String song) {
        YoutubeSearchProvider youtubeSearchProvider = new YoutubeSearchProvider();
        final AudioItem audioItem = youtubeSearchProvider.loadSearchResult(song, audioTrackInfo -> new YoutubeAudioTrack(audioTrackInfo, youtubeAudioSourceManager));
        return ((BasicAudioPlaylist) audioItem).getTracks().get(0);
    }
}
