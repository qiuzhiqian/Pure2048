package com.mycode.xml.pure2048;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by xia_m on 2017/11/19/0019.
 */

public class MusicManager {
    private SoundPool mSoundPool=null;
    Context context=null;

    AssetManager am=null;
    //int soundId=0;

    HashMap<String, Integer> musicMap = new HashMap<String, Integer>();

    public MusicManager(Context context)
    {
        this.context=context;
        initSP();
    }

    public void initSP()
    {
        if (Build.VERSION.SDK_INT >= 21) {
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                            .build())
                    .build();
        } else {
            // This old constructor is deprecated, but we need it for
            // compatibility.
            mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        am = this.context.getAssets();
    }

    public void load(String sound) throws IOException {
        AssetFileDescriptor afd = am.openFd(sound);
        Integer soundId = mSoundPool.load(afd, 1);
        //sound.setSoundId(soundId);
        musicMap.put(sound,soundId);
    }

    public void play(String sound) {
        Integer soundId = musicMap.get(sound);
        // Id 设置成 Integer 类就是为了判断 null 较为方便
        if (soundId == null) {
            return;
        }
        // play 函数的第一个参数是 sound 的 soundId
        // 应该是由 load() 方法返回的 id
        // 第二个是左声道的音量
        // 第三个是右声道的音量
        // 第四个是priority（无效）
        // 第五个是是否循环
        // 第六个是播放速度
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void release() {
        mSoundPool.release();
    }
}
