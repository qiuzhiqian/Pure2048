#Pure2048

这是一款纯净小巧的2048休闲游戏android版本  
配置文件会保存到/storage/emulated/0/Android/data/com.mycode.xml.pure2048/files/下面  

|文件|用途|
|-|-|
|appConfig|Pure2048的配置文件|
|appTheme|Pure2048的主题文件|

##appConfig文件介绍

|字段|取值范围|描述|
|-|-|-|
|game_music|0~1|=0表示关闭音效，=1表示开启音效|
|lens|>0|该值表示方阵每行每列的方块个数，建议>=4,<=10|
|show_style|0、1、2|表示方块显示样式，=0只显示数字，不显示颜色，=1只显示颜色不显示数字，=2数字颜色都显示|
|game_mode|保留|游戏模式，保留字段|

##appTheme文件介绍  

**注意：改字段取值是按照aRGB格式取值，即2位透明度+2位R+2位G+2位B，透明度统一使用ff不透明，否则会出现bug**  

|字段|描述|
|-|-|
|color_0|表示空白方块的颜色,比如ffc0c0c0|
|color_x|x应该是2的n次方，比如2、4、8、16等，该字段的表示对应值的方块的颜色|
|color_other|表示其他未知方块的颜色|

#预览

![预览1](https://raw.githubusercontent.com/qiuzhiqian/Pure2048/master/doc/review1.jpg)