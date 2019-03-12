# 中山大学数据科学与计算机学院本科生实验报告
## （2018年秋季学期）
| 课程名称 | 手机平台应用开发 | 任课老师 | 郑贵锋 |
| :------: | :-------------: | :------------: | :-------------: |
| 年级 | 2016级 | 专业（方向） | 软件工程 |
| 学号 | 16340020 | 姓名 | 陈吉凡 |
| 电话 | 15017239913 | Email | 1004920224@qq.com |
| 开始日期 | 2018.12.13 | 完成日期 | 2018.12.20 |

## 一、实验题目
### WEB API

---

## 二、实现内容
#### 实现一个github用户repos以及issues应用
<table>
    <tr>
        <td ><img src="/manual/images/img9.png" >主界面有两个跳转按钮分别对应两次作业</td>
        <td ><img src="/manual/images/img10.png" >github界面，输入用户名搜索该用户所有可提交issue的repo，每个item可点击</td>
    </tr>
    <tr>
        <td ><img src="/manual/images/img11.png" >repo详情界面，显示该repo所有的issues</td>
        <td ><img src="/manual/images/img12.png" >加分项：在该用户的该repo下增加一条issue，输入title和body即可</td>
    </tr>
</table>

* 教程位于`./manual/tutorial_retrofit.md`
* 每次点击搜索按钮都会清空上次搜索结果再进行新一轮的搜索
* 获取repos时需要处理以下异常：HTTP 404 以及 用户没有任何repo
* 只显示 has_issues = true 的repo（即fork的他人的项目不会显示）
* repo显示的样式自由发挥，显示的内容可以自由增加（不能减少）
* repo的item可以点击跳转至下一界面
* 该repo不存在任何issue时需要弹Toast提示
* 不完成加分项的同学只需要显示所有issues即可，样式自由发挥，内容可以增加

---

## 三、课堂实验结果
### (1)实验截图
* 初始页面：  
![输入图片说明](https://images.gitee.com/uploads/images/2018/1221/232929_9992f282_2164918.png "11.png")
* 进入页面：  
![输入图片说明](https://images.gitee.com/uploads/images/2018/1221/233019_d7254e5a_2164918.png "22.png")
* 搜索用户名：  
![输入图片说明](https://images.gitee.com/uploads/images/2018/1221/233436_1a0e8478_2164918.png "33.png")
* 点击第一个问题数为0的仓库：  
![输入图片说明](https://images.gitee.com/uploads/images/2018/1229/141521_fd5973fb_2164918.png "2.png")  
* 搜索一个未拥有仓库的用户：  
![输入图片说明](https://images.gitee.com/uploads/images/2018/1229/141535_b48fb157_2164918.png "3.png")  
* 搜索一个不存在的用户：  
![输入图片说明](https://images.gitee.com/uploads/images/2018/1229/141545_ead8ea42_2164918.png "在这里输入图片标题")  
* 再次搜索：  
![输入图片说明](https://images.gitee.com/uploads/images/2018/1229/141638_4bdb2963_2164918.png "5.png")
* 点击第一个仓库，显示问题：    
![输入图片说明](https://images.gitee.com/uploads/images/2018/1229/141654_0273ef18_2164918.png "6.png")
---

### (2)实验步骤以及关键代码
1.先写完UI，大部分代码可以使用上次实验的，不再赘述了。

2.使用Retrofit的进行网络访问，建完存储数据的类后，再建一个接口类用于网络请求，两个函数分别为用于访问仓库和访问issue的,其中使用了数组，因为返回的是json数组：
```
public interface GetRequestInterface {
    @GET("/users/{user_name}/repos")
    Call<Repos[]> getRepos(@Path("user_name") String user_name);

    @GET("/repos/{user_name}/{repos_name}/issues")
    Call<Issue[]> getReposIssues(@Path("user_name") String user_name, @Path("repos_name") String repos_name);
}
```
然后是核心使用代码（访问仓库issue的基本一致，注意其中对异常的处理，用于判断404错误：：
```
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                GetRequestInterface request = retrofit.create(GetRequestInterface.class);
                Call<Repos[]> call = request.getRepos(currentUser);
                call.enqueue(new Callback<Repos[]>() {
                    //请求成功时
                    @Override
                    public void onResponse(Call<Repos[]> call, Response<Repos[]> response) {
                        try {
                            List<Repos> list = Arrays.asList(response.body());
                            data.clear();
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).isHas_issues()) {
                                    data.add(list.get(i));
                                }
                            }
                            if (data.isEmpty()) {
                                Toast.makeText(GithubActivity.this, "该用户没有任何仓库", Toast.LENGTH_SHORT).show();
                            }
                            Observable.create(new Observable.OnSubscribe<Object>() {
                                @Override
                                public void call(Subscriber<? super Object> subscriber) {
                                    subscriber.onNext("");
                                    subscriber.onCompleted();
                                }
                            }).subscribe(new Action1<Object>() {
                                @Override
                                public void call(Object obj) {
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                }
                            });
                        }
                        catch (NullPointerException e) {
                            Toast.makeText(GithubActivity.this, "404错误，找不到该用户", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //请求失败时
                    @Override
                    public void onFailure(Call<Repos[]> call, Throwable throwable) {
                        Toast.makeText(GithubActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
```

### (3)实验遇到的困难以及解决思路
1. 对Retrofit的使用以前比较少见，要对照着课件等资料慢慢理解使用。  
2. new Retrofit.Builder().baseUrl("");中，必须要以/结尾，否则会抛出异常。
3. 对gson的使用，其实gson很方便的，要解析为数组，只要把读取类型改为数组即可
---

## 五、实验思考及感想
这次实验其实主要在理解上，理解完课件的内容代码量其实挺少，大部分可以重用上次实验的，主要学习了REST和Retrofit 2.0的使用，很实用的工具。

---

#### 作业要求
* 命名要求：学号_姓名_实验编号，例如12345678_张三_lab1.md
* 实验报告提交格式为md
* 实验内容不允许抄袭，我们要进行代码相似度对比。如发现抄袭，按0分处理