# ��ɽ��ѧ���ݿ�ѧ������ѧԺ������ʵ�鱨��
## ��2018���＾ѧ�ڣ�
| �γ����� | �ֻ�ƽ̨Ӧ�ÿ��� | �ο���ʦ | ֣��� |
| :------: | :-------------: | :------------: | :-------------: |
| �꼶 | 2016�� | רҵ������ | ������� |
| ѧ�� | 16340020 | ���� | �¼��� |
| �绰 | 15017239913 | Email | 1004920224@qq.com |
| ��ʼ���� | 2018.12.13 | ������� | 2018.12.20 |

## һ��ʵ����Ŀ
### WEB API

---

## ����ʵ������
#### ʵ��һ��github�û�repos�Լ�issuesӦ��
<table>
    <tr>
        <td ><img src="/manual/images/img9.png" >��������������ת��ť�ֱ��Ӧ������ҵ</td>
        <td ><img src="/manual/images/img10.png" >github���棬�����û����������û����п��ύissue��repo��ÿ��item�ɵ��</td>
    </tr>
    <tr>
        <td ><img src="/manual/images/img11.png" >repo������棬��ʾ��repo���е�issues</td>
        <td ><img src="/manual/images/img12.png" >�ӷ���ڸ��û��ĸ�repo������һ��issue������title��body����</td>
    </tr>
</table>

* �̳�λ��`./manual/tutorial_retrofit.md`
* ÿ�ε��������ť��������ϴ���������ٽ�����һ�ֵ�����
* ��ȡreposʱ��Ҫ���������쳣��HTTP 404 �Լ� �û�û���κ�repo
* ֻ��ʾ has_issues = true ��repo����fork�����˵���Ŀ������ʾ��
* repo��ʾ����ʽ���ɷ��ӣ���ʾ�����ݿ����������ӣ����ܼ��٣�
* repo��item���Ե����ת����һ����
* ��repo�������κ�issueʱ��Ҫ��Toast��ʾ
* ����ɼӷ����ͬѧֻ��Ҫ��ʾ����issues���ɣ���ʽ���ɷ��ӣ����ݿ�������

---

## ��������ʵ����
### (1)ʵ���ͼ
* ��ʼҳ�棺  
![����ͼƬ˵��](https://images.gitee.com/uploads/images/2018/1221/232929_9992f282_2164918.png "11.png")
* ����ҳ�棺  
![����ͼƬ˵��](https://images.gitee.com/uploads/images/2018/1221/233019_d7254e5a_2164918.png "22.png")
* �����û�����  
![����ͼƬ˵��](https://images.gitee.com/uploads/images/2018/1221/233436_1a0e8478_2164918.png "33.png")
* �����һ��������Ϊ0�Ĳֿ⣺  
![����ͼƬ˵��](https://images.gitee.com/uploads/images/2018/1229/141521_fd5973fb_2164918.png "2.png")  
* ����һ��δӵ�вֿ���û���  
![����ͼƬ˵��](https://images.gitee.com/uploads/images/2018/1229/141535_b48fb157_2164918.png "3.png")  
* ����һ�������ڵ��û���  
![����ͼƬ˵��](https://images.gitee.com/uploads/images/2018/1229/141545_ead8ea42_2164918.png "����������ͼƬ����")  
* �ٴ�������  
![����ͼƬ˵��](https://images.gitee.com/uploads/images/2018/1229/141638_4bdb2963_2164918.png "5.png")
* �����һ���ֿ⣬��ʾ���⣺    
![����ͼƬ˵��](https://images.gitee.com/uploads/images/2018/1229/141654_0273ef18_2164918.png "6.png")
---

### (2)ʵ�鲽���Լ��ؼ�����
1.��д��UI���󲿷ִ������ʹ���ϴ�ʵ��ģ�����׸���ˡ�

2.ʹ��Retrofit�Ľ���������ʣ�����洢���ݵ�����ٽ�һ���ӿ������������������������ֱ�Ϊ���ڷ��ʲֿ�ͷ���issue��,����ʹ�������飬��Ϊ���ص���json���飺
```
public interface GetRequestInterface {
    @GET("/users/{user_name}/repos")
    Call<Repos[]> getRepos(@Path("user_name") String user_name);

    @GET("/repos/{user_name}/{repos_name}/issues")
    Call<Issue[]> getReposIssues(@Path("user_name") String user_name, @Path("repos_name") String repos_name);
}
```
Ȼ���Ǻ���ʹ�ô��루���ʲֿ�issue�Ļ���һ�£�ע�����ж��쳣�Ĵ��������ж�404���󣺣�
```
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.github.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                GetRequestInterface request = retrofit.create(GetRequestInterface.class);
                Call<Repos[]> call = request.getRepos(currentUser);
                call.enqueue(new Callback<Repos[]>() {
                    //����ɹ�ʱ
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
                                Toast.makeText(GithubActivity.this, "���û�û���κβֿ�", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(GithubActivity.this, "404�����Ҳ������û�", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //����ʧ��ʱ
                    @Override
                    public void onFailure(Call<Repos[]> call, Throwable throwable) {
                        Toast.makeText(GithubActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
                    }
                });
```

### (3)ʵ�������������Լ����˼·
1. ��Retrofit��ʹ����ǰ�Ƚ��ټ���Ҫ�����ſμ��������������ʹ�á�  
2. new Retrofit.Builder().baseUrl("");�У�����Ҫ��/��β��������׳��쳣��
3. ��gson��ʹ�ã���ʵgson�ܷ���ģ�Ҫ����Ϊ���飬ֻҪ�Ѷ�ȡ���͸�Ϊ���鼴��
---

## �塢ʵ��˼��������
���ʵ����ʵ��Ҫ������ϣ������μ������ݴ�������ʵͦ�٣��󲿷ֿ��������ϴ�ʵ��ģ���Ҫѧϰ��REST��Retrofit 2.0��ʹ�ã���ʵ�õĹ��ߡ�

---

#### ��ҵҪ��
* ����Ҫ��ѧ��_����_ʵ���ţ�����12345678_����_lab1.md
* ʵ�鱨���ύ��ʽΪmd
* ʵ�����ݲ�����Ϯ������Ҫ���д������ƶȶԱȡ��緢�ֳ�Ϯ����0�ִ���