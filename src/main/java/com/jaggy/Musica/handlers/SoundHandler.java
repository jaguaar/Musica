package com.jaggy.Musica.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public class SoundHandler implements AudioSendHandler {
	private final AudioPlayer audioPlayer;
	private final AudioPlayerManager manager;
	private final TrackScheduler trackScheduler;
	private ByteBuffer buffer;
	private final MutableAudioFrame frame;

	@Autowired
	public SoundHandler() {
		manager = new DefaultAudioPlayerManager();
		this.audioPlayer = manager.createPlayer();

		buffer = ByteBuffer.allocate(1024);
		this.frame = new MutableAudioFrame();


		trackScheduler = new TrackScheduler(audioPlayer);
		audioPlayer.addListener(trackScheduler);

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
