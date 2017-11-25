package com.mycode.xml.pure2048;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Log.d("MyLog","gameMod="+gameMode);

        if(gameMode==1)
        {
            gameView=new GameView(this);
        }
        else
        {
            gameView=new GameView(this);
        }

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

    }

    public FrameLayout getViewLayout()
    {
        return viewLayout;
    }



    public DbHelper getDBHelp() {
        return dbHelper;
    }
    //public SQLiteDatabase getDB(){
    //    return db;
    //}

    private GameView gameView;
    //private GameViewNormal gameViewNormal;
    private Button btn1;
    public int gameMode;
    private DbHelper dbHelper;
    //private SQLiteDatabase db=null;
    private FrameLayout viewLayout;
}
