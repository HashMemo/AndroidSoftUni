package com.example.asus.spotifydesign;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class MainActivity extends Activity implements IRecycleViewSelectedElement, View.OnClickListener {

    Context mCtx;
    int br=0;
    MusicService mService;
    boolean mBound = false;
    boolean mPause = false;
    private Button btnPlayPause, btnReverse, btnForward;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> data;
    private TextView txtViews;
    private TextView txtSongToShow;
    String path;

    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new ArrayList<String>();
        int vr=0;


        path = "/storage/extSdCard/Music";
        File f = new File(path);
        File[] file = f.listFiles();
        Log.d("Files", file.toString());
        btnPlayPause = (Button)findViewById(R.id.btnPlay);
        btnPlayPause.setOnClickListener(this);
        btnForward = (Button)findViewById(R.id.btnForward);
        btnReverse = (Button)findViewById(R.id.btnReverse);
        btnForward.setOnClickListener(this);
        btnReverse.setOnClickListener(this);

        txtViews = (TextView)findViewById(R.id.textView2);
        txtSongToShow = (TextView)findViewById(R.id.txtPlayingSongName);

        for (int index = 0; index < file.length; index++) {
            data.add(file[index].getName());

        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecycleViewAdapter(data, this);

        mRecyclerView.setAdapter(mAdapter);

        mIth.attachToRecyclerView(mRecyclerView);

        mCtx = this;

        RecycleViewCustomDecoration itemDecoration = new RecycleViewCustomDecoration(this);
        mRecyclerView.addItemDecoration(itemDecoration);


    }


    ItemTouchHelper mIth = new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                    ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    int elementPosition = viewHolder.getAdapterPosition();

                    data.remove(elementPosition);

                    mAdapter.notifyItemRemoved(elementPosition);
                }


            });

    @Override
    public void onItemSelected(int position) {

        Toast.makeText(this, String.valueOf(position), Toast.LENGTH_LONG).show();
        if(br==1){
            br=0;
            mService.stopMusicForNextOne();
        }
        if(br==0) {
            br++;
            Intent intent = new Intent(MainActivity.this, MusicService.class);
            intent.putExtra("songToPlay", path + "/" + data.get(position));
            startService(intent);
            txtSongToShow.setText(data.get(position));
            int img = R.drawable.ic_pause_image;
            btnPlayPause.setCompoundDrawablesWithIntrinsicBounds(img, 0, 0, 0);
            Log.d("Music To Play", path + "/" + data.get(position));
            Log.d("Song: ", data.get(position));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnPlay){
            if(mBound){
                if(mService.isPlaying()==false){
                    //mPause = false;
                    mService.onPlay();
                    int img = R.drawable.ic_pause_image;
                    btnPlayPause.setCompoundDrawablesWithIntrinsicBounds(img, 0, 0, 0);
                }
                else if(mService.isPlaying()) {
                    //mPause = true;
                    mService.onPause();
                    int img = R.drawable.ic_action_name;
                    btnPlayPause.setCompoundDrawablesWithIntrinsicBounds(img, 0, 0, 0);
                }

            }
        }
        if(view.getId() == R.id.btnReverse){
            mService.Reverse();
        }
        if(view.getId() == R.id.btnForward){
            mService.Forward();
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, MusicService.class);
        stopService(intent);
        super.onDestroy();
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
