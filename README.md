# Pure2048

这是一款纯净小巧的2048休闲游戏android版本  
配置文件会保存到/storage/emulated/0/Android/data/com.mycode.xml.pure2048/files/下面  

|文件|用途|
|-|-|
|appConfig|Pure2048的配置文件|
|themes/|Pure2048的主题文件夹|

## appConfig文件介绍

|字段|取值范围|描述|
|-|-|-|
|game_music|0~1|=0表示关闭音效，=1表示开启音效|
|lens|>0|该值表示方阵每行每列的方块个数，建议>=4,<=10|
|game_mode|保留|游戏模式，保留字段|

## 主题

本软件默认自带一个主题，即数字+背景色的主题  
同时本软件支持主题拓展  
主题包有三要素：  

1. 主题名
2. 主题配置
3. 主题资源  

**主题名与主题配置文件名以及主题包名必须保持一致**

基本结构如下，比如现有一个主题名为：theme_chaodai

- files/
	- appconfig
	- themes/
		- theme_chaodai/
			- theme_chaodai
			- 1_xia.png
			- 2_shang.png

theme_chaodai/是资源包文件夹，其下包含一个theme_chaodai配置文件和若干.png资源文件

theme_chaodai配置文件参考如下：

```
show_style=3

show_0=0_null.png
show_2=1_xia.png
show_4=2_shang.png
show_8=3_zhou.png
show_16=4_chunqiu.png
show_32=5_zhanguo.png
show_64=6_qin.png
show_128=7_han.png
show_256=8_sanguo.png
show_512=9_jin.png
show_1024=10_nanchao.png
show_2048=11_beichao.png
show_4096=12_sui.png
show_8192=13_tang.png
show_16384=14_wudai.png
show_32768=15_shiguo.png
show_65536=16_song.png
show_131072=17_liao.png
show_262144=18_jin.png
show_524288=19_yuan.png
show_1048676=20_ming.png
show_2097152=21_qing.png
show_other=0_null.png

```

|字段|描述|
|-|-|
|show_style|0、1、2、3|表示方块显示样式，=0只显示数字，不显示颜色，=1只显示颜色不显示数字，=2数字颜色都显示，=3显示图片|
|show_0|表示空白方块的颜色,比如ffc0c0c0|
|show_x|x应该是2的n次方，比如2、4、8、16等，该字段的表示对应值的方块的颜色或者图片|
|show_other|表示其他未知方块的颜色或者图片|

**注意：**

**1、数字颜色主题的色值按照aRGB格式取值，即2位透明度+2位R+2位G+2位B，透明度统一使用ff不透明，否则会出现bug**

**2、数字颜色主题不需要资源文件，只需要一个配置文件即可**



# 预览

## 设置预览

![设置预览](https://raw.githubusercontent.com/qiuzhiqian/Pure2048/master/doc/review1.png)

## 设置1

![设置1](https://raw.githubusercontent.com/qiuzhiqian/Pure2048/master/doc/review2.png)

## 设置2

![设置2](https://raw.githubusercontent.com/qiuzhiqian/Pure2048/master/doc/review3.png)

## 设置3

![设置3](https://raw.githubusercontent.com/qiuzhiqian/Pure2048/master/doc/review4.png)

## 设置4

![设置4](https://raw.githubusercontent.com/qiuzhiqian/Pure2048/master/doc/review5.png)

## 使用默认主题，4x4

![预览1](https://raw.githubusercontent.com/qiuzhiqian/Pure2048/master/doc/review6.png)

## 使用默认主题，5x5

![预览2](https://raw.githubusercontent.com/qiuzhiqian/Pure2048/master/doc/review7.png)

## 使用拓展朝代主题，5x5

![预览2](https://raw.githubusercontent.com/qiuzhiqian/Pure2048/master/doc/review8.png)