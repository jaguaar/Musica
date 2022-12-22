package com.jaggy.Musica.handlers;

import java.nio.ByteBuffer;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;

@Component
public class SoundHandler implements AudioSendHandler {
	private final AudioPlayer audioPlayer;
	private final AudioPlayerManager manager;
	private final TrackScheduler trackScheduler;
	private final ByteBuffer buffer;
	private final MutableAudioFrame frame;

	@Autowired
	public SoundHandler(@Value("${discord.volume}") final int volume) {
		this.manager = new DefaultAudioPlayerManager();
		this.audioPlayer = manager.createPlayer();
		this.audioPlayer.setVolume(volume);

		this.buffer = ByteBuffer.allocate(1024);
		this.frame = new MutableAudioFrame();

		this.trackScheduler = new TrackScheduler(audioPlayer);
		this.audioPlayer.addListener(trackScheduler);

		this.frame.setBuffer(buffer);
	}

	@Override
	public boolean canProvide() {
		return this.audioPlayer.provide(this.frame);
	}

	@Nullable
	@Override
	public ByteBuffer provide20MsAudio() {
		return this.buffer.flip();
	}

	@Override
	public boolean isOpus() {
		return true;
	}

	public TrackScheduler getTrackScheduler() {
		return trackScheduler;
	}
}
