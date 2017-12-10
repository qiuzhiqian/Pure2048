package com.mycode.xml.pure2048;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btn1 = (Button)findViewById(R.id.normal_mode);
        btn2 = (Button)findViewById(R.id.musicSta);
        //btn_show = (Button)findViewById(R.id.theme_select);

        spinner =(Spinner)findViewById(R.id.spinner01);

        themeList=new ArrayList<String>();
        String extPath=getApplicationContext().getExternalFilesDir("themes").getAbsolutePath();
        initThemeList(extPath);

        //themeList.add("num_color");
        //themeList.add("chaodai");

        adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,themeList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<String> adp=(ArrayAdapter<String>)adapterView.getAdapter();
                Config.game_theme=adp.getItem(i);
                saveConfig("appConfig");
                initDefTheme(Config.game_theme);
                Log.d("MyConfig","Config.game_mode="+Config.show_style);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        configGame();

        intent=new Intent();

        btn1.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                //saveConfig("appConfig");
                String extPath=getApplicationContext().getExternalFilesDir("themes").getAbsolutePath();
                if(Config.show_style>2)
                {
                    Log.d("MyConfig","use png data");
                    Config.initBpList(extPath+"/"+Config.game_theme);
                }
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
                saveConfig("appConfig");
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

        setThemeSelect(Config.game_theme);

        flag=initDefTheme(Config.game_theme);

        if(flag==false)
        {
            Log.d("MyConfig","Use def theme");
            saveTheme(Config.game_theme);
        }

        //String extPath=getApplicationContext().getExternalFilesDir("themes").getAbsolutePath();
        //initThemeList(extPath);
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
        String themesDir="themes";
        Properties properties = new Properties();
        String extPath=getApplicationContext().getExternalFilesDir(themesDir).getAbsolutePath();
        String filespath=extPath+"/"+themestr+"/"+themestr;
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
        String themesDir="themes";
        Properties properties = new Properties();
        String extPath=getApplicationContext().getExternalFilesDir(themesDir).getAbsolutePath();
        String filespath=extPath+"/"+themestr+"/"+themestr;
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

    public void initThemeList(String dirpath)
    {
        File file=new File(dirpath);
        themeList.clear();

        File files[] = file.listFiles();
        for(File f : files)
        {
            Log.d("MyConfig","file:"+f.getName());
            if(f.isDirectory())
            {
                themeList.add(f.getName());
            }
        }

    }

    public void setThemeSelect(String themeName)
    {
        int cnt=themeList.size();
        int i=0;
        for(i=0;i<cnt;i++)
        {
            if(themeList.get(i).equals(themeName))
                break;
        }
        if(i>=cnt)  i=0;
        spinner.setSelection(i);
    }

    private Intent intent;
    private  Button btn1=null;
    private  Button btn2=null;
    private Button btn_show=null;

    private Spinner spinner=null;
    private List<String> themeList=null;
    private ArrayAdapter<String> adapter=null;
}
