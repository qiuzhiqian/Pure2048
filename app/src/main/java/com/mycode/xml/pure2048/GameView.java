package com.mycode.xml.pure2048;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by xia_m on 2017/10/29/0029.
 */

public class GameView extends LinearLayout {
    public GameView(Context context) {
        super(context);

        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initGameView();
    }

    private void initCardsMap(MapRecoder cardnum)
    {
        for(int y=0;y<Config.Cnt;y++)
        {
            for(int x=0;x<Config.Cnt;x++)
            {
                cardsMap[y][x].setNum(cardnum.cardmap[y][x],Config.show_style);
                Log.d("MyCard","("+y+","+x+")="+cardnum.cardmap[y][x]);
            }
        }
    }

    private void PopCardsMap()      //出栈---对应撤销
    {
        MapRecoder cardnum=null;
        int cnt=cardStack.size();
        if(cnt>1)
        {
            cardStack.remove(0);
        }
        cardnum=cardStack.get(0);
        initCardsMap(cardnum);
    }

    private void PushCardsMap(MapRecoder cardnum)      //入栈---对应操作
    {
        int cnt=cardStack.size();

        cardStack.add(0,cardnum);

        initCardsMap(cardnum);

        if(cnt>10)
        {
            for(int i=0;i<cnt-10;i++)
            {
                cardStack.remove(cnt-1-i);
            }
        }
    }

    private void initGameView() {
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xffbbada0);

        m_music = new MusicManager(getContext());
        try {
            m_music.load("sound.step.to.line.mp3");
            m_music.load("sound.merge.wav");
            m_music.load("sound.contnuousMatch1.mp3");
            m_music.load("sound.contnuousMatch2.mp3");
            m_music.load("sound.contnuousMatch3.mp3");
            m_music.load("sound.contnuousMatch4.mp3");
            m_music.load("sound.contnuousMatch5.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }

        setOnTouchListener(new View.OnTouchListener() {
            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = motionEvent.getX();
                        startY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = motionEvent.getX() - startX;
                        offsetY = motionEvent.getY() - startY;
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX > 5)
                                swipeRight();
                            else if (offsetX < -5)
                                swipeLeft();
                        } else {
                            if (offsetY > 5)
                                swipeDown();
                            else if (offsetY < -5)
                                swipeUp();
                        }
                        break;
                    default:
                        break;
                }

                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        cardWidth = (Math.min(w, h) - 10) / Config.Cnt;
        Log.d("MyScole","osc");
        cardsMap=new Card[Config.Cnt][Config.Cnt];
        addCard(cardWidth, cardWidth);

        dbHelp=((MainActivity)getContext()).getDBHelp();
        Log.d("MyScole","osc1");
        startGame();

        m_timercnt =0;
        timerStart();
    }

    public void addCard(int w, int h) {
        Card c;
        LinearLayout line = null;
        LinearLayout.LayoutParams linelp = null;

        for (int y = 0; y < Config.Cnt; y++) {
            line = new LinearLayout(getContext());
            linelp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, h);
            addView(line, linelp);

            for (int x = 0; x < Config.Cnt; x++) {
                c = new Card(getContext());

                LinearLayout.LayoutParams cardlp = new LinearLayout.LayoutParams(w-10, w-10);
                cardlp.setMargins(10,10,0,0);
                //line.addView(c, w, h);
                line.addView(c,cardlp);

                cardsMap[y][x] = c;
            }
        }
    }

    public void startGame()
    {
        MapRecoder cardnum=null;
        int cnt=cardStack.size();
        if(cnt>0)
        {
            Log.d("MyScole","st");
            cardnum=cardStack.get(0).objClone();
            cardStack.clear();

            if(cardnum.scole>200)
            {
                saveScole(cardnum.scole);
            }
        }
        else
        {
            Log.d("MyScole","st1");
            cardnum=new MapRecoder();
            cardnum.scole=0;
        }

        calScole(cardnum.scole);
        Log.d("MyScole","st3");

        for(int i=0;i<Config.Cnt;i++)
        {
            for(int j=0;j<Config.Cnt;j++)
            {
                cardnum.cardmap[i][j]=0;
            }
        }
        cardnum.scole=0;

        cardStack.add(cardnum);
        initCardsMap(cardnum);
        Log.d("MyScole","st3");

        addRandNum();
        addRandNum();

        findTopScole();

        m_timercnt =0;
    }

    public void undo()
    {
        PopCardsMap();
        calScole(cardStack.get(0).scole);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        SQLiteDatabase db= dbHelp.getWritableDatabase();
        saveScole(cardStack.get(0).scole);
    }

    void timerStart()
    {
        timeView= ((Activity)getContext()).findViewById(R.id.timeView);
        //timeView.setText("ABC");


        handler=new Handler();

        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,1000);

                m_timercnt++;
                if(m_timercnt<0)
                    m_timercnt=0;

                int hourInt=m_timercnt/3600;
                int minInt=(m_timercnt%3600)/60;
                int secInt=m_timercnt%60;
                String hourStr=null;
                String minStr=null;
                String secStr=null;
                if(hourInt>9)   hourStr=String.valueOf(hourInt);
                else            hourStr="0"+String.valueOf(hourInt);
                if(minInt>9)   minStr=String.valueOf(minInt);
                else            minStr="0"+String.valueOf(minInt);
                if(secInt>9)   secStr=String.valueOf(secInt);
                else            secStr="0"+String.valueOf(secInt);
                //Log.d("MyLog",hourStr+":"+minStr+":"+secStr);
                timeView.setText(hourStr+":"+minStr+":"+secStr);
                calScole(cardStack.get(0).scole);
                //Log.d("MyLog","Timer End");
            }
        };

        handler.postDelayed(runnable,1000);
    }

    public void addRandNum()
    {
        int emptysize=0;
        int num=0;
        MapRecoder newTable=cardStack.get(0);

        int val=Math.random()>0.1?2:4;

        for(int y=0;y<Config.Cnt;y++)
        {
            for(int x=0;x<Config.Cnt;x++)
            {
                if(newTable.cardmap[y][x]==0)
                    emptysize++;
            }
        }

        num=(int)(Math.random()*emptysize);

        int index=0;
        for(int y=0;y<Config.Cnt;y++)
        {
            for(int x=0;x<Config.Cnt;x++) {
                if(newTable.cardmap[y][x]==0)
                {
                    if(index==num) {
                        newTable.cardmap[y][x]=val;
                        initCardsMap(newTable);
                        return;
                    }
                    index++;
                }
            }
        }

        initCardsMap(newTable);
    }

    //原理：x与x+1交换,如果x+1为空，查找下一个直到不为空，不为空就做处理，处理如下：
    //1、如果x为空，交换
    //2、如果x不为空，并且相等，合并，x1清零
    protected void swipeLeft()
    {
        boolean merge=false;
        boolean change=false;
        int mergeLevel=0;       //合并等级

        MapRecoder tempList=null;
        MapRecoder newTable=cardStack.get(0);

        tempList=newTable.objClone();

        int scoleBak=tempList.scole;

        for(int y=0;y<Config.Cnt;y++)
        {
            for(int x=0;x<Config.Cnt;x++)
            {
                for(int x1=x+1;x1<Config.Cnt;x1++)
                {
                    //if(cardsMap[y][x1].getNum()>0)
                    if(tempList.cardmap[y][x1]>0)
                    {
                        if(tempList.cardmap[y][x]<=0)  //交换
                        {
                            addMoveAnimation(tempList.cardmap[y][x1],tempList.cardmap[y][x],x1,x,y,y);
                            tempList.cardmap[y][x]=tempList.cardmap[y][x1];
                            tempList.cardmap[y][x1]=0;
                            change=true;
                        }
                        else if(tempList.cardmap[y][x]==tempList.cardmap[y][x1])
                        {
                            addMoveAnimation(tempList.cardmap[y][x1],tempList.cardmap[y][x],x1,x,y,y);
                            tempList.cardmap[y][x]=tempList.cardmap[y][x1]*2;
                            tempList.cardmap[y][x1]=0;

                            tempList.scole+=tempList.cardmap[y][x];
                            merge=true;
                            change=true;
                        }
                        else
                        {
                            break;
                        }
                    }
                }
            }
        }
        musicRun(tempList.scole-scoleBak);

        if(change)
        {
            PushCardsMap(tempList);
        }

        if(true)
        {
            checkOver();
            addRandNum();
            calScole(tempList.scole);
        }

        tempList=null;
        newTable=null;
    }

    protected void swipeRight()
    {
        boolean merge=false;
        boolean change=false;
        int mergeLevel=0;       //合并等级


        MapRecoder tempList=null;
        MapRecoder newTable=cardStack.get(0);

        tempList=newTable.objClone();
        int scoleBak=tempList.scole;

        for(int y=0;y<Config.Cnt;y++)
        {
            for(int x=Config.Cnt-1;x>=0;x--)
            {
                for(int x1=x-1;x1>=0;x1--)
                {
                    if(tempList.cardmap[y][x1]>0)
                    {
                        if(tempList.cardmap[y][x]<=0)  //交换
                        {
                            addMoveAnimation(tempList.cardmap[y][x1],tempList.cardmap[y][x],x1,x,y,y);
                            tempList.cardmap[y][x]=tempList.cardmap[y][x1];
                            tempList.cardmap[y][x1]=0;
                            change=true;
                        }
                        else if(tempList.cardmap[y][x]==tempList.cardmap[y][x1])
                        {
                            addMoveAnimation(tempList.cardmap[y][x1],tempList.cardmap[y][x],x1,x,y,y);

                            tempList.cardmap[y][x]=tempList.cardmap[y][x1]*2;
                            tempList.cardmap[y][x1]=0;
                            tempList.scole+=tempList.cardmap[y][x];
                            merge=true;
                            change=true;
                        }
                        else
                        {
                            break;
                        }
                    }
                }
            }
        }
        musicRun(tempList.scole-scoleBak);

        if(change)
        {
            PushCardsMap(tempList);
        }

        if(true)
        {
            checkOver();
            addRandNum();
            calScole(tempList.scole);
        }

        tempList=null;
        newTable=null;
    }

    protected void swipeUp()
    {
        boolean merge=false;
        boolean change=false;
        int mergeLevel=0;       //合并等级


        MapRecoder tempList=null;
        MapRecoder newTable=cardStack.get(0);

        tempList=newTable.objClone();
        int scoleBak=tempList.scole;

        for(int x=0;x<Config.Cnt;x++)
        {
            for(int y=0;y<Config.Cnt;y++)
            {
                for(int y1=y+1;y1<Config.Cnt;y1++)
                {
                    if(tempList.cardmap[y1][x]>0)
                    {
                        if(tempList.cardmap[y][x]<=0)  //交换
                        {
                            addMoveAnimation(tempList.cardmap[y1][x],tempList.cardmap[y][x],x,x,y1,y);
                            tempList.cardmap[y][x]=tempList.cardmap[y1][x];
                            tempList.cardmap[y1][x]=0;
                            change=true;
                        }
                        else if(tempList.cardmap[y][x]==tempList.cardmap[y1][x])
                        {

                            addMoveAnimation(tempList.cardmap[y1][x],tempList.cardmap[y][x],x,x,y1,y);
                            tempList.cardmap[y][x]=tempList.cardmap[y1][x]*2;
                            tempList.cardmap[y1][x]=0;
                            tempList.scole+=tempList.cardmap[y][x];
                            merge=true;
                            change=true;
                        }
                        else
                        {
                            break;
                        }
                    }
                }
            }
        }
        musicRun(tempList.scole-scoleBak);

        if(change)
        {
            PushCardsMap(tempList);
        }

        if(true)
        {
            checkOver();
            addRandNum();
            calScole(tempList.scole);
        }

        tempList=null;
        newTable=null;
    }

    protected void swipeDown()
    {
        boolean merge=false;
        boolean change=false;
        int mergeLevel=0;       //合并等级


        MapRecoder tempList=null;
        MapRecoder newTable=cardStack.get(0);

        tempList=newTable.objClone();
        int scoleBak=tempList.scole;

        for(int x=0;x<Config.Cnt;x++)
        {
            for(int y=Config.Cnt-1;y>=0;y--)
            {
                for(int y1=y-1;y1>=0;y1--)
                {
                    if(tempList.cardmap[y1][x]>0)
                    {
                        if(tempList.cardmap[y][x]<=0)  //交换
                        {

                            addMoveAnimation(tempList.cardmap[y1][x],tempList.cardmap[y][x],x,x,y1,y);
                            tempList.cardmap[y][x]=tempList.cardmap[y1][x];
                            tempList.cardmap[y1][x]=0;
                            change=true;
                        }
                        else if(tempList.cardmap[y][x]==tempList.cardmap[y1][x])
                        {
                            addMoveAnimation(tempList.cardmap[y1][x],tempList.cardmap[y][x],x,x,y1,y);
                            tempList.cardmap[y][x]=tempList.cardmap[y1][x]*2;
                            tempList.cardmap[y1][x]=0;
                            tempList.scole+=tempList.cardmap[y][x];
                            merge=true;
                            change=true;
                        }
                        else
                        {
                            break;
                        }
                    }
                }
            }
        }
        musicRun(tempList.scole-scoleBak);

        if(change)
        {
            PushCardsMap(tempList);
        }

        if(true)
        {
            checkOver();
            addRandNum();
            calScole(tempList.scole);
        }

        tempList=null;
        newTable=null;
    }

    protected int checkOver()
    {
        int zeroCnt=0;
        for(int y=0;y<Config.Cnt;y++)
        {
            for(int x=0;x<Config.Cnt;x++)
            {
                if(cardsMap[y][x].getNum()==0)
                {
                    zeroCnt++;
                }
                else if(cardsMap[y][x].getNum()>=2048)
                {
                    if(m_toast==null)
                    {
                        m_toast=Toast.makeText(getContext(),"",Toast.LENGTH_SHORT);
                    }
                    m_toast.setText("Game Success!");
                    m_toast.setDuration(Toast.LENGTH_SHORT);
                    m_toast.show();
                    return 1;   //游戏成功
                }
            }
        }

        if(zeroCnt==0)
        {
            if(m_toast==null)
            {
                m_toast=Toast.makeText(getContext(),"",Toast.LENGTH_SHORT);
            }
            m_toast.setText("Game Over!");
            m_toast.setDuration(Toast.LENGTH_SHORT);
            m_toast.show();

            return -1;      //游戏失败
        }
        return 0;
    }


    public void addMoveAnimation(final int fromNum,final int toNum, int fromX, final int toX, int fromY, final int toY)
    {
        //final int fnum=num;
        final Card moveCard=new Card(getContext());
        final Card toCard=new Card(getContext());       //为了防止动画时效性导致的突变效果


        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(cardWidth-10, cardWidth-10);
        lp.leftMargin = fromX*cardWidth+10;
        lp.topMargin = fromY*cardWidth+10;
        ((MainActivity)getContext()).getViewLayout().addView(moveCard,lp);

        FrameLayout.LayoutParams tolp = new FrameLayout.LayoutParams(cardWidth-10, cardWidth-10);
        tolp.leftMargin = toX*cardWidth+10;
        tolp.topMargin = toY*cardWidth+10;
        ((MainActivity)getContext()).getViewLayout().addView(toCard,tolp);

        Log.d("MyLog","fromNum="+fromNum+"toNum="+toNum);
        moveCard.setNum(fromNum,Config.show_style);
        moveCard.setVisibility(View.VISIBLE);
        moveCard.getLabel().setVisibility(View.VISIBLE);
        toCard.setNum(toNum,Config.show_style);
        toCard.setVisibility(View.VISIBLE);
        toCard.getLabel().setVisibility(View.VISIBLE);

        TranslateAnimation ta = new TranslateAnimation(0, cardWidth*(toX-fromX), 0, cardWidth*(toY-fromY));
        ta.setDuration(100);
        //ta.setFillAfter(true);
        ta.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                //cardsMap[toY][toX].getLabel().setVisibility(View.VISIBLE);
                //if(flag==1)
                //    cardsMap[toY][toX].setNum(2*fnum,Config.show_style);
                //else
                //    cardsMap[toY][toX].setNum(fnum,Config.show_style);
                moveCard.getLabel().setVisibility(View.INVISIBLE);
                moveCard.setVisibility(View.INVISIBLE);
                toCard.getLabel().setVisibility(View.INVISIBLE);
                toCard.setVisibility(View.INVISIBLE);
                //moveCard.setNum(0,Config.show_style);
                moveCard.setAnimation(null);
                moveCard.clearAnimation();

                animation=null;     //防止内存泄露

                ((MainActivity)getContext()).getViewLayout().removeView(moveCard);     //防止内存泄露
                ((MainActivity)getContext()).getViewLayout().removeView(toCard);    //防止内存泄露
            }
        });

        moveCard.startAnimation(ta);
    }

    private int calScole(int sscole)
    {
        TextView scoleText= ((Activity)getContext()).findViewById(R.id.scoleText);

        scoleText.setText(String.valueOf(sscole));
        scoleText=null;
        return 0;
    }

    public void musicRun(int musicLevel)
    {
        if(Config.game_music!=0)
        {
            if(musicLevel>=2048)   //16 32 64 128 256 512 1024 2048
            {
                m_music.play("sound.contnuousMatch5.mp3");
            }
            else if(musicLevel>=1024)   //16 32 64 128 256 512 1024 2048
            {
                m_music.play("sound.contnuousMatch4.mp3");
            }
            else if(musicLevel>=512)   //16 32 64 128 256 512 1024 2048
            {
                m_music.play("sound.contnuousMatch3.mp3");
            }
            else if(musicLevel>=256)   //16 32 64 128 256 512 1024 2048
            {
                m_music.play("sound.contnuousMatch2.mp3");
            }
            else if(musicLevel>=128)   //16 32 64 128 256 512 1024 2048
            {
                m_music.play("sound.contnuousMatch1.mp3");
            }
            else if(musicLevel>0)
            {
                m_music.play("sound.merge.wav");
            }
            else
            {
                m_music.play("sound.step.to.line.mp3");
            }
        }
    }

    public void findTopScole()
    {
        SQLiteDatabase db= dbHelp.getWritableDatabase();
        Cursor cursor = db.query(dbHelp.DB_Table,
                new String[] {"max(Scole)"},
                null,
                null,
                null, null, null);

        if(cursor.getCount()>0)
        {
            Log.d("MyLog","Curse Column="+cursor.getColumnCount());
            cursor.moveToFirst();

            m_maxScole=cursor.getInt(0);
            TextView topScoleView= ((Activity)getContext()).findViewById(R.id.topScole);
            topScoleView.setText(String.valueOf(m_maxScole));
        }
        db.close();
    }

    public void saveScole(int mscole)
    {
        SQLiteDatabase db= dbHelp.getWritableDatabase();
        db.execSQL("insert into " + dbHelp.DB_Table + " (rankdate, Scole, useTime, mode) values ('2017-11-12 11:11:11:123',"+mscole+",132,"+0+")");
        db.close();
    }

    public void saveScole()
    {
        int mscole=0;
        int cnt=cardStack.size();
        if(cnt>0)
        {
            mscole=cardStack.get(0).scole;
        }
        SQLiteDatabase db= dbHelp.getWritableDatabase();
        db.execSQL("insert into " + dbHelp.DB_Table + " (rankdate, Scole, useTime, mode) values ('2017-11-12 11:11:11:123',"+mscole+",132,"+0+")");
        db.close();
    }

    public class MapRecoder{
        public int cardmap[][];
        public int scole=0;

        MapRecoder()
        {
            cardmap=new int[Config.Cnt][Config.Cnt];
        }

        public MapRecoder objClone()
        {
            MapRecoder ret=new MapRecoder();
            for(int y=0;y<Config.Cnt;y++)
            {
                for(int x=0;x<Config.Cnt;x++)
                {
                    ret.cardmap[y][x]=this.cardmap[y][x];
                }
            }
            ret.scole=this.scole;
            return ret;
        }
    };

    protected Card cardsMap[][] = null;
    protected boolean isOver;
    protected Toast m_toast=null;
    protected int m_scole=0;
    private DbHelper dbHelp=null;
    //private SQLiteDatabase db;
    private int m_currentId=0;
    private int m_maxScole=0;

    //private Card moveCard;
    private int cardWidth=0;

    Handler handler;
    private int m_timercnt;
    private TextView timeView;

    private MusicManager m_music=null;

    private List<MapRecoder> cardStack = new ArrayList<MapRecoder>();         //动作缓冲栈
}
