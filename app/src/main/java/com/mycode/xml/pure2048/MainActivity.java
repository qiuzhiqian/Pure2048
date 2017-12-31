package com.mycode.xml.pure2048;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootPath = getApplicationContext().getExternalFilesDir("").getAbsolutePath();

        gameMode=1;

        //LinearLayout mainLayout= (LinearLayout) findViewById(R.id.main_view);
        viewLayout= (FrameLayout)findViewById(R.id.viewFrame);

        Intent intent = getIntent();

        String apppath=this.getFilesDir().getPath();
        Log.d("MyLog","appPath="+apppath);

        String extPath=getApplicationContext().getExternalFilesDir("").getAbsolutePath();
        dbHelper=new DbHelper(this,extPath+"/rank.db");
        //db = dbHelper.getWritableDatabase();

        gameMode = intent.getIntExtra("Mode",1);
        rcName = intent.getStringExtra("Recorder");
        if(rcName==null)
        {
            rcName = "";
        }
        //Log.d("MyLog","gameMod="+gameMode);

        if(gameMode==1)
        {
            gameView=new GameView(this);
        }
        else
        {
            gameView=new GameView(this);
        }
        gameView.setRcName(rcName);

        //FrameLayout fmy=new FrameLayout(getApplicationContext());
        FrameLayout.LayoutParams linelp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewLayout.addView(gameView,linelp);

        //gameView = (GameView) findViewById(R.id.gameview);
        btn1 = (Button)findViewById(R.id.btn_start);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.startGame();
            }
        });

        btn2 = (Button)findViewById(R.id.btn_redo);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.undo();
            }
        });

        btn3 = (Button)findViewById(R.id.btn_record);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date today = new Date();
                int year=today.getYear()+1900;
                int month=today.getMonth()+1;
                int day=today.getDate();
                int hour=today.getHours();
                int min=today.getMinutes();
                int sec=today.getSeconds();
                String sYear=null,sMon=null,sDay=null,sHour=null,sMin=null,sSec=null;
                if(month<10)    sMon="0"+month;
                else    sMon=""+month;
                if(day<10)  sDay="0"+day;
                else    sDay=""+day;
                if(hour<10) sHour="0"+hour;
                else    sHour=""+hour;
                if(min<10)  sMin="0"+min;
                else    sMin=""+min;
                if(sec<10)  sSec="0"+sec;
                else    sSec=""+sec;
                String saveName = "RC-"+year+sMon+sDay+sHour+sMin+sSec;
                Log.d("MyRecord",saveName);
                Toast.makeText(MainActivity.this,"Save "+saveName,Toast.LENGTH_SHORT).show();
                gameView.writeRecorder(rootPath+"/"+"recorders"+"/"+saveName);
            }
        });

    }

    public FrameLayout getViewLayout()
    {
        return viewLayout;
    }



    public DbHelper getDBHelp() {
        return dbHelper;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(gameView!=null)
        {
            gameView.saveScole();
        }
    }

    String rootPath=null;
    private GameView gameView;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    public int gameMode;
    private DbHelper dbHelper;
    private FrameLayout viewLayout;
    private String rcName=null;
}
