package com.mycode.xml.pure2048;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xia_m on 2017/11/5/0005.
 */

public class Config {

    public static int game_lens=4;
    public static String NumTable[]={
            "ffc0c0c0",     //0 60, 63, 65
            "ff9bde7a",     //other
            "fffec463",     //2
            "fff36b49",     //4
            "ffdff711",     //8
            "ff8af90f",     //16
            "ff04cc36",     //32
            "ff28f2c0",     //64
            "ff4daef7",     //128
            "ff0d48f2",     //256
            "ff6c82f9",     //512
            "ff8135f2",     //1024
            "ffe825ed",     //2048
            "ffed1455",     //4096
            "ffbe872c",     //8192
            "ff659326",     //16384
            "ff1d6928",     //32768
            "ff249295",     //65536
            "ff226097",     //131072
            "ff3f3cb7",     //262144
            "ff5f318c",     //524288
            "ff732b4b",     //1048676
            "ffc56775",     //2097152
    };

    //public static void initColorList()
    //{
    //
    //}

    public static void initBpList(String url)
    {
        int cnt=colorList.size();
        Bitmap tempbp=null;
        bmlist.clear();
        for(int i=0;i<cnt;i++)
        {
            tempbp=getLocalBitmap(url+"/"+colorList.get(i));
            bmlist.add(tempbp);
        }
    }

    public static List<String> colorList=new ArrayList<String>();
    public static List<Bitmap> bmlist=new ArrayList<Bitmap>();

    public static int show_style=2;
    public static int game_mode=0;
    public static int game_music=1;
    public static int configVer[]={1,0,0};

    public static String game_theme="num_color";

    public static String toColorStr(int val)
    {
        String colorStr=Integer.toHexString(val);
        int len=colorStr.length();
        if(len<8)
        {
            for(int i=0;i<8-len;i++)
            {
                colorStr="0"+colorStr;
            }
        }
        return colorStr;
    }

    public static Bitmap getLocalBitmap(String url)
    {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
