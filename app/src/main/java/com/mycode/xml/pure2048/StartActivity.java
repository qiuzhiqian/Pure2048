package com.mycode.xml.pure2048;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btn1 = (Button)findViewById(R.id.normal_mode);
        btn2 = (Button)findViewById(R.id.musicSta);

        configGame();

        intent=new Intent();

        btn1.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                saveConfig();
                intent.setClass(StartActivity.this,MainActivity.class);
                intent.putExtra("Mode",1);
                startActivity(intent);

                StartActivity.this.finish();
            }
        });

        btn2.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(Config.game_music!=0)
                {
                    Config.game_music=0;
                    btn2.setText("音乐:关");
                    btn2.setBackgroundColor(0x998aa3bb);
                }
                else
                {
                    Config.game_music=1;
                    btn2.setText("音乐:开");
                    btn2.setBackgroundColor(0x9965dc25);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(Config.game_music!=0)
        {
            //Config.game_music=0;
            btn2.setText("音乐:开");
            btn2.setBackgroundColor(0x9965dc25);
        }
        else
        {
            //Config.game_music=1;
            btn2.setText("音乐:关");
            btn2.setBackgroundColor(0x998aa3bb);
        }
    }

    private void configGame()
    {
        //导入配置
        boolean flag=false;
        flag=initfConfig();
        if(flag==false)
        {
            saveConfig();
        }

        flag=initDefTheme();
        if(flag==false)
        {
            saveTheme();
        }

    }

    private boolean initfConfig()
    {
        Properties properties = new Properties();
        String extPath=getApplicationContext().getExternalFilesDir("").getAbsolutePath();
        File file = new File(extPath+"/appConfig");
        if(!file.exists()) {
            return false;
        }
        else
        {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            try {
                properties.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            Config.Cnt = Integer.parseInt(properties.getProperty("lens","4"),10);
            Config.show_style = Integer.parseInt(properties.getProperty("show_style","2"),10);
            Config.game_mode = Integer.parseInt(properties.getProperty("game_mode","1"),10);
            Config.game_music = Integer.parseInt(properties.getProperty("game_music","1"),10);
        }
        return true;
    }

    private boolean initDefTheme()
    {
        Properties properties = new Properties();
        String extPath=getApplicationContext().getExternalFilesDir("").getAbsolutePath();
        File file = new File(extPath+"/appTheme");
        if(!file.exists()) {
            return false;
        }
        else
        {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            try {
                properties.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            int color_index=0;
            Config.ColorTable[color_index++] = Color.parseColor("#"+properties.getProperty("color_0","ffc0c0c0"));
            Config.ColorTable[color_index++] = Color.parseColor("#"+properties.getProperty("color_2","ffeee4da"));
            Config.ColorTable[color_index++] = Color.parseColor("#"+properties.getProperty("color_4","ffff66a3"));
            Config.ColorTable[color_index++] = Color.parseColor("#"+properties.getProperty("color_8","fff2b179"));
            Config.ColorTable[color_index++] = Color.parseColor("#"+properties.getProperty("color_16","fff59563"));
            Config.ColorTable[color_index++] = Color.parseColor("#"+properties.getProperty("color_32","ff66b2ff"));
            Config.ColorTable[color_index++] = Color.parseColor("#"+properties.getProperty("color_64","fff65e3b"));
            Config.ColorTable[color_index++] = Color.parseColor("#"+properties.getProperty("color_128","ffedcf72"));
            Config.ColorTable[color_index++] = Color.parseColor("#"+properties.getProperty("color_256","ff33cc33"));
            Config.ColorTable[color_index++] = Color.parseColor("#"+properties.getProperty("color_512","ff40ff00"));
            Config.ColorTable[color_index++] = Color.parseColor("#"+properties.getProperty("color_1024","ffff00ff"));
            Config.ColorTable[color_index++] = Color.parseColor("#"+properties.getProperty("color_2048","ffd926d9"));
            Config.ColorTable[color_index++] = Color.parseColor("#"+properties.getProperty("color_other","ff737373"));
        }
        return true;
    }

    private void saveConfig()
    {
        Properties properties = new Properties();
        String extPath=getApplicationContext().getExternalFilesDir("").getAbsolutePath();
        File file = new File(extPath+"/appConfig");
        try {
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            properties.setProperty("lens",String.valueOf(Config.Cnt));
            properties.setProperty("show_style",String.valueOf(Config.show_style));
            properties.setProperty("game_mode",String.valueOf(Config.game_mode));
            properties.setProperty("game_music",String.valueOf(Config.game_music));

            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveTheme()
    {
        Properties properties = new Properties();
        String extPath=getApplicationContext().getExternalFilesDir("").getAbsolutePath();
        File file = new File(extPath+"/appTheme");
        try {
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            int color_index=0;

            properties.setProperty("color_0",Config.toColorStr(Config.ColorTable[color_index++]));
            properties.setProperty("color_2",Integer.toHexString(Config.ColorTable[color_index++]));
            properties.setProperty("color_4",Integer.toHexString(Config.ColorTable[color_index++]));
            properties.setProperty("color_8",Integer.toHexString(Config.ColorTable[color_index++]));
            properties.setProperty("color_16",Integer.toHexString(Config.ColorTable[color_index++]));
            properties.setProperty("color_32",Integer.toHexString(Config.ColorTable[color_index++]));
            properties.setProperty("color_64",Integer.toHexString(Config.ColorTable[color_index++]));
            properties.setProperty("color_128",Integer.toHexString(Config.ColorTable[color_index++]));
            properties.setProperty("color_256",Integer.toHexString(Config.ColorTable[color_index++]));
            properties.setProperty("color_512",Integer.toHexString(Config.ColorTable[color_index++]));
            properties.setProperty("color_1024",Integer.toHexString(Config.ColorTable[color_index++]));
            properties.setProperty("color_2048",Integer.toHexString(Config.ColorTable[color_index++]));
            properties.setProperty("color_other",Integer.toHexString(Config.ColorTable[color_index++]));

            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Intent intent;
    private  Button btn1=null;
    private  Button btn2=null;
}
