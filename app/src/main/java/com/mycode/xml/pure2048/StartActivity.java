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
                saveConfig("appConfig");
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
        String appconfigpath="appConfig";
        flag=initDefConfig(appconfigpath);
        if(flag==false)
        {
            Log.d("MyConfig","Use def config");
            saveConfig(appconfigpath);
        }

        Log.d("MyConfig","Theme="+Config.game_theme);
        flag=initDefTheme(Config.game_theme);

        if(flag==false)
        {
            Log.d("MyConfig","Use def theme");
            saveTheme(Config.game_theme);
        }
    }

    private boolean initDefConfig(String confstr)
    {
        Properties properties = new Properties();
        String extPath=getApplicationContext().getExternalFilesDir("").getAbsolutePath();
        File file = new File(extPath+"/"+confstr);
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

            Config.game_mode = Integer.parseInt(properties.getProperty("game_mode","1"),10);
            Config.game_music = Integer.parseInt(properties.getProperty("game_music","1"),10);
            Config.game_theme = properties.getProperty("game_theme","num_color");
        }
        return true;
    }

    private boolean initDefTheme(String themestr)
    {
        Properties properties = new Properties();
        String extPath=getApplicationContext().getExternalFilesDir(themestr).getAbsolutePath();
        String filespath=extPath+"/"+themestr;
        Log.d("MyConfig","Path="+filespath);
        File file = new File(filespath);
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
            int num_index=0;
            Config.show_style = Integer.parseInt(properties.getProperty("show_style","2"),10);
            switch(Config.show_style)
            {
                case 1:     //数字
                    break;
                case 3:     //图片
                default:    //数字+颜色
                {
                    num_index=0;
                    Config.NumTable[num_index] = properties.getProperty("show_0",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_2",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_4",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_8",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_16",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_32",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_64",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_128",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_256",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_512",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_1024",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_2048",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_4096",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_8192",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_16384",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_32768",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_65536",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_131072",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_262144",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_524288",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_1048676",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_2097152",Config.NumTable[num_index]);
                    num_index++;
                    Config.NumTable[num_index] = properties.getProperty("show_other",Config.NumTable[num_index]);
                    num_index++;

                    Config.initBpList(extPath);
                }
                    break;

            }
        }
        return true;
    }

    private void saveConfig(String confstr)
    {
        Properties properties = new Properties();
        String extPath=getApplicationContext().getExternalFilesDir("").getAbsolutePath();
        File file = new File(extPath+"/"+confstr);
        try {
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            properties.setProperty("lens",String.valueOf(Config.Cnt));

            properties.setProperty("game_mode",String.valueOf(Config.game_mode));
            properties.setProperty("game_music",String.valueOf(Config.game_music));
            properties.setProperty("game_theme",Config.game_theme);

            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("MyConfig","saveConfig Excep");
        }
    }

    private void saveTheme(String themestr)
    {
        Properties properties = new Properties();
        String extPath=getApplicationContext().getExternalFilesDir(themestr).getAbsolutePath();
        String filespath=extPath+"/"+themestr;
        File file = new File(filespath);
        try {
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);

            properties.setProperty("show_style",String.valueOf(Config.show_style));

            int num_index=0;

            properties.setProperty("show_0",Config.NumTable[num_index++]);
            properties.setProperty("show_2",Config.NumTable[num_index++]);
            properties.setProperty("show_4",Config.NumTable[num_index++]);
            properties.setProperty("show_8",Config.NumTable[num_index++]);
            properties.setProperty("show_16",Config.NumTable[num_index++]);
            properties.setProperty("show_32",Config.NumTable[num_index++]);
            properties.setProperty("show_64",Config.NumTable[num_index++]);
            properties.setProperty("show_128",Config.NumTable[num_index++]);
            properties.setProperty("show_256",Config.NumTable[num_index++]);
            properties.setProperty("show_512",Config.NumTable[num_index++]);
            properties.setProperty("show_1024",Config.NumTable[num_index++]);
            properties.setProperty("show_2048",Config.NumTable[num_index++]);
            properties.setProperty("show_4096",Config.NumTable[num_index++]);
            properties.setProperty("show_8192",Config.NumTable[num_index++]);
            properties.setProperty("show_16384",Config.NumTable[num_index++]);
            properties.setProperty("show_32768",Config.NumTable[num_index++]);
            properties.setProperty("show_65536",Config.NumTable[num_index++]);
            properties.setProperty("show_131072",Config.NumTable[num_index++]);
            properties.setProperty("show_262144",Config.NumTable[num_index++]);
            properties.setProperty("show_524288",Config.NumTable[num_index++]);
            properties.setProperty("show_1048676",Config.NumTable[num_index++]);
            properties.setProperty("show_2097152",Config.NumTable[num_index++]);
            properties.setProperty("show_other",Config.NumTable[num_index++]);
            properties.store(fos, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Intent intent;
    private  Button btn1=null;
    private  Button btn2=null;
}
