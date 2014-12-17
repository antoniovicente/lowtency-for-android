package com.antoniovm.lowtency.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;

/**
 * This class handles the audio stream and sends it to the output device
 *
 * @author Antonio Vicente Martin
 */
public class AudioOutputManager extends AudioIOManger {

    private AudioTrack audioTrack;
    private byte[] samplesBuffer;

    public AudioOutputManager(int chunkSizeInSamples) {
        this(AudioIOManger.DEFAULT_SAMPLERATE, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, chunkSizeInSamples);
    }

    public AudioOutputManager(int sampleRate, int channelFormat, int encodingFormat, int chunkSizeInSamples) {
        int minimumBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelFormat, encodingFormat);

        //minimumBufferSize = MathUtils.getUpperClosestMultiple(minimumBufferSize, chunkSizeInSamples * getBytesPerSample(encodingFormat));

        this.audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelFormat, encodingFormat,
                minimumBufferSize, AudioTrack.MODE_STREAM);
        this.samplesBuffer = new byte[minimumBufferSize];

    }

    public void writeSamples(byte[] samples) {
        audioTrack.write(samples, 0, samples.length);
    }

    public void writeSamples(byte[] samples, int i, int j) {
        audioTrack.write(samples, i, j);
    }

    public void play() {
        audioTrack.play();
    }

    public void stop() {
        audioTrack.stop();
    }

    public void playSamples() {
        writeSamples(samplesBuffer);

    }

    public int getMinBufferSize() {
        return AudioTrack.getMinBufferSize(audioTrack.getSampleRate(), audioTrack.getChannelConfiguration(),
                audioTrack.getAudioFormat());
    }

    /**
     * Returns the number of bytes per sample
     *
     * @returnm The number of bytes per sample
     */
    public int getBytesPerSample() {
        return getBytesPerSample(audioTrack.getAudioFormat());
    }

    /**
     * Returns the number of bytes per sample
     *
     * @param encodingFormat The encoding type
     * @return The number of bytes per sample
     */
    private static int getBytesPerSample(int encodingFormat) {
        switch (encodingFormat) {
            case AudioFormat.ENCODING_PCM_8BIT:
                return 1;
            case AudioFormat.ENCODING_PCM_16BIT:
                return 2;
            default:
                break;
        }

        return 0;
    }

    /**
     * @return The buffer length
     */
    public int getBufferLength() {
        return samplesBuffer.length;
    }
}
