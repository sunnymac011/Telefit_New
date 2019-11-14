package fit.tele.com.telefit.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fit.tele.com.telefit.R;
import fit.tele.com.telefit.adapter.RoutineExercisesAdapter;
import fit.tele.com.telefit.apiBase.FetchServiceBase;
import fit.tele.com.telefit.base.BaseActivity;
import fit.tele.com.telefit.databinding.ActivityPlayVideoBinding;
import fit.tele.com.telefit.modelBean.ExxDetial;
import fit.tele.com.telefit.modelBean.ModelBean;
import fit.tele.com.telefit.modelBean.RoutinePlanDetailsBean;
import fit.tele.com.telefit.modelBean.YogaExerciseDetailsBean;
import fit.tele.com.telefit.utils.CommonUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PlayVideoActivity extends BaseActivity implements TextToSpeech.OnInitListener {

    ActivityPlayVideoBinding binding;
    private SimpleExoPlayer player;

    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean isREStartVideo = false, shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
    private String plan_id="";
    private RoutineExercisesAdapter routineExercisesAdapter;
    private int intSets = 0, intReps = 0,intSecond = 0,intFixReps = 0, intFixSets=0,intVideos = 0, intPos = 0, intPlaySec = 0;
    private CountDownTimer mCountDownTimer, mCountDownTimerNew;
    private long timeCountInMilliSeconds = 1 * 60000;
    private ExxDetial exxDetial;
    private ArrayList<ExxDetial> exxDetials;
    private DecimalFormat formatter;
    private Vibrator v;
    private TextToSpeech textToSpeech;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_play_video;
    }

    @Override
    public void init() {
        binding = (ActivityPlayVideoBinding) getBindingObj();

        binding.imgSideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textToSpeech = new TextToSpeech(this,this);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (preferences.getScreenOnPref().equalsIgnoreCase("1"))
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        formatter = new DecimalFormat("00");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.rvExercises.setLayoutManager(linearLayoutManager);

        if (routineExercisesAdapter == null) {
            routineExercisesAdapter = new RoutineExercisesAdapter(context, new RoutineExercisesAdapter.ExerciseListener() {
                @Override
                public void onClick(int pos, ExxDetial routinePlanDetailsBean) {
                    intPos = pos;
                    if (mCountDownTimer != null)
                        mCountDownTimer.cancel();
                    mCountDownTimer = null;
                    if (mCountDownTimerNew != null)
                        mCountDownTimerNew.cancel();
                    mCountDownTimerNew = null;
                    binding.txtCounts.setVisibility(View.GONE);
                    exxDetial = routinePlanDetailsBean;
                    setData(exxDetial);
                }
            });

        }
        binding.rvExercises.setAdapter(routineExercisesAdapter);
        routineExercisesAdapter.clearAll();

        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "TeleFit"), (TransferListener<? super DataSource>) bandwidthMeter);

        if (getIntent().hasExtra("plan_id"))
            plan_id = getIntent().getStringExtra("plan_id");

        callRoutinePlanDetailsApi(plan_id);
    }

    private void setData(ExxDetial data) {
        if (data.getCatId() != null && !TextUtils.isEmpty(data.getCatId())) {
            if (data.getCatId().equalsIgnoreCase("2") || data.getCatId().equalsIgnoreCase("4")) {
                binding.txtSet.setVisibility(View.GONE);
                binding.txtReps.setVisibility(View.GONE);
                binding.txtExeWeight.setVisibility(View.GONE);
                binding.txtExeTime.setVisibility(View.VISIBLE);
            }
            else {
                binding.txtSet.setVisibility(View.VISIBLE);
                binding.txtReps.setVisibility(View.VISIBLE);
                binding.txtExeWeight.setVisibility(View.VISIBLE);
                binding.txtExeTime.setVisibility(View.GONE);

                intSets = 1;
                if (data.getSetsReps().get(0).getSets() != null && !TextUtils.isEmpty(data.getSetsReps().get(0).getSets()))
                    intFixSets = Integer.parseInt(data.getSetsReps().get(0).getSets());
                if (data.getSetsReps().get(0).getReps() != null && !TextUtils.isEmpty(data.getSetsReps().get(0).getReps()))
                    intReps = Integer.parseInt(data.getSetsReps().get(0).getReps());
                if (data.getSetsReps().get(0).getReps() != null && !TextUtils.isEmpty(data.getSetsReps().get(0).getReps()))
                    intFixReps = Integer.parseInt(data.getSetsReps().get(0).getReps());
                if (data.getSetsReps().get(0).getSrHours() != null && !TextUtils.isEmpty(data.getSetsReps().get(0).getSrHours()) &&
                        data.getSetsReps().get(0).getSrMin() != null && !TextUtils.isEmpty(data.getSetsReps().get(0).getSrMin()) &&
                        data.getSetsReps().get(0).getSrSec() != null && !TextUtils.isEmpty(data.getSetsReps().get(0).getSrSec()))
                {
                    int intH = Integer.parseInt(data.getSetsReps().get(0).getSrHours());
                    int intM = Integer.parseInt(data.getSetsReps().get(0).getSrMin());
                    int intS = Integer.parseInt(data.getSetsReps().get(0).getSrSec());
                    intSecond = ((intH * 60)*60) + (intM * 60) + intS;
                }
                if (data.getSetsReps().get(0).getSets() != null && !TextUtils.isEmpty(data.getSetsReps().get(0).getSets()))
                    binding.txtSet.setText("Set "+intSets);
                else
                    binding.txtSet.setVisibility(View.GONE);
                if (data.getSetsReps().get(0).getReps() != null && !TextUtils.isEmpty(data.getSetsReps().get(0).getReps()))
                    binding.txtReps.setText("Rep "+intReps);
                else
                    binding.txtReps.setVisibility(View.GONE);
                if (data.getSetsReps().get(0).getWeight() != null && !TextUtils.isEmpty(data.getSetsReps().get(0).getWeight()))
                    binding.txtExeWeight.setText(data.getSetsReps().get(0).getWeight()+""+data.getSetsReps().get(0).getWeightType());
            }
        }

        if (data.getExes().getExeTitle() != null && !TextUtils.isEmpty(data.getExes().getExeTitle()))
            binding.txtCurrentExercise.setText(data.getExes().getExeTitle());
        if (data.getExes().getVideoURLBeans().get(0).getVidUrl() != null && !TextUtils.isEmpty(data.getExes().getVideoURLBeans().get(0).getVidUrl()))
        {
            if (data.getExes().getVideoURLBeans().size() > 1)
                initializePlayer(data.getExes().getVideoURLBeans().get(0).getVidUrl(), false, true,false);
            else
                initializePlayer(data.getExes().getVideoURLBeans().get(0).getVidUrl(), true, true,false);
            intVideos = 1;

//            if (data.getExes().getCatId().equalsIgnoreCase("2"))
//            {
//                if (data.getExes().getVideoURLBeans().size() > 1)
//                    initializePlayer(data.getExes().getVideoURLBeans().get(0).getVidUrl(), false, true,false);
//                else
//                    initializePlayer(data.getExes().getVideoURLBeans().get(0).getVidUrl(), true, true,false);
//                intVideos = 1;
//            }
//            else if (data.getExes().getCatId().equalsIgnoreCase("4")) {
//                initializePlayer(data.getExes().getVideoURLBeans().get(0).getVidUrl(), false, true,false);
//            }
//            else{
//                initializePlayer(data.getExes().getVideoURLBeans().get(0).getVidUrl(), false, true,false);
//            }
        }

        if (isREStartVideo)
        {
            if (data.getExes().getCatId().equalsIgnoreCase("2") || data.getExes().getCatId().equalsIgnoreCase("4"))
                startTimer();
            else
                startGHTimer();
        }

        binding.imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying()) {
                    if (player != null) {
                        player.setPlayWhenReady(false);
                        binding.imgPlay.setBackgroundResource(R.drawable.play_video_button);
                        if (mCountDownTimer != null)
                            mCountDownTimer.cancel();
                    }
                }
                else {
                    if (player != null) {
                        player.setPlayWhenReady(true);
                        binding.imgPlay.setBackgroundResource(R.drawable.pause_video_button);
                        if (data.getExes().getCatId().equalsIgnoreCase("2") || data.getExes().getCatId().equalsIgnoreCase("4"))
                            startTimer();
                        else
                            startGHTimer();
                    }
                }
            }
        });
    }

    private void startGHTimer() {
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
        mCountDownTimer = null;
        int playSecond = 5 * intReps;
        timeCountInMilliSeconds = playSecond * 1000;
        mCountDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);

                intPlaySec = intPlaySec+1;
                if(intPlaySec%5==0){
                    intReps = intReps-1;
                    startCountAnimation("Rep "+intReps);
                    binding.txtReps.setText("Rep "+intReps);
                    if (preferences.getSoundPref().equalsIgnoreCase("1"))
                        textToSpeech.speak(""+intReps, TextToSpeech.QUEUE_FLUSH, null);

                    if (preferences.getVibratePref().equalsIgnoreCase("1")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(400, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(400);
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                intPlaySec = 0;
                player.setPlayWhenReady(false);
                intReps = 0;
                if (preferences.getSoundPref().equalsIgnoreCase("1"))
                    textToSpeech.speak("Rest time", TextToSpeech.QUEUE_FLUSH, null);
                if (preferences.getVibratePref().equalsIgnoreCase("1")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(700);
                    }
                }
                if (intReps < 1)
                {
                    intSets = intSets+1;

                    if (intSets > exxDetial.getSetsReps().size())
                    {
                        if (intSecond != 0) {
                            timeCountInMilliSeconds = intSecond * 1000;
                            mCountDownTimerNew = new CountDownTimer(timeCountInMilliSeconds, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    binding.txtCounts.setVisibility(View.VISIBLE);
                                    binding.imgPlay.setEnabled(false);
                                    long hours = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) / 3600;
                                    long minutes = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 3600) / 60;
                                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                                    binding.txtCounts.setText(formatter.format(hours)+":"+formatter.format(minutes)+":"+formatter.format(seconds));
                                    if (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) == 7 && preferences.getSoundPref().equalsIgnoreCase("1"))
                                        textToSpeech.speak("Get ready", TextToSpeech.QUEUE_FLUSH, null);
                                    if (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) <= 5 && preferences.getSoundPref().equalsIgnoreCase("1"))
                                        textToSpeech.speak(""+TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished), TextToSpeech.QUEUE_FLUSH, null);
                                }

                                @Override
                                public void onFinish() {
                                    binding.imgPlay.setEnabled(true);
                                    binding.txtCounts.setVisibility(View.GONE);
                                    intSets = 1;
                                    player.stop();
                                    intPos = intPos+1;
                                    if (intPos >= exxDetials.size()){
                                        binding.imgPlay.setBackgroundResource(R.drawable.play_video_button);
                                        isREStartVideo = false;
                                        intPos = 0;
                                        exxDetial = exxDetials.get(intPos);
                                        routineExercisesAdapter.changeSelection(intPos);
                                        mCountDownTimer.cancel();
                                        mCountDownTimer = null;
                                        setData(exxDetial);
                                    } else {
                                        mCountDownTimer.cancel();
                                        mCountDownTimer = null;
                                        binding.imgPlay.setBackgroundResource(R.drawable.pause_video_button);
                                        isREStartVideo = true;
                                        exxDetial = exxDetials.get(intPos);
                                        routineExercisesAdapter.changeSelection(intPos);
                                        setData(exxDetial);
                                    }
                                }

                            }.start();
                        }
                        return;
                    }
                    else {
                        if (intSecond != 0) {
                            timeCountInMilliSeconds = intSecond * 1000;
                            mCountDownTimerNew = new CountDownTimer(timeCountInMilliSeconds, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    binding.imgPlay.setEnabled(false);
                                    binding.txtCounts.setVisibility(View.VISIBLE);
                                    long hours = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) / 3600;
                                    long minutes = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 3600) / 60;
                                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                                    binding.txtCounts.setText(formatter.format(hours)+":"+formatter.format(minutes)+":"+formatter.format(seconds));
                                    if (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) == 7 && preferences.getSoundPref().equalsIgnoreCase("1"))
                                        textToSpeech.speak("Get ready", TextToSpeech.QUEUE_FLUSH, null);
                                    if (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) <= 5 && preferences.getSoundPref().equalsIgnoreCase("1"))
                                        textToSpeech.speak(""+TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished), TextToSpeech.QUEUE_FLUSH, null);
                                }

                                @Override
                                public void onFinish() {
                                    binding.imgPlay.setEnabled(true);
                                    if (preferences.getSoundPref().equalsIgnoreCase("1"))
                                        textToSpeech.speak(""+(intSets-1), TextToSpeech.QUEUE_FLUSH, null);
                                    binding.txtCounts.setVisibility(View.GONE);
                                    player.setPlayWhenReady(true);
                                    startGHTimer();
                                }

                            }.start();
                        }
                    }
                    int intH = Integer.parseInt(exxDetial.getSetsReps().get(0).getSrHours());
                    int intM = Integer.parseInt(exxDetial.getSetsReps().get(0).getSrMin());
                    int intS = Integer.parseInt(exxDetial.getSetsReps().get(0).getSrSec());
                    intSecond = ((intH * 60)*60) + (intM * 60) + intS;
                    intReps = Integer.parseInt(exxDetial.getSetsReps().get(intSets-1).getReps());
                    binding.txtReps.setText("Rep "+intReps);
                    binding.txtSet.setText("Set "+intSets);
                    if (exxDetial.getSetsReps().get(intSets-1).getWeight() != null && !TextUtils.isEmpty(exxDetial.getSetsReps().get(intSets-1).getWeight()))
                        binding.txtExeWeight.setText(exxDetial.getSetsReps().get(intSets-1).getWeight()+""+exxDetial.getSetsReps().get(intSets-1).getWeightType());
                }
            }
        }.start();
    }

    private void startTimer() {
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
        mCountDownTimer = null;
        if (exxDetial.getExeHours() != null && !TextUtils.isEmpty(exxDetial.getExeHours()) &&
                exxDetial.getExeMin() != null && !TextUtils.isEmpty(exxDetial.getExeMin()) &&
                exxDetial.getExeSec() != null && !TextUtils.isEmpty(exxDetial.getExeSec()))
        {
            int intH = Integer.parseInt(exxDetial.getExeHours());
            int intM = Integer.parseInt(exxDetial.getExeMin());
            int intS = Integer.parseInt(exxDetial.getExeSec());
            intSecond = ((intH * 60)*60) + (intM * 60) + intS;
            binding.txtExeTime.setText(intH+":"+intM+":"+intS);

            timeCountInMilliSeconds = intSecond * 1000;
            mCountDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long hours = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) / 3600;
                    long minutes = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 3600) / 60;
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
                    binding.txtExeTime.setText(formatter.format(hours)+":"+formatter.format(minutes)+":"+formatter.format(seconds));
                    if (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) >= 5 && preferences.getSoundPref().equalsIgnoreCase("1"))
                        textToSpeech.speak(""+TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished), TextToSpeech.QUEUE_FLUSH, null);
                }

                @Override
                public void onFinish() {
                    if (player != null) {
                        player.seekTo(0);
                        player.stop();
                        intPos = intPos+1;
                        if (preferences.getSoundPref().equalsIgnoreCase("1"))
                            textToSpeech.speak("Next workout", TextToSpeech.QUEUE_FLUSH, null);
                        if (preferences.getVibratePref().equalsIgnoreCase("1")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                //deprecated in API 26
                                v.vibrate(700);
                            }
                        }
                        if (intPos >= exxDetials.size()){
                            isREStartVideo = false;
                            intPos = 0;
                            exxDetial = exxDetials.get(intPos);
                            routineExercisesAdapter.changeSelection(intPos);
                            binding.imgPlay.setBackgroundResource(R.drawable.play_video_button);
                            mCountDownTimer.cancel();
                            mCountDownTimer = null;
                            setData(exxDetial);
                        } else {
                            binding.imgPlay.setBackgroundResource(R.drawable.pause_video_button);
                            isREStartVideo = true;
                            exxDetial = exxDetials.get(intPos);
                            routineExercisesAdapter.changeSelection(intPos);
                            setData(exxDetial);
                        }
                    }
                }
            }.start();
        }
    }

    public boolean isPlaying(){
        if (player != null) {
            if(player.getPlaybackState() == Player.STATE_READY && player.getPlayWhenReady()){
                return true;
            }
            else return false;
        }
        else return false;
    }

    private void initializePlayer(String videoURL, boolean isLooping, boolean isMulVideo, boolean isStartVideo) {

        binding.epvExercise.requestFocus();
        binding.epvExercise.setUseController(false);

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        player.setVolume(0f);
        binding.epvExercise.setPlayer(player);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL),
                mediaDataSourceFactory, extractorsFactory, null, null);

        if (isLooping) {
            LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource);
            player.prepare(loopingSource);
        }
        else
            player.prepare(mediaSource);

        if (isStartVideo || isREStartVideo)
            player.setPlayWhenReady(true);

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == ExoPlayer.STATE_BUFFERING){
                    binding.progressBarVideo.setVisibility(View.VISIBLE);
                } else {
                    binding.progressBarVideo.setVisibility(View.INVISIBLE);
                }

                if (isMulVideo) {
                    if (playbackState == ExoPlayer.STATE_ENDED) {
                        if (exxDetial != null && exxDetial.getExes() != null && exxDetial.getExes().getVideoURLBeans() != null)
                        {
                            intVideos = intVideos+1;
                            if (intVideos <= exxDetial.getExes().getVideoURLBeans().size())
                            {
                                initializePlayer(exxDetial.getExes().getVideoURLBeans().get(intVideos-1).getVidUrl(),false, true,true);
                            }
                        }
                    }
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });

    }

    private void startCountAnimation(String strCount) {
        binding.txtCounts.setText(strCount);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        anim.setStartOffset(50);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(1);
        binding.txtCounts.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.txtCounts.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.txtCounts.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void stopTimer()
    {
        mCountDownTimer.cancel();
    }

    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d",
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;


    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if ((Util.SDK_INT <= 23 || player == null)) {
//            initializePlayer();
//        }
    }

    private void callRoutinePlanDetailsApi(String id) {
        if (CommonUtils.isInternetOn(context)) {
            binding.progress.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("routine_p_id", id);

            Observable<ModelBean<ArrayList<RoutinePlanDetailsBean>>> signupusers = FetchServiceBase.getFetcherServiceWithToken(context).getRoutinePlanDetailsAPI(map);
            subscription = signupusers.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ModelBean<ArrayList<RoutinePlanDetailsBean>>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.toast(context, e.getMessage());
                            Log.e("callRoutinePlanDetails"," "+e);
                            binding.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onNext(ModelBean<ArrayList<RoutinePlanDetailsBean>> apiExercisesBean) {
                            binding.progress.setVisibility(View.GONE);
                            if (apiExercisesBean.getStatus().toString().equalsIgnoreCase("1"))
                            {
                                exxDetials = apiExercisesBean.getResult().get(0).getExxDetial();
                                routineExercisesAdapter.addAllList(exxDetials);
                                if (exxDetials.size() > 0)
                                {
                                    intPos = 0;
                                    exxDetial = exxDetials.get(0);
                                    setData(exxDetial);
                                }

                            }
                            else
                                CommonUtils.toast(context, ""+apiExercisesBean.getMessage());
                        }
                    });

        } else {
            CommonUtils.toast(context, context.getString(R.string.snack_bar_no_internet));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","onDestroy");
        releasePlayer();
        if (mCountDownTimer != null)
        {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        if (mCountDownTimerNew != null)
        {
            mCountDownTimerNew.cancel();
            mCountDownTimerNew = null;
        }
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (preferences.getSoundPref().equalsIgnoreCase("0")) {
            releasePlayer();
            if (mCountDownTimer != null)
            {
                mCountDownTimer.cancel();
                mCountDownTimer = null;
            }
            if (mCountDownTimerNew != null)
            {
                mCountDownTimerNew.cancel();
                mCountDownTimerNew = null;
            }
            if (textToSpeech != null) {
                textToSpeech.stop();
                textToSpeech.shutdown();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        releasePlayer();
        if (mCountDownTimer != null)
        {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        if (mCountDownTimerNew != null)
        {
            mCountDownTimerNew.cancel();
            mCountDownTimerNew = null;
        }
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }

    }
}
