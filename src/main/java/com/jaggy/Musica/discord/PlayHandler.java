package com.jaggy.Musica.discord;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public interface PlayHandler {
	void play(GuildMessageReceivedEvent event, String url, boolean playNext);

	void playTrack(AudioTrack audioTrack, boolean playNext);

	List<AudioTrack> getQueue();

	AudioTrack getCurrentlyPlaying();

	void skip();

	void clear();

	void shuffle();
}
