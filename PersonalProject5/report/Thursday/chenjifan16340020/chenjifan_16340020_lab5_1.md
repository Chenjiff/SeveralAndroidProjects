# ��ɽ��ѧ���ݿ�ѧ������ѧԺ������ʵ�鱨��
## ��2018���＾ѧ�ڣ�
| �γ����� | �ֻ�ƽ̨Ӧ�ÿ��� | �ο���ʦ | ֣��� |
| :------: | :-------------: | :------------: | :-------------: |
| �꼶 | 2016�� | רҵ������ | ������� |
| ѧ�� | 16340020 | ���� | �¼��� |
| �绰 | 15017239913 | Email | 1004920224@qq.com |
| ��ʼ���� | 2018.12.06 | ������� | 2018.12.13 |

## һ��ʵ����Ŀ
### WEB API

---

## ����ʵ������
#### ʵ��һ��bilibili���û���Ƶ��Ϣ��ȡ���
<table>
    <tr>
        <td ><img src="/manual/images/img1.png" >�򿪳�����ҳ��</td>
        <td ><img src="/manual/images/img2.png" >�����û�id��Ҫ��������int���ͣ�������ĵ�Toast��ʾ����</td>
    </tr>
    <tr>
        <td ><img src="/manual/images/img3.png" >�����û�id���������������û����Toast��ʾ��������ʧ��</td>
        <td ><img src="/manual/images/img4.png" >���������£������û�id����������Ӧ���ݵĵ�Toast��ʾ</td>
    </tr>
    <tr>
        <td ><img src="/manual/images/img5.png" >�����û�id = 2�����������չʾͼƬ/������/����/ʱ��/����ʱ��/����/�������</td>
        <td ><img src="/manual/images/img6.png" >�ٴ������û�id = 7�������ϴν������չʾ��������</td>
    </tr>
</table>

* ������ֻ����������int���ͣ������ϵ���Ҫ��Toast��ʾ
*  ���ֻ����ڷ���ģʽ��ر�wifi���ƶ����ݵ���������ʱ����Ҫ��Toast��ʾ
*  ����bilibili��API����״̬�кܶ࣬��������ر�����������¼���
    * ������ϢAPI�ӿ�Ϊ�� `https://space.bilibili.com/ajax/top/showTop?mid=<user_id>`
    * ͼƬ��ϢAPI�ӿ�Ϊ������ϢAPI���ص�URL��cover�ֶ�
    * ֻ���ǰ40���û�id���д�����`user_id <= 40`
    * [2,7,10,19,20,24,32]���������ݣ���Ҫ��ȷ��ʾ
* **��ͼƬ���س���ǰ��Ҫ��һ������������Ҫ������ؽ���ͬ��**
* ���ֺ���ʽû��ǿ��Ҫ��ֻ��ҪչʾͼƬ/������/����/ʱ��/����ʱ��/����/�������ݼ��ɣ��������ɷ���
* **������Ҫʹ�õ�CardView��RecyclerView**
* ÿ��item����ʹ��2��CardView�����������ÿ��������ɷ��ӣ�������Ҳ��
* ����ɼӷ����ͬѧ���Բ���ʾSeekBar
* ������Լ���ť��Ҫһֱ���ڶ���


---

## ��������ʵ����
### (1)ʵ���ͼ
* ��ʼ״̬��  
![����ͼƬ˵��](https://images.gitee.com/uploads/images/2018/1221/230032_a7e99f87_2164918.png "1.png")  
* �������Ϸ���  
![����ͼƬ˵��](https://images.gitee.com/uploads/images/2018/1221/230045_8bcb5ba0_2164918.png "2.png")  
* û���������ӣ�  
![����ͼƬ˵��](https://images.gitee.com/uploads/images/2018/1221/230225_b2901741_2164918.png "3.png")
* �Ҳ�����¼��  
![����ͼƬ˵��](https://images.gitee.com/uploads/images/2018/1221/230123_3052b4e9_2164918.png "4.png")
* ����������  
![����ͼƬ˵��](https://images.gitee.com/uploads/images/2018/1221/230130_78cd46f4_2164918.png "5.png")
* �ٴ�������  
![����ͼƬ˵��](https://images.gitee.com/uploads/images/2018/1221/230140_a5e01631_2164918.png "6.png")


### (2)ʵ�鲽���Լ��ؼ�����
1.��д��UI��ʹ����RecycleView��CardView��ǰ�߾Ͳ���˵�ˣ�������CardView�Ĵ���,��Ҫ��ʹ����RecycleView�������У�
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
2.Ȼ���ȡ�������ݲ����кϷ����жϣ�������Ҫ���ǻ�ȡ������Ϣ�ˣ�����ʹ����HttpURLConnection���������ٶ�InputStream���д��������ݣ�ע��ʹ�õ���RecyclerObj������Gson�����������ݣ�    
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
3.����һ�������handle������UI���£�  
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

### (3)ʵ�������������Լ����˼·
1. ���ʵ����CardView��ʹ���ϳ���һЩ���⣬�󲿷ֶ���û���˽����Ե��µģ�дǰ����ö��˽�һ�¿ؼ������ԡ�
2. ���ж�InputStream�Ĵ��������ϣ�����ͨ��avialable()����ֱ�Ӵ�HttpURLConnection����ö������У�����ֻ�ܶԻ�õ�String�ٽ��зָЧ�ʿ��ܻ��һЩ���Ժ���ձ��˵Ĵ������ѧϰһ�¡�  
---

## �塢ʵ��˼��������
���ʵ��ѧϰ�˺ܶ����������Ϣ��ȡ��֪ʶ���ر�ʵ�ã���ǰ���ⷽ��Ҳ�ܱ�������εõ��˲��䣬�о��ܳ�ʵ��

---

#### ��ҵҪ��
* ����Ҫ��ѧ��_����_ʵ���ţ�����12345678_����_lab1.md
* ʵ�鱨���ύ��ʽΪmd
* ʵ�����ݲ�����Ϯ������Ҫ���д������ƶȶԱȡ��緢�ֳ�Ϯ����0�ִ���