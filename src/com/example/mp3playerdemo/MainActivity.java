package com.example.mp3playerdemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import util.MusicList;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.R.bool;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {
	private TabHost host;
	private ImageButton ibtnPrev,ibtnPaly,ibtnNext;
	private ListView musicList;
	private TextView musicInfo;
	
	private MediaPlayer mediaPlayer;
	
	private int musicId=0;
	
	Timer timer;
	TimerTask task;
	
	ArrayList<String> musics;
	
	
	private SeekBar seekBar;
	//音乐列表的根目录
	public static final String MUSIC_HOME_PATH="/mnt/sdcard/";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//启动notification
		startNotification();
		
		musicInfo=(TextView)findViewById(R.id.txtMusicInfo);
		
		setTabWiget();
		setIamgeButton();
		
		musicList=(ListView)findViewById(android.R.id.list);
		
		musics=MusicList.getMusicList(MUSIC_HOME_PATH);
	if(musics!=null){
		String path=MUSIC_HOME_PATH+musics.get(musicId);
		mediaPlayer=new MediaPlayer();
		try {
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			musicInfo.setText(path);
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		ArrayAdapter<String> musicListAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, musics);
		musicList.setAdapter(musicListAdapter);
		
		//ListView的控制逻辑
		musicList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String musicName=musics.get(position);
				if(mediaPlayer!=null){
					mediaPlayer.release();
					mediaPlayer=null;
				}
				
				mediaPlayer=new MediaPlayer();
					try {
						mediaPlayer.setDataSource(MUSIC_HOME_PATH+musicName);
						mediaPlayer.prepare();
						mediaPlayer.start();
						musicInfo.setText(MUSIC_HOME_PATH+musicName);
						ibtnPaly.setImageResource(R.drawable.pause);
						updateSeekBar();
						
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
		
		//seekbar的监听
		seekBar=(SeekBar)findViewById(R.id.musicduration);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int musicDuration=mediaPlayer.getDuration();
				int curpoint=seekBar.getProgress();
				if(mediaPlayer.isPlaying()){
					mediaPlayer.seekTo(musicDuration*curpoint/100);
				}
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//播放按钮的监听
		ibtnPaly.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				if(mediaPlayer!=null){
					if(mediaPlayer.isPlaying()){
						mediaPlayer.pause();
						stopSeekBar();
						ibtnPaly.setImageResource(R.drawable.play);
					}else{
						mediaPlayer.start();
						updateSeekBar();
						ibtnPaly.setImageResource(R.drawable.pause);
					}
				}
				
			}
		});
		
		//next按钮的监听
		ibtnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopSeekBar();
				if(musicId==musics.size()-1){
					musicId=0;
				}else {
					musicId++;
				}
				
				if(mediaPlayer!=null){
					mediaPlayer.release();
					mediaPlayer=null;
				}
				mediaPlayer=new MediaPlayer();
				try {
					
					mediaPlayer.setDataSource(MUSIC_HOME_PATH+musics.get(musicId));
					mediaPlayer.prepare();
					mediaPlayer.start();
					ibtnPaly.setImageResource(R.drawable.pause);
					musicInfo.setText(MUSIC_HOME_PATH+musics.get(musicId));
					updateSeekBar();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		//prev按钮的监听
		ibtnPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopSeekBar();
				if(musicId==0){
					musicId=musics.size()-1;
				}else {
					musicId--;
				}
				
				if(mediaPlayer!=null){
					mediaPlayer.release();
					mediaPlayer=null;
				}
				mediaPlayer=new MediaPlayer();
				try {
					
					mediaPlayer.setDataSource(MUSIC_HOME_PATH+musics.get(musicId));
					mediaPlayer.prepare();
					mediaPlayer.start();
					ibtnPaly.setImageResource(R.drawable.pause);
					musicInfo.setText(MUSIC_HOME_PATH+musics.get(musicId));
					updateSeekBar();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//添加tabwiget
	public void setTabWiget(){
		host=this.getTabHost();
		TabWidget mTabWidget=host.getTabWidget();
		mTabWidget.getBackground().setAlpha(50);
		//设置选项卡1
		TabSpec tab1=host.newTabSpec("tab1");
		tab1.setIndicator("播放界面");
		tab1.setContent(R.id.linear1);
		
		//设置选项卡2
		TabSpec tab2=host.newTabSpec("tab2");
		tab2.setIndicator("音乐列表");
		tab2.setContent(R.id.linear2);
		
		//设置选项卡3
		TabSpec tab3=host.newTabSpec("tab3");
		tab3.setIndicator("在线音乐");
		tab3.setContent(R.id.linear3);
		
		//添加tabs
		host.addTab(tab1);
		host.addTab(tab2);
		host.addTab(tab3);
	}
	
	//设置imagebutton的背景为透明
	public void setIamgeButton(){
		//上一曲键
		ibtnPrev=(ImageButton)findViewById(R.id.ibtnPrev);
		ibtnPrev.getBackground().setAlpha(255);
		//下一曲键
		ibtnNext=(ImageButton)findViewById(R.id.ibtnNext);
		ibtnNext.getBackground().setAlpha(255);
		//播放键
		ibtnPaly=(ImageButton)findViewById(R.id.ibtnPlay);
		ibtnPaly.getBackground().setAlpha(255);
		
	}
	
	//更新seekbar
	public void updateSeekBar(){
		timer=new Timer();
		task=new TimerTask() {
			
			@Override
			public void run() {
				seekBar.setProgress(mediaPlayer.getCurrentPosition()*100/mediaPlayer.getDuration());
			}
		};
		timer.schedule(task, 0, 1000);
	}

	//停止seekbar
	public void stopSeekBar(){
			if(timer!=null){
				timer.cancel();
			}
	}
	
	//启动notification
	public void startNotification(){
		NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		int icon=R.drawable.music;
		String tickerText="音乐播放器启动";
		long when=System.currentTimeMillis();
		
		Notification notification=new Notification(icon, tickerText, when);
		notification.flags=Notification.FLAG_ONGOING_EVENT;
		
		CharSequence contentTitle="我的音乐播放器";
		CharSequence contentText="播放器正在播放音乐";
		
		Intent intent=new Intent();
		//设置点击notification时是返回activity而不是重新创建一个activity
//		intent.setAction(Intent.ACTION_MAIN);
//		intent.addCategory(Intent.CATEGORY_LAUNCHER);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.setClass(getApplicationContext(), MainActivity.class);
		
		PendingIntent pendingIntent=PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
		notification.setLatestEventInfo(MainActivity.this, contentTitle, contentText, pendingIntent);
		notificationManager.notify(1,notification);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		Intent intent=new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);			//返回主页面
		startActivity(intent);
	}
	
	
}
