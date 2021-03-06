package com.mycode.xml.pure2048;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class StartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<SettingMenu> mDatas;
    private SettingAdapter recycleAdapter;

    private int themeCheckedIndex=0;
    private List<String> themesList = null;

    private int numCheckedIndex=0;
    private List<String> numList = null;

    private int modeCheckedIndex=0;
    private List<String> modeList=null;

    private int recorderCheckedIndex=0;
    private List<String> recorderList=null;

    private String rootPath=null;

    private Intent intent=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        rootPath=getApplicationContext().getExternalFilesDir("").getAbsolutePath();
        Log.d("MyPath","rootPath="+rootPath);

        initConfig("appConfig");

        themesList = new ArrayList<String>();
        numList = new ArrayList<String>();
        modeList = new ArrayList<String>();
        recorderList = new ArrayList<String>();

        initThemeList(rootPath+"/"+"themes");
        if(themesList.size()==0)    //说明没有主题，则创建默认主题
        {
            creatDefTheme("num_color");
            initThemeList(rootPath+"/"+"themes");   //重新刷新列表
        }
        initNumList();
        initModeList();
        initRecordList(rootPath+"/"+"recorders");

        initData();
        defSelect();

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view );


        recycleAdapter= new SettingAdapter(StartActivity.this , mDatas );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recyclerView.setAdapter( recycleAdapter);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recycleAdapter.setOnItemClickListener(new SettingAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view , int position){
                //Toast.makeText(StartActivity.this, mDatas.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                switch(position)
                {
                    case 0:
                        switchChange(position);
                        break;
                    case 1:
                        showThemeList(position);
                        break;
                    case 2:
                        showNumList(position);
                        break;
                    case 3:
                        showModeList(position);
                        break;
                    case 4:
                        showRecorderList(position);
                        break;
                    default:
                        break;
                }
            }
        });
        recycleAdapter.notifyDataSetChanged();

        intent =new Intent();
        Button btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(rootPath+"/appConfig");        //保存当前配置
                saveConfig(file);
                File pf=new File(rootPath+"/themes"+"/"+Config.game_theme+"/"+Config.game_theme);
                loadTheme(pf);      //加載主題内容
                Log.d("MyConfig","show_style="+Config.show_style);
                if(Config.show_style>2)
                {

                    Log.d("MyConfig","use png data");
                    String themePath=rootPath+"/themes"+"/"+Config.game_theme;
                    Log.d("MyConfig","themePath="+themePath);
                    Config.initBpList(themePath);       //加載圖片内容
                }
                intent.setClass(StartActivity.this,MainActivity.class);
                intent.putExtra("Mode",1);
                intent.putExtra("Recorder",mDatas.get(4).getVal());
                startActivity(intent);

                StartActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.d("MyConfig","music="+Config.game_music);
        File file = new File(rootPath+"/appConfig");
        saveConfig(file);
        Log.d("MyExit","SaveConfig");
        super.onBackPressed();
    }

    private void initData() {
        mDatas = new ArrayList<SettingMenu>();
        SettingMenu musicItem = new SettingMenu();
        SettingMenu themeItem = new SettingMenu();
        SettingMenu numItem = new SettingMenu();
        SettingMenu modeItem = new SettingMenu();
        SettingMenu fileItem = new SettingMenu();

        musicItem.setTitle("音效开关");
        musicItem.setType(0);
        themeItem.setTitle("主题选择");
        themeItem.setVal("num_color");
        themeItem.setType(1);
        numItem.setTitle("矩阵数量");
        numItem.setVal("4x4");
        numItem.setType(1);
        modeItem.setTitle("模式选择");
        modeItem.setVal("");
        modeItem.setType(1);
        fileItem.setTitle("档案载入");
        fileItem.setVal("");
        fileItem.setType(1);

        mDatas.add(musicItem);
        mDatas.add(themeItem);
        mDatas.add(numItem);
        mDatas.add(modeItem);
        mDatas.add(fileItem);
    }

    private void initThemeList(String dirpath)
    {
        File file=new File(dirpath);
        themesList.clear();

        if(!file.isDirectory())
        {
            file.mkdir();
        }

        File files[] = file.listFiles();
        for(File f : files)
        {
            if(f.isDirectory())
            {
                themesList.add(f.getName());
            }
        }

    }

    private void showThemeList(final int index)
    {
        int len = themesList.size();
        if(len==0)
        {
            mDatas.get(index).setVal("");
            themeCheckedIndex=0;
            return;
        }
        String[] items= new String[len];
        themesList.toArray(items);
        if(themeCheckedIndex>=len)  themeCheckedIndex=0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("请选择主题");
        //设置图标
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(items, themeCheckedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                themeCheckedIndex=i;
                mDatas.get(index).setVal(themesList.get(themeCheckedIndex));
                recycleAdapter.notifyDataSetChanged();
                dialogInterface.dismiss();
                Config.game_theme=themesList.get(themeCheckedIndex);
            }
        });
        builder.create();
        builder.show();
    }

    private void initNumList()
    {
        numList.clear();
        numList.add("4x4");
        numList.add("5x5");
        numList.add("6x6");
        numList.add("7x7");
        numList.add("8x8");
        numList.add("9x9");
        numList.add("10x10");
    }

    private void showNumList(final int index)
    {
        int len = numList.size();
        String[] items= new String[len];
        numList.toArray(items);
        if(numCheckedIndex>=len)  numCheckedIndex=0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("请选择大小");
        //设置图标
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(items, numCheckedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                numCheckedIndex=i;
                mDatas.get(index).setVal(numList.get(numCheckedIndex));
                recycleAdapter.notifyDataSetChanged();
                dialogInterface.dismiss();
                Config.game_lens=(numCheckedIndex+4);
            }
        });
        builder.create();
        builder.show();
    }

    private void initModeList()
    {
        modeList.clear();
        modeList.add("普通模式");
        modeList.add("生存模式");
    }

    private void showModeList(final int index)
    {
        int len = modeList.size();
        String[] items= new String[len];
        modeList.toArray(items);
        if(modeCheckedIndex>=len)  modeCheckedIndex=0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("请选择模式");
        //设置图标
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(items, modeCheckedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                modeCheckedIndex=i;
                mDatas.get(index).setVal(modeList.get(modeCheckedIndex));
                recycleAdapter.notifyDataSetChanged();
                dialogInterface.dismiss();
                Config.game_lens=(modeCheckedIndex+4);
            }
        });
        builder.create();
        builder.show();
    }

    private void initRecordList(String dirpath)
    {
        File file=new File(dirpath);
        recorderList.clear();

        if(!file.isDirectory())
        {
            file.mkdir();
        }

        recorderList.add("");

        File files[] = file.listFiles();
        for(File f : files)
        {
            if(f.isFile())
            {
                recorderList.add(f.getName());
            }
        }
    }

    private void showRecorderList(final int index)
    {
        int len = recorderList.size();
        if(len==0)
        {
            mDatas.get(index).setVal("");
            recorderCheckedIndex=0;
            return;
        }
        String[] items= new String[len];
        recorderList.toArray(items);
        if(recorderCheckedIndex>=len)  recorderCheckedIndex=0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置标题
        builder.setTitle("请选择记录");
        //设置图标
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setSingleChoiceItems(items, recorderCheckedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                recorderCheckedIndex=i;
                mDatas.get(index).setVal(recorderList.get(recorderCheckedIndex));
                recycleAdapter.notifyDataSetChanged();
                dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void switchChange(int index)
    {
        if(mDatas.get(index).getSwtchSta())
        {
            mDatas.get(index).setSwitchSta(false);
            recycleAdapter.notifyDataSetChanged();
            Config.game_music=0;
        }
        else
        {
            mDatas.get(index).setSwitchSta(true);
            recycleAdapter.notifyDataSetChanged();
            Config.game_music=1;
        }
    }

    private void initConfig(String fileName)
    {
        Properties properties = new Properties();
        File file = new File(rootPath+"/"+fileName);
        if(!file.isFile())  //不存在这个配置文件
        {
            try {
                file.createNewFile();
                saveConfig(file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else    //存在配置文件
        {
            loadConfig(file);
        }
    }

    private void loadConfig(File pfile)
    {
        Properties properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(pfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Config.game_lens = Integer.parseInt(properties.getProperty("game_lens","4"),10);

        Config.game_mode = Integer.parseInt(properties.getProperty("game_mode","1"),10);
        Config.game_music = Integer.parseInt(properties.getProperty("game_music","1"),10);
        Config.game_theme = properties.getProperty("game_theme","num_color");
    }

    private void saveConfig(File pfile)
    {
        Properties properties = new Properties();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        properties.setProperty("game_lens",String.valueOf(Config.game_lens));

        properties.setProperty("game_mode",String.valueOf(Config.game_mode));
        properties.setProperty("game_music",String.valueOf(Config.game_music));
        properties.setProperty("game_theme",Config.game_theme);

        try {
            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void creatDefTheme(String themestr)
    {
        Properties properties = new Properties();
        String themeDir=rootPath+"/themes";
        String themepath=themeDir+"/"+themestr;
        String filespath=themepath+"/"+themestr;
        Log.d("MyPath","filespath="+filespath);
        File fdir= new File(themepath);
        if(!fdir.isDirectory())
        {
            fdir.mkdirs();
        }

        File file = new File(filespath);
        if(!file.exists()) {
            try {
                file.createNewFile();
                saveTheme(file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean loadTheme(File pfile)
    {
        Properties properties = new Properties();
        FileInputStream fis = null;
        String val=null;
        try {
            fis = new FileInputStream(pfile);
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
                int base=0;
                val=properties.getProperty("show_0");
                if(val==null)   //读取失败
                {
                    return false;
                }
                Config.colorList.clear();
                Config.colorList.add(val);

                val=properties.getProperty("show_other");
                if(val==null)
                {
                    return false;
                }
                Config.colorList.add(val);

                int trycnt=40;
                while((trycnt--)>0)
                {
                    base++;
                    val=properties.getProperty("show_"+base);
                    if(val==null)       //说明查找完毕
                    {
                        break;
                    }
                    else
                    {
                        Config.colorList.add(val);
                    }
                }


            }
            break;
        }
        return true;
    }

    private void saveTheme(File pfile)
    {
        Properties properties = new Properties();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        properties.setProperty("show_style",String.valueOf(Config.show_style));

        int num_index=0;

        properties.setProperty("show_0",Config.NumTable[num_index++]);
        properties.setProperty("show_other",Config.NumTable[num_index++]);
        properties.setProperty("show_1",Config.NumTable[num_index++]);
        properties.setProperty("show_2",Config.NumTable[num_index++]);
        properties.setProperty("show_3",Config.NumTable[num_index++]);
        properties.setProperty("show_4",Config.NumTable[num_index++]);
        properties.setProperty("show_5",Config.NumTable[num_index++]);
        properties.setProperty("show_6",Config.NumTable[num_index++]);
        properties.setProperty("show_7",Config.NumTable[num_index++]);
        properties.setProperty("show_8",Config.NumTable[num_index++]);
        properties.setProperty("show_9",Config.NumTable[num_index++]);
        properties.setProperty("show_10",Config.NumTable[num_index++]);
        properties.setProperty("show_11",Config.NumTable[num_index++]);
        properties.setProperty("show_12",Config.NumTable[num_index++]);
        properties.setProperty("show_13",Config.NumTable[num_index++]);
        properties.setProperty("show_14",Config.NumTable[num_index++]);
        properties.setProperty("show_15",Config.NumTable[num_index++]);
        properties.setProperty("show_16",Config.NumTable[num_index++]);
        properties.setProperty("show_17",Config.NumTable[num_index++]);
        properties.setProperty("show_18",Config.NumTable[num_index++]);
        properties.setProperty("show_19",Config.NumTable[num_index++]);
        properties.setProperty("show_20",Config.NumTable[num_index++]);
        properties.setProperty("show_21",Config.NumTable[num_index++]);

        try {
            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int len=Config.NumTable.length;
        List<String> tempList=Config.colorList;
        tempList.clear();
        for(int i=0;i<len;i++)
        {
            tempList.add(Config.NumTable[i]);
        }
    }

    private void defSelect()
    {
        int cnt=0;
        int i=0;

        if(Config.game_music==0)
        {
            mDatas.get(0).setSwitchSta(false);
        }
        else
        {
            mDatas.get(0).setSwitchSta(true);
        }

        cnt=themesList.size();
        for(i=0;i<cnt;i++)
        {
            if(themesList.get(i).equals(Config.game_theme))
                break;
        }
        if(i>=cnt)  i=0;
        themeCheckedIndex=i;
        Log.d("MyAdapter","game_theme="+Config.game_theme);
        Log.d("MyAdapter","themeCheckedIndex="+themeCheckedIndex);

        cnt=numList.size();
        String lenStr=""+Config.game_lens+"x"+Config.game_lens;
        for(i=0;i<cnt;i++)
        {
            if(numList.get(i).equals(lenStr))
                break;
        }
        if(i>=cnt)  i=0;
        numCheckedIndex=i;

        recorderCheckedIndex=0;

        mDatas.get(1).setVal(themesList.get(themeCheckedIndex));
        mDatas.get(2).setVal(numList.get(numCheckedIndex));
        mDatas.get(3).setVal(modeList.get(modeCheckedIndex));
        mDatas.get(4).setVal(recorderList.get(recorderCheckedIndex));
    }
}
