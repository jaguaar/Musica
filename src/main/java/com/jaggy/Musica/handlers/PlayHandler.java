package com.jaggy.Musica.handlers;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Message;

public interface PlayHandler {
	void play(Message message, List<String> args, boolean playNext, boolean shuffle);

	void playTrack(AudioTrack audioTrack, boolean playNext);

	List<AudioTrack> getQueue();

	AudioTrack getCurrentlyPlaying();

	AudioTrack skip();

	void clear();

	void shuffle();
}
