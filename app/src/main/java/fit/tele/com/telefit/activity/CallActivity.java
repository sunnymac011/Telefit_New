package fit.tele.com.telefit.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityCallBinding;
import fit.tele.com.telefit.utils.MyApplication;
import io.agora.AgoraAPI;
import io.agora.AgoraAPIOnlySignal;
import io.agora.IAgoraAPI;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class CallActivity extends BaseActivity implements MyApplication.OnAgoraEngineInterface {
    private final String TAG = CallActivity.class.getSimpleName();

    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1;
    private static final int PERMISSION_REQ_ID_STORAGE = PERMISSION_REQ_ID_CAMERA + 1;

    private AgoraAPIOnlySignal mAgoraAPI;
    private RtcEngine mRtcEngine;

    private String mSubscriber;

    private String channelName = "channelid";
    private MediaPlayer mPlayer;
    private int callType = -1;
    private boolean mIsCallInRefuse = false;
    private int mRemoteUid = 0;
    ActivityCallBinding binding;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_call;
    }

    @Override
    public void init() {
        binding = (ActivityCallBinding) getBindingObj();

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)
                && checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA)
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQ_ID_STORAGE)) {
            initAgoraEngineAndJoinChannel();
        }
    }

    private void setupData() {
        Intent intent = getIntent();

        mSubscriber = intent.getStringExtra("subscriber");
        channelName = intent.getStringExtra("channelName");
        callType = intent.getIntExtra("type", -1);
        if (callType == Constant.CALL_IN) {
            mIsCallInRefuse = true;
            binding.callLayoutCallin.setVisibility(View.VISIBLE);
            binding.callButtonHangup.setVisibility(View.GONE);
            binding.meetTitle.setText(String.format(Locale.US, "%s is calling...", mSubscriber));

            try {
                mPlayer = MediaPlayer.create(this, R.raw.basic_ring);
                mPlayer.setLooping(true);
                mPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            setupLocalVideo(); // Tutorial Step 3
        } else if (callType == Constant.CALL_OUT) {
            binding.callLayoutCallin.setVisibility(View.GONE);
            binding.callButtonHangup.setVisibility(View.VISIBLE);
            binding.meetTitle.setText(String.format(Locale.US, "%s is be called...", mSubscriber));

            try {
                mPlayer = MediaPlayer.create(this, R.raw.basic_tones);
                mPlayer.setLooping(true);
                mPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            setupLocalVideo(); // Tutorial Step 3
            joinChannel(); // Tutorial Step 4
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "onNewIntent");
        setupData();
    }

    @Override
    public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) { // Tutorial Step 5
        Log.i(TAG, "onFirstRemoteVideoDecoded  uid:" + uid);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRemoteUid != 0) {
                    return;
                }
                mRemoteUid = uid;
                setupRemoteVideo(uid);
            }
        });
    }

    @Override
    public void onUserOffline(final int uid, int reason) { // Tutorial Step 7
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onRemoteUserLeft(uid);
            }
        });
    }

    @Override
    public void onUserMuteVideo(final int uid, final boolean muted) { // Tutorial Step 10
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onRemoteUserVideoMuted(uid, muted);
            }
        });
    }

    private CompoundButton.OnCheckedChangeListener oncheckChangeListerener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mRtcEngine.muteLocalAudioStream(isChecked);
        }
    };

    public void CallClickInit(View v) {
        switch (v.getId()) {

            case R.id.call_in_hangup:
                callInRefuse();
                break;

            case R.id.call_in_pickup:
                mIsCallInRefuse = false;
                joinChannel(); // Tutorial Step 4
                mAgoraAPI.channelInviteAccept(channelName, mSubscriber, 0, null);
                binding.callLayoutCallin.setVisibility(View.GONE);
                binding.callButtonHangup.setVisibility(View.VISIBLE);
                binding.meetTitle.setVisibility(View.GONE);
                startCountDown();
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.stop();
                }
                break;

            case R.id.call_button_hangup: // call out canceled or call ended

                callOutHangup();
                break;
        }
    }

    private void callOutHangup() {
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
        if (mAgoraAPI != null)
            mAgoraAPI.channelInviteEnd(channelName, mSubscriber, 0);
    }

    private void callInRefuse() {
        // "status": 0 // Default
        // "status": 1 // Busy
        if (mAgoraAPI != null)
            mAgoraAPI.channelInviteRefuse(channelName, mSubscriber, 0, "{\"status\":0}");

        onEncCallClicked();
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        Log.i(TAG, "onJoinChannelSuccess channel: " + channel + " uid: " + uid);
    }

    private void addSignalingCallback() {
        if (mAgoraAPI == null) {
            return;
        }

        mAgoraAPI.callbackSet(new AgoraAPI.CallBack() {

            @Override
            public void onLogout(final int i) {
                Log.i(TAG, "onLogout  i = " + i);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (i == IAgoraAPI.ECODE_LOGOUT_E_KICKED) { // other login the account
                            Toast.makeText(CallActivity.this, "Other login account ,you are logout.", Toast.LENGTH_SHORT).show();

                        } else if (i == IAgoraAPI.ECODE_LOGOUT_E_NET) { // net
                            Toast.makeText(CallActivity.this, "Logout for Network can not be.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        Intent intent = new Intent();
                        intent.putExtra("result", "finish");
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });

            }

            /**
             * call in receiver
             */
            @Override
            public void onInviteReceived(final String channelID, final String account, final int uid, String s2) {
                Log.i(TAG, "onInviteReceived  channelID = " + channelID + "  account = " + account);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                      "status": 0 // Default
//                      "status": 1 // Busy
                        mAgoraAPI.channelInviteRefuse(channelID, account, uid, "{\"status\":1}");

                    }
                });
            }

            /**
             * call out other ,local receiver
             */
            @Override
            public void onInviteReceivedByPeer(final String channelID, String account, int uid) {
                Log.i(TAG, "onInviteReceivedByPeer  channelID = " + channelID + "  account = " + account);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.callButtonHangup.setVisibility(View.VISIBLE);

                        binding.meetTitle.setText(String.format(Locale.US, "%s is being called ...", mSubscriber));
                    }
                });
            }

            /**
             * other receiver call accept callback
             * @param channelID
             * @param account
             * @param uid
             * @param s2
             */
            @Override
            public void onInviteAcceptedByPeer(String channelID, String account, int uid, String s2) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startCountDown();
                        if (mPlayer != null && mPlayer.isPlaying()) {
                            mPlayer.stop();
                        }
                        binding.meetTitle.setVisibility(View.GONE);
                    }
                });

            }

            /**
             * other receiver call refuse callback
             * @param channelID
             * @param account
             * @param uid
             * @param s2
             */

            @Override
            public void onInviteRefusedByPeer(String channelID, final String account, int uid, final String s2) {
                Log.i(TAG, "onInviteRefusedByPeer channelID = " + channelID + " account = " + account + " s2 = " + s2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mPlayer != null && mPlayer.isPlaying()) {
                            mPlayer.stop();
                        }
                        if (s2.contains("status") && s2.contains("1")) {
                            Toast.makeText(CallActivity.this, account + " reject your call for busy", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CallActivity.this, account + " reject your call", Toast.LENGTH_SHORT).show();
                        }

                        onEncCallClicked();
                    }
                });
            }


            /**
             * end call remote receiver callback
             * @param channelID
             * @param account
             * @param uid
             * @param s2
             */
            @Override
            public void onInviteEndByPeer(final String channelID, String account, int uid, String s2) {
                Log.i(TAG, "onInviteEndByPeer channelID = " + channelID + " account = " + account);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (channelID.equals(channelName)) {
                            onEncCallClicked();
                        }

                    }
                });
            }

            /**
             * end call local receiver callback
             * @param channelID
             * @param account
             * @param uid
             */
            @Override
            public void onInviteEndByMyself(String channelID, String account, int uid) {
                Log.i(TAG, "onInviteEndByMyself channelID = " + channelID + "  account = " + account);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onEncCallClicked();
                    }
                });
            }
        });
    }

    public final void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        addSignalingCallback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy");
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }

        if (mCountDownTimer != null)
            mCountDownTimer.cancel();

        if (mRtcEngine != null) {
            mRtcEngine.stopPreview();
            mRtcEngine.leaveChannel();
        }
        mRtcEngine = null;

    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed callType: " + callType + " mIsCallInRefuse: " + mIsCallInRefuse);
        if (callType == Constant.CALL_IN && mIsCallInRefuse) {
            callInRefuse();
        } else {
            callOutHangup();
        }
        super.onBackPressed();
    }

    // Tutorial Step 8
    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
    }

    // Tutorial Step 6
    public void onEncCallClicked() {
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
        finish();
    }

    // Tutorial Step 1
    private void initializeAgoraEngine() {
        mAgoraAPI = MyApplication.the().getmAgoraAPI();
        mRtcEngine = MyApplication.the().getmRtcEngine();
        Log.i(TAG, "initializeAgoraEngine mRtcEngine :" + mRtcEngine);
        if (mRtcEngine != null) {
            mRtcEngine.setLogFile("/sdcard/sdklog.txt");
        }
        setupVideoProfile();

    }

    // Tutorial Step 2
    private void setupVideoProfile() {
        mRtcEngine.enableVideo();
//      mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false); // Earlier than 2.3.0

        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_640x360, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    // Tutorial Step 3
    private void setupLocalVideo() {
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        binding.remoteVideoViewContainer.addView(surfaceView);
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        binding.localVideoViewContainer.setVisibility(View.VISIBLE);
        int ret = mRtcEngine.startPreview();
        Log.i(TAG, "setupLocalVideo startPreview enter << ret :" + ret);
    }

    // Tutorial Step 4
    private void joinChannel() {
        int ret = mRtcEngine.joinChannel(null, channelName, "Extra Optional Data", 0); // if you do not specify the uid, we will generate the uid for you
        Log.i(TAG, "joinChannel enter ret :" + ret);
    }

    // Tutorial Step 5
    private void setupRemoteVideo(int uid) {
        Log.i(TAG, "setupRemoteVideo uid: " + uid + " " + binding.remoteVideoViewContainer.getChildCount());
        if (binding.remoteVideoViewContainer.getChildCount() >= 1) {
            binding.remoteVideoViewContainer.removeAllViews();
        }

        SurfaceView surfaceViewSmall = RtcEngine.CreateRendererView(getBaseContext());
        surfaceViewSmall.setZOrderMediaOverlay(true);
        binding.localVideoViewContainer.addView(surfaceViewSmall);
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceViewSmall, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        binding.localVideoViewContainer.setVisibility(View.VISIBLE);

        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        binding.remoteVideoViewContainer.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        binding.remoteVideoViewContainer.setVisibility(View.VISIBLE);
    }


    // Tutorial Step 7
    private void onRemoteUserLeft(int uid) {
        if (uid == mRemoteUid) {
            finish();
        }
    }

    // Tutorial Step 10
    private void onRemoteUserVideoMuted(int uid, boolean muted) {
        FrameLayout container = (FrameLayout) findViewById(R.id.remote_video_view_container);

        SurfaceView surfaceView = (SurfaceView) container.getChildAt(0);

        Object tag = surfaceView.getTag();
        if (tag != null && (Integer) tag == uid) {
            surfaceView.setVisibility(muted ? View.GONE : View.VISIBLE);
        }
    }


    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.i(TAG, "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode);

        switch (requestCode) {
            case PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA, PERMISSION_REQ_ID_CAMERA);
                } else {
                    showLongToast("No permission for " + Manifest.permission.RECORD_AUDIO);
                    onEncCallClicked();
                }
                break;
            }
            case PERMISSION_REQ_ID_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQ_ID_STORAGE);
                } else {
                    showLongToast("No permission for " + Manifest.permission.CAMERA);
                    onEncCallClicked();
                }
                break;
            }
            case PERMISSION_REQ_ID_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initAgoraEngineAndJoinChannel();
                } else {
                    showLongToast("No permission for " + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    onEncCallClicked();
                }
                break;
            }
        }
    }

    private void initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine();
        MyApplication.the().setOnAgoraEngineInterface(this);
        setupData();
    }

    private CountDownTimer mCountDownTimer;
    private long timeCountInMilliSeconds = 180 * 1000;
    private void startCountDown() {
        mCountDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.txtTimer.setText(""+(int) (millisUntilFinished / 500));
            }

            @Override
            public void onFinish() {
                binding.txtTimer.setText("00:00");
            }

        }.start();
        mCountDownTimer.start();
    }

}
