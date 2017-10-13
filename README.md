 ### 一个模仿薄荷的标尺控件

 ## 效果
 ![](/ruler.gif)

 ## 使用示例

 在布局中加入
```
    <liyafeng.com.ruler.SimpleRulerView
        android:id="@+id/rv_ruler"
        android:layout_width="match_parent"
        android:layout_height="200dp"/>
```
 在代码中加入

 ```
        SimpleRulerView rv_ruler = (SimpleRulerView) findViewById(R.id.rv_ruler);
        //配置基本参数 第一个值为一个刻度的长度，第二个参数是单位，第三个是字体的颜色
        rv_ruler.config(0.1f, "kg", Color.parseColor("#1aa260"));
        //设置滑动范围
        rv_ruler.setRange(30,100);
 		//设置初始位置，默认为最小值
        rv_ruler.scrollTo(50);

 ```

 当然还支持平滑滑动

 ```
        rv_ruler.smoothScrollTo(80);
 ```