package com.jaggy.Musica.handlers;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public interface PlayHandler {
	void play(Message message, String url, boolean playNext);

	void playTrack(AudioTrack audioTrack, boolean playNext);

	List<AudioTrack> getQueue();

	AudioTrack getCurrentlyPlaying();

	AudioTrack skip();

	void clear();

	void shuffle();
}
