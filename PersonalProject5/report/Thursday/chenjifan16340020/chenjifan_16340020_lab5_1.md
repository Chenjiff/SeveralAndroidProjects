# 中山大学数据科学与计算机学院本科生实验报告
## （2018年秋季学期）
| 课程名称 | 手机平台应用开发 | 任课老师 | 郑贵锋 |
| :------: | :-------------: | :------------: | :-------------: |
| 年级 | 2016级 | 专业（方向） | 软件工程 |
| 学号 | 16340020 | 姓名 | 陈吉凡 |
| 电话 | 15017239913 | Email | 1004920224@qq.com |
| 开始日期 | 2018.12.06 | 完成日期 | 2018.12.13 |

## 一、实验题目
### WEB API

---

## 二、实现内容
#### 实现一个bilibili的用户视频信息获取软件
<table>
    <tr>
        <td ><img src="/manual/images/img1.png" >打开程序主页面</td>
        <td ><img src="/manual/images/img2.png" >输入用户id，要求正整数int类型，不满足的弹Toast提示即可</td>
    </tr>
    <tr>
        <td ><img src="/manual/images/img3.png" >输入用户id，点击搜索，网络没打开则弹Toast提示网络连接失败</td>
        <td ><img src="/manual/images/img4.png" >网络打开情况下，输入用户id，不存在相应数据的弹Toast提示</td>
    </tr>
    <tr>
        <td ><img src="/manual/images/img5.png" >输入用户id = 2，点击搜索，展示图片/播放数/评论/时长/创建时间/标题/简介内容</td>
        <td ><img src="/manual/images/img6.png" >再次输入用户id = 7，接着上次结果继续展示以上内容</td>
    </tr>
</table>

* 搜索框只允许正整数int类型，不符合的需要弹Toast提示
*  当手机处于飞行模式或关闭wifi和移动数据的网络连接时，需要弹Toast提示
*  由于bilibili的API返回状态有很多，这次我们特别的限制在以下几点
    * 基础信息API接口为： `https://space.bilibili.com/ajax/top/showTop?mid=<user_id>`
    * 图片信息API接口为基础信息API返回的URL，cover字段
    * 只针对前40的用户id进行处理，即`user_id <= 40`
    * [2,7,10,19,20,24,32]都存在数据，需要正确显示
* **在图片加载出来前需要有一个加载条，不要求与加载进度同步**
* 布局和样式没有强制要求，只需要展示图片/播放数/评论/时长/创建时间/标题/简介的内容即可，可以自由发挥
* **布局需要使用到CardView和RecyclerView**
* 每个item最少使用2个CardView，布局怎样好看可以自由发挥，不发挥也行
* 不完成加分项的同学可以不显示SeekBar
* 输入框以及按钮需要一直处于顶部


---

## 三、课堂实验结果
### (1)实验截图
* 初始状态：  
![输入图片说明](https://images.gitee.com/uploads/images/2018/1221/230032_a7e99f87_2164918.png "1.png")  
* 搜索不合法：  
![输入图片说明](https://images.gitee.com/uploads/images/2018/1221/230045_8bcb5ba0_2164918.png "2.png")  
* 没有网络连接：  
![输入图片说明](https://images.gitee.com/uploads/images/2018/1221/230225_b2901741_2164918.png "3.png")
* 找不到记录：  
![输入图片说明](https://images.gitee.com/uploads/images/2018/1221/230123_3052b4e9_2164918.png "4.png")
* 正常搜索：  
![输入图片说明](https://images.gitee.com/uploads/images/2018/1221/230130_78cd46f4_2164918.png "5.png")
* 再次搜索：  
![输入图片说明](https://images.gitee.com/uploads/images/2018/1221/230140_a5e01631_2164918.png "6.png")


### (2)实验步骤以及关键代码
1.先写完UI，使用了RecycleView和CardView，前者就不多说了，下面是CardView的代码,主要是使用在RecycleView的子项中：
```
<android.support.v7.widget.CardView
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="0dp"
        app:cardCornerRadius="10dp"
        app:cardMaxElevation="10dp"
        app:cardElevation="5dp"
        app:cardPreventCornerOverlap="true"
        app:cardUseCompatPadding="true">
</CardView>
```  
2.然后获取输入内容并进行合法性判断，接着主要就是获取网络信息了，这里使用了HttpURLConnection发送请求，再对InputStream进行处理获得数据，注意使用到了RecyclerObj来接收Gson解析到的数据：    
```
URL url = new URL("https://space.bilibili.com/ajax/top/showTop?mid=" + content);
final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
InputStream inputStream = httpURLConnection.getInputStream();
byte[] bytes = new byte[2000];
int size = inputStream.read(bytes);
inputStream.close();
String jsonString = new String(bytes).substring(0, size);
final RecyclerObj recyclerObj = new Gson().fromJson(jsonString, RecyclerObj.class);
final boolean status = recyclerObj.isStatus();
final RecyclerObj.Data rdata = recyclerObj.getData();
```
```
public class RecyclerObj {
    private boolean status;
    private Data data;
    public static class Data {
        private int aid;
        private int state;
        private String cover;
        private String title;
        private String content;
        private int play;
        private String duration;
        private int video_preview;
        private String create;
        private String rec;
        private int count;
        //...
    }
    //...
}

```
3.这次我还是用了handle来处理UI更新：  
```
handler.post(new Runnable() {
      @Override
      public void run() {
         data.add(new Video(rdata.getCover(), rdata.getPlay(), rdata.getVideo_preview(), rdata.getDuration(),
         rdata.getCreate(), rdata.getTitle(), rdata.getContent()));
         recyclerView.getAdapter().notifyDataSetChanged();
      }
});
```

### (3)实验遇到的困难以及解决思路
1. 这次实验在CardView的使用上出了一些问题，大部分都是没有了解属性导致的，写前端最好多了解一下控件的属性。
2. 还有对InputStream的处理问题上，发现通过avialable()或者直接从HttpURLConnection处获得都不可行，最终只能对获得的String再进行分割，效率可能会低一些，以后参照别人的代码参照学习一下。  
---

## 五、实验思考及感想
这次实验学习了很多关于网络信息获取的知识，特别实用，以前在这方面也很薄弱，这次得到了补充，感觉很充实。

---

#### 作业要求
* 命名要求：学号_姓名_实验编号，例如12345678_张三_lab1.md
* 实验报告提交格式为md
* 实验内容不允许抄袭，我们要进行代码相似度对比。如发现抄袭，按0分处理