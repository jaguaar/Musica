package com.jaggy.Musica.handlers;

import com.jaggy.Musica.spotify.SpotifyUtils;
import com.jaggy.Musica.youtube.YoutubeUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.random.RandomGenerator;

@Component
public class PlayHandlerImpl implements PlayHandler {
    private static final Logger LOG = LogManager.getLogger(PlayHandlerImpl.class);
    private final SoundHandler soundHandler;
    private final YoutubeUtils youtubeUtils;
    private final SpotifyUtils spotifyUtils;
    private final Properties overruleList = new Properties();
    private final boolean enableOverruling;
    private final int randomFactor;

    private VoiceChannel currentChannel;


    @Autowired
    public PlayHandlerImpl(final SoundHandler soundHandler, final YoutubeUtils youtubeUtils, final SpotifyUtils spotifyUtils,
                           @Value("${play.random.overruling.enable}") final boolean enableOverruling,
                           @Value("${play.random.overruling.factor}") final int randomFactor) {
        this.soundHandler = soundHandler;
        this.youtubeUtils = youtubeUtils;
        this.spotifyUtils = spotifyUtils;
        this.enableOverruling = enableOverruling;
        this.randomFactor = randomFactor;
        if (enableOverruling) {
            loadOverrules();
        }

    }

    @Override
    public void play(final Message message, final List<String> args, final boolean playNext) {
        final VoiceChannel channel = message.getMember().getVoiceState().getChannel();

        if (channel != null) {
            if (currentChannel != channel) {
                joinChannel(message, channel);
            }

            final String name = message.getAuthor().getName();
            final String url = shouldOverrule(name) ? overruleList.getProperty(name, args.get(0)) : args.get(0);

            if (spotifyUtils.isSpotifyPlaylist(url)) {
                final List<String> songTitles = spotifyUtils.getSongTitles(url);
                songTitles.parallelStream().map(youtubeUtils::searchSong).forEachOrdered(track -> playTrack(track, playNext));
                message.getChannel().sendMessage(":arrow_forward: Added " + songTitles.size() + " songs from the playlist to the Queue!").queue();
                return;
            }

            final AudioTrack audioTrack;

            if (youtubeUtils.isYoutubeSong(url)) {
                audioTrack = youtubeUtils.getAudioTrack(url);
            } else if (spotifyUtils.isSpotifySong(url)) {
                final String songTitle = spotifyUtils.getSongTitle(url);
                audioTrack = youtubeUtils.searchSong(songTitle);
            } else {
                // Just search it
                audioTrack = youtubeUtils.searchSong(String.join(" ", args));
            }

            message.getChannel()
                    .sendMessage(":arrow_forward: Added " + audioTrack.getInfo().title + " to the Queue! (" + audioTrack.getInfo().uri + ")").queue();
            playTrack(audioTrack, playNext);
        }
    }

    private boolean shouldOverrule(final String name) {
        if (!enableOverruling) {
            return false;
        }
        if (!overruleList.containsKey(name)) {
            return false;
        }
        return RandomGenerator.getDefault().nextInt() % randomFactor == 0;
    }

    @Override
    public void playTrack(final AudioTrack audioTrack, final boolean playNext) {
        soundHandler.getTrackScheduler().queue(audioTrack, playNext);
    }

    private void joinChannel(final Message message, final VoiceChannel channel) {
        currentChannel = channel;

        final AudioManager manager = message.getGuild().getAudioManager();
        manager.setSendingHandler(soundHandler);
        manager.openAudioConnection(channel);
    }

    @Override
    public List<AudioTrack> getQueue() {
        return soundHandler.getTrackScheduler().getQueue();
    }

    @Override
    public AudioTrack getCurrentlyPlaying() {
        return soundHandler.getTrackScheduler().getCurrentlyPlaying();
    }

    @Override
    public AudioTrack skip() {
        final AudioTrack currentlyPlaying = soundHandler.getTrackScheduler().getCurrentlyPlaying();
        soundHandler.getTrackScheduler().skip();
        return currentlyPlaying;
    }

    @Override
    public void clear() {
        soundHandler.getTrackScheduler().clear();
    }

    @Override
    public void shuffle() {
        soundHandler.getTrackScheduler().shuffle();
    }

    private void loadOverrules() {
        try (final InputStream input = PlayHandlerImpl.class.getClassLoader().getResourceAsStream("overrule.properties")) {
            overruleList.load(input);
        } catch (final IOException e) {
            LOG.error("Could not load short cut list", e);
        }
    }
}
