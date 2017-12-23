package com.mycode.xml.pure2048;

/**
 * Created by xia_m on 2017/12/20/0020.
 */

public class SettingMenu {
    private String title;
    private String val;
    private boolean switchSta;
    private int type;

    public SettingMenu()
    {
        title="title";
        val="val";
        switchSta=false;
        type=0;
    }

    public void setTitle(String s)
    {
        title=s;
    }

    public void setVal(String s)
    {
        val=s;
    }

    public void setType(int i)
    {
        type=i;
    }

    public void setSwitchSta(boolean sta){ switchSta = sta;}

    public String getTitle()
    {
        return title;
    }

    public String getVal()
    {
        return val;
    }

    public int getType()
    {
        return type;
    }
    public boolean getSwtchSta()
    {
        return switchSta;
    }
}
