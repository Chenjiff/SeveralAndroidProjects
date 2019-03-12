package com.code.chenjifff.httpapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetRequestInterface {
    @GET("/users/{user_name}/repos")
    Call<Repos[]> getRepos(@Path("user_name") String user_name);

    @GET("/repos/{user_name}/{repos_name}/issues")
    Call<Issue[]> getReposIssues(@Path("user_name") String user_name, @Path("repos_name") String repos_name);
}
