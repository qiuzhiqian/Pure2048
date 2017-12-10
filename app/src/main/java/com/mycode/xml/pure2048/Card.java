package com.mycode.xml.pure2048;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by xia_m on 2017/10/29/0029.
 */

public class Card extends FrameLayout {
    public Card(Context context)
    {
        super(context);

        LayoutParams lp = null;

        background = new View(getContext());
        background.setBackgroundColor(0x33ffffff);

        lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 0, 0, 0);
        addView(background,lp);

        label=new TextView(getContext());
        label.setTextSize(28);
        label.setGravity(Gravity.CENTER);

        lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        lp.setMargins(0,0,0,0);
        addView(label,lp);
    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int num,int showMask)
    {
        if(num<0)
            num=0;

        this.num=num;

        switch(showMask)
        {
            case 0:     //数字
                if(num==0)
                    label.setText("");
                else
                    label.setText(num+"");

                label.setBackgroundColor(Color.parseColor("#"+Config.NumTable[0]));
                break;
            case 1:     //颜色
                label.setText("");
                setCardColor();
                break;
            case 2:     //数字+颜色
                Log.d("MyCard","set Color and Num");
                if(num==0)
                    label.setText("");
                else
                    label.setText(num+"");

                setCardColor();
                break;
            case 3:     //图片主题
                if(num==0)
                {
                    setCardBitmap(0);
                }
                else if(num<=2097152)
                {
                    int cnt=getIndex(num);
                    setCardBitmap(cnt);
                }
                else
                {
                    setCardBitmap(Config.bmlist.size()-1);
                }
                break;
            default:
                break;
        }

    }

    private void setCardColor()
    {
        //int colorval=Color.parseColor(Config.NumTable[num]);
        //Log.d("MyCard","color="+Config.NumTable[num]);
        if(num==0)
        {
            label.setBackgroundColor(Color.parseColor("#"+Config.NumTable[0]));
        }
        else if(num<=2097152)
        {
            int cnt=getIndex(num);
            Log.d("MyCard","cnt="+cnt);
            label.setBackgroundColor(Color.parseColor("#"+Config.NumTable[cnt]));
        }
        else
        {
            label.setBackgroundColor(Color.parseColor("#"+Config.NumTable[Config.NumTable.length-1]));
        }
    }

    private void setCardBitmap(int index)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            label.setBackground(new BitmapDrawable(Config.bmlist.get(index)));
        }
        else
        {
            label.setBackgroundDrawable(new BitmapDrawable(Config.bmlist.get(index)));
        }
    }

    private int getIndex(int num)
    {
        int cnt=0;
        while(true)
        {
            num/=2;
            if(num>0)
                cnt++;
            else
                break;
        }
        return cnt;
    }

    public boolean equals(Card c)
    {
        return getNum()==c.getNum();
    }

    public TextView getLabel()
    {
        return label;
    }

    private int num;
    private TextView label;
    private View background;
}
