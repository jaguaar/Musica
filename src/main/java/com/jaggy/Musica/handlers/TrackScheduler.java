package com.jaggy.Musica.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackScheduler extends AudioEventAdapter {
	private final AudioPlayer player;

	private final List<AudioTrack> queue;
	private AudioTrack currentlyPlaying;

	public TrackScheduler(final AudioPlayer player) {
		this.player = player;
		queue = new ArrayList<>();
	}

	@Override
	public void onPlayerPause(AudioPlayer player) {
		System.out.println("pause");
	}

	@Override
	public void onPlayerResume(AudioPlayer player) {
		System.out.println("resume");
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		System.out.println("started");
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		System.out.println("Stopped song for reason " + endReason);

		currentlyPlaying = null;

		if (endReason.mayStartNext) {
			if(!queue.isEmpty()) {
				AudioTrack nextTrack = queue.get(0);
				queue.remove(nextTrack);

				player.playTrack(nextTrack);
				currentlyPlaying = nextTrack;
				System.out.println("Starting queued song " + nextTrack.getInfo().title);
			}
		}
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
	}

	public void queue(final AudioTrack track, boolean playNext) {
		if(player.getPlayingTrack() == null) {
			System.out.println("Starting " + track.getInfo().title);
			player.playTrack(track);
			currentlyPlaying = track;
		} else {
			if(playNext) {
				System.out.println("Queueing as next song " + track.getInfo().title);
				queue.add(0, track);
				return;
			}

			System.out.println("Queueing " + track.getInfo().title);
			queue.add(track);
		}
	}

	public AudioTrack getCurrentlyPlaying() {
		return currentlyPlaying;
	}

	public List<AudioTrack> getQueue() {
		return queue;
	}

	public void skip() {
		this.onTrackEnd(player, currentlyPlaying, AudioTrackEndReason.FINISHED);
	}

	public void clear() {
		queue.clear();
		this.onTrackEnd(player, currentlyPlaying, AudioTrackEndReason.FINISHED);
	}

	public void shuffle() {
		Collections.shuffle(queue);
	}
}
