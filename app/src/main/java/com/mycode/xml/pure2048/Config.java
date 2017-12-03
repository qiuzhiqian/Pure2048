package com.mycode.xml.pure2048;

/**
 * Created by xia_m on 2017/11/5/0005.
 */

public class Config {
    public static int Cnt=4;
    public static int ColorTable[]={
            0xffc0c0c0,     //0 60, 63, 65
            0xffeee4da,     //2
            0xffff66a3,     //4
            0xfff2b179,     //8
            0xfff59563,     //16
            0xff66b2ff,     //32
            0xfff65e3b,     //64
            0xffedcf72,     //128
            0xff33cc33,     //256
            0xff40ff00,     //512
            0xffff00ff,     //1024
            0xffd926d9,     //2048
            0xff737373,     //其他
    };
    public static int show_style=2;
    public static int game_mode=0;
    public static int game_music=1;
    public static int configVer[]={1,0,0};

    public static String game_theme="themeConfig";

    public static String PicTable[]={
            "pic_0",
            "pic_2",
            "pic_4",
            "pic_8",
            "pic_16",
            "pic_32",
            "pic_64",
            "pic_128",
            "pic_256",
            "pic_512",
            "pic_1024",
            "pic_2048",
            "pic_other",
    };

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

}
