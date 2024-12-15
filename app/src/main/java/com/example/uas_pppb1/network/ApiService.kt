package com.example.uas_pppb1.network

import com.example.uas_pppb1.data.model.Article
import com.example.uas_pppb1.data.model.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Mendapatkan semua artikel
    @GET("{database}/{table}")
    suspend fun getAllArticles(
        @Path("database") database: String,
        @Path("table") table: String
    ): Response<List<Article>>

    // Membuat artikel baru
    @POST("{database}/{table}/create")
    suspend fun createArticle(
        @Path("database") database: String,
        @Path("table") table: String,
        @Body article: Article
    ): Response<Article>

    // Menghapus artikel berdasarkan ID
    @DELETE("{database}/{table}/delete/{id}")
    suspend fun deleteArticle(
        @Path("database") database: String,
        @Path("table") table: String,
        @Path("id") id: String
    ): Response<Void>

    // Mengupdate artikel berdasarkan ID
    @PUT("{database}/{table}/update/{id}")
    suspend fun updateArticle(
        @Path("database") database: String,
        @Path("table") table: String,
        @Path("id") id: String,
        @Body article: Article
    ): Response<Article>

    // Mendapatkan semua pengguna (untuk login)
    @GET("{database}/{table}")
    suspend fun getAllUsers(
        @Path("database") database: String,
        @Path("table") table: String
    ): Response<List<User>>

    // Registrasi pengguna baru
    @POST("{database}/{table}/register")
    suspend fun registerUser(
        @Path("database") database: String,
        @Path("table") table: String,
        @Body user: User
    ): Response<User>
}
