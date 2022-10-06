

package com.goldze.mvvmhabit.aioui.relax.music.play.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;


import com.goldze.mvvmhabit.aioui.relax.music.play.PlayerManager;

import java.util.Objects;

public class PlayerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("fengao_xiaomi", "onReceive getAction: "+intent.getAction());
        if (Objects.equals(intent.getAction(), Intent.ACTION_MEDIA_BUTTON)) {
            if (intent.getExtras() == null) {
                return;
            }
            KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
            if (keyEvent == null) {
                return;
            }
            if (keyEvent.getAction() != KeyEvent.ACTION_DOWN) {
                return;
            }
            Log.i("fengao_xiaomi", "onReceive: keyEvent: "+keyEvent.getKeyCode());
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    PlayerManager.getInstance().togglePlay();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    PlayerManager.getInstance().playAudio();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    PlayerManager.getInstance().pauseAudio();
                    break;
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    PlayerManager.getInstance().clear();
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    PlayerManager.getInstance().playNext();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    PlayerManager.getInstance().playPrevious();
                    break;
                default:
            }

        } else {

            if (Objects.requireNonNull(intent.getAction()).equals(PlayerService.NOTIFY_PLAY)) {
                PlayerManager.getInstance().playAudio();
            } else if (intent.getAction().equals(PlayerService.NOTIFY_PAUSE)
                || intent.getAction().equals(android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
                PlayerManager.getInstance().pauseAudio();
            } else if (intent.getAction().equals(PlayerService.NOTIFY_NEXT)) {
                PlayerManager.getInstance().playNext();
            } else if (intent.getAction().equals(PlayerService.NOTIFY_CLOSE)) {
                PlayerManager.getInstance().clear();
            } else if (intent.getAction().equals(PlayerService.NOTIFY_PREVIOUS)) {
                PlayerManager.getInstance().playPrevious();
            }
        }
    }
}
