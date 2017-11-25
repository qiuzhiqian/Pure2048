package com.mycode.xml.pure2048;

import android.content.Context;
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

        if(showMask==0 || showMask==2)
        {
            if(num==0)
                label.setText("");
            else
                label.setText(num+"");
        }
        else
        {
            label.setText("");
        }

        if(showMask==1 || showMask==2)
        {
            if(num==0)
            {
                label.setBackgroundColor(Config.ColorTable[0]);
            }
            else if(num<=2048)
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
                label.setBackgroundColor(Config.ColorTable[cnt]);
            }
            else
            {
                label.setBackgroundColor(Config.ColorTable[Config.ColorTable.length-1]);
            }
        }
        else
        {
            label.setBackgroundColor(Config.ColorTable[0]);
        }


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
