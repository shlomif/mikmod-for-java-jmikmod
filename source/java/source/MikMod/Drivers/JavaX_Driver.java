package MikMod.Drivers;

import java.io.*;
import MikMod.*;
import javax.sound.sampled.*;

/***************************************************************************
 * JavaX Driver - implemented in 2002 by Tobias Braun (tb@640k.de)         *
 * quite an ugly hack at the moment - sound buffer is alway initialized    *
 * with 44100HZ/16Bit/Stereo. But hey, it works for me ;)                  *
 ***************************************************************************/

public class JavaX_Driver extends clDRIVER {
	public final int RAWBUFFERSIZE = 16384;
  SourceDataLine line;

	byte RAW_DMABUF[]; //[RAWBUFFERSIZE];

	public JavaX_Driver(clMain theMain)
	//	: clDRIVER(theMain)
	{
		super(theMain);
		int i;

		Name = new String("JavaX-Driver");
		Version = new String("JavaX output driver v0.01 by Tobias Braun tb@640k.de");

		RAW_DMABUF = new byte[RAWBUFFERSIZE];
		for (i = 0; i < RAWBUFFERSIZE; i++)
			RAW_DMABUF[i] = 0;
	}

	public boolean IsPresent() {
		return true;
	}

	public int Init() {
    // User defined values should be set in the following line
    AudioFormat format = new AudioFormat(44100f, 16, 2, true, false);

    DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
    if (!AudioSystem.isLineSupported(info)) {
        System.out.println("Line not supported!");
        System.exit(-1);
    }

    // Obtain and open the line.
    try {
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format);
    } catch (LineUnavailableException ex) {
        ex.printStackTrace();
        System.exit(-1);
    }

		if (!m_.Virtch.VC_Init()) {
			line.close();
			line = null;
			return 0;
		}

			return 1;
	}

	public void Exit() {
			m_.Virtch.VC_Exit();
			line.close();
	}

	public void Update() {
			m_.Virtch.VC_WriteBytes(RAW_DMABUF, RAWBUFFERSIZE);
			//fwrite(RAW_DMABUF,RAWBUFFERSIZE,1,rawout);
			line.write(RAW_DMABUF, 0, RAWBUFFERSIZE);
	}

	public short SampleLoad(
		RandomAccessFile fp,
		int length,
		int reppos,
		int repend,
		int flags) {
		return m_.Virtch.VC_SampleLoad(fp, length, reppos, repend, flags);
	}

	public void SampleUnLoad(short handle) {
		m_.Virtch.VC_SampleUnload(handle);
	}

	public void PlayStart() {
		m_.Virtch.VC_PlayStart();
    line.start();
	}

	public void PlayStop() {
		m_.Virtch.VC_PlayStop();
    line.stop();
	}

	public void VoiceSetVolume(short voice, short vol) {
		m_.Virtch.VC_VoiceSetVolume(voice, vol);
	}

	public void VoiceSetFrequency(short voice, int frq) {
		m_.Virtch.VC_VoiceSetFrequency(voice, frq);
	}

	public void VoiceSetPanning(short voice, short pan) {
		m_.Virtch.VC_VoiceSetPanning(voice, pan);
	}

	public void VoicePlay(
		short voice,
		short handle,
		int start,
		int size,
		int reppos,
		int repend,
		int flags) {
		m_.Virtch.VC_VoicePlay(
			voice,
			handle,
			start,
			size,
			reppos,
			repend,
			flags);
	}

}