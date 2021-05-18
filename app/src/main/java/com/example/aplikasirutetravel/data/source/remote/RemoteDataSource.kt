package com.example.aplikasirutetravel.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aplikasirutetravel.data.source.local.entity.KondisiJalanEntity
import com.example.aplikasirutetravel.data.source.remote.response.*
import com.example.aplikasirutetravel.data.source.remote.service.ApiConfig
import com.example.aplikasirutetravel.utils.EMPTY_DATA
import com.example.aplikasirutetravel.utils.ERROR_CONNECTION
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class RemoteDataSource(private val apiConfig: ApiConfig) {

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(apiConfig: ApiConfig): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(apiConfig)
            }
    }

    fun getAllPerusahaan(): LiveData<ApiResponse<List<Perusahaan>>> {
        val listPerusahaan = MutableLiveData<ApiResponse<List<Perusahaan>>>()

        apiConfig.client().getAllPerusahaan().enqueue(object : Callback<PerusahaanResponse> {
            override fun onResponse(
                call: Call<PerusahaanResponse>,
                response: Response<PerusahaanResponse>
            ) {
                if (response.code() == 200) {
                    response.body()?.perusahaan?.let {
                        if (it.isNotEmpty()) {
                            Timber.d("oiii ${response.body()?.perusahaan}")
                            listPerusahaan.value = ApiResponse.success(it)
                        } else if (it.isEmpty()) {
                            listPerusahaan.value = ApiResponse.empty(EMPTY_DATA, it)
                        }
                    }

                } else {
                    response.body()?.perusahaan?.let {
                        listPerusahaan.value = ApiResponse.error(ERROR_CONNECTION, it)
                    }
                }
            }

            override fun onFailure(call: Call<PerusahaanResponse>, t: Throwable) {
                listPerusahaan.value = ApiResponse.error(ERROR_CONNECTION, null)
            }
        })

        return listPerusahaan
    }

    fun getPerusahaanSearch(query: String): LiveData<ApiResponse<List<Perusahaan>>> {
        val listPerusahaan = MutableLiveData<ApiResponse<List<Perusahaan>>>()

        apiConfig.client().getPerusahaanSearch(query).enqueue(object : Callback<PerusahaanResponse> {
            override fun onResponse(
                call: Call<PerusahaanResponse>,
                response: Response<PerusahaanResponse>
            ) {
                if (response.code() == 200) {
                    response.body()?.perusahaan?.let {
                        if (it.isNotEmpty()) {
                            Timber.d("oiii ${response.body()?.perusahaan}")
                            listPerusahaan.value = ApiResponse.success(it)
                        } else if (it.isEmpty()) {
                            listPerusahaan.value = ApiResponse.empty(EMPTY_DATA, it)
                        }
                    }

                } else {
                    response.body()?.perusahaan?.let {
                        listPerusahaan.value = ApiResponse.error(ERROR_CONNECTION, it)
                    }
                }
            }

            override fun onFailure(call: Call<PerusahaanResponse>, t: Throwable) {
                listPerusahaan.value = ApiResponse.error(ERROR_CONNECTION, null)
            }
        })

        return listPerusahaan
    }

    fun getPerusahaanById(id_perusahaan: String): LiveData<ApiResponse<List<Perusahaan>>> {
        val listPerusahaan = MutableLiveData<ApiResponse<List<Perusahaan>>>()

        apiConfig.client().getPerusahaanById(id_perusahaan)
            .enqueue(object : Callback<PerusahaanResponse> {
                override fun onResponse(
                    call: Call<PerusahaanResponse>,
                    response: Response<PerusahaanResponse>
                ) {
                    if (response.code() == 200) {
                        response.body()?.perusahaan?.let {
                            if (it.isNotEmpty()) {
                                listPerusahaan.value = ApiResponse.success(it)
                            } else if (it.isEmpty()) {
                                listPerusahaan.value = ApiResponse.empty(EMPTY_DATA, it)
                            }
                        }

                    } else {
                        response.body()?.perusahaan?.let {
                            listPerusahaan.value = ApiResponse.error(ERROR_CONNECTION, it)
                        }
                    }
                }

                override fun onFailure(call: Call<PerusahaanResponse>, t: Throwable) {
                    listPerusahaan.value = ApiResponse.error(ERROR_CONNECTION, null)
                }
            })

        return listPerusahaan
    }

    fun getAllKondisiJalan(): LiveData<ApiResponse<List<KondisiJalan>>> {
        val listKondisiJalan = MutableLiveData<ApiResponse<List<KondisiJalan>>>()

        apiConfig.client().getAllKondisiJalan().enqueue(object : Callback<KondisiJalanResponse> {
            override fun onResponse(
                call: Call<KondisiJalanResponse>,
                response: Response<KondisiJalanResponse>
            ) {
                if (response.code() == 200) {
                    response.body()?.kondisi_jalan?.let {
                        if (it.isNotEmpty()) {
                            listKondisiJalan.value = ApiResponse.success(it)
                        } else if (it.isEmpty()) {
                            listKondisiJalan.value = ApiResponse.empty(EMPTY_DATA, it)
                        }
                    }

                } else {
                    response.body()?.kondisi_jalan?.let {
                        listKondisiJalan.value = ApiResponse.error(ERROR_CONNECTION, it)
                    }
                }
            }

            override fun onFailure(call: Call<KondisiJalanResponse>, t: Throwable) {
                listKondisiJalan.value = ApiResponse.error(ERROR_CONNECTION, null)
            }
        })

        return listKondisiJalan
    }

    fun getKondisiJalanSearch(query: String): LiveData<ApiResponse<List<KondisiJalanEntity>>> {
        val listKondisiJalan = MutableLiveData<ApiResponse<List<KondisiJalanEntity>>>()

        apiConfig.client().getKondisiJalanSearch(query)
            .enqueue(object : Callback<KondisiJalanSearchResponse> {
                override fun onResponse(
                    call: Call<KondisiJalanSearchResponse>,
                    response: Response<KondisiJalanSearchResponse>
                ) {
                    if (response.code() == 200) {
                        response.body()?.kondisi_jalan?.let {
                            if (it.isNotEmpty()) {
                                listKondisiJalan.value = ApiResponse.success(it)
                            } else if (it.isEmpty()) {
                                listKondisiJalan.value = ApiResponse.empty(EMPTY_DATA, it)
                            }
                        }

                    } else {
                        response.body()?.kondisi_jalan?.let {
                            listKondisiJalan.value = ApiResponse.error(ERROR_CONNECTION, it)
                        }
                    }
                }

                override fun onFailure(call: Call<KondisiJalanSearchResponse>, t: Throwable) {
                    listKondisiJalan.value = ApiResponse.error(ERROR_CONNECTION, null)
                }
            })

        return listKondisiJalan
    }

    fun getKondisiJalanById(id_kondisi_jalan: String): LiveData<ApiResponse<List<KondisiJalan>>> {
        val listKondisiJalan = MutableLiveData<ApiResponse<List<KondisiJalan>>>()

        apiConfig.client().getKondisiJalanById(id_kondisi_jalan)
            .enqueue(object : Callback<KondisiJalanResponse> {
                override fun onResponse(
                    call: Call<KondisiJalanResponse>,
                    response: Response<KondisiJalanResponse>
                ) {
                    if (response.code() == 200) {
                        response.body()?.kondisi_jalan?.let {
                            if (it.isNotEmpty()) {
                                listKondisiJalan.value = ApiResponse.success(it)
                            } else if (it.isEmpty()) {
                                listKondisiJalan.value = ApiResponse.empty(EMPTY_DATA, it)
                            }
                        }
                    } else {
                        response.body()?.kondisi_jalan?.let {
                            listKondisiJalan.value = ApiResponse.error(ERROR_CONNECTION, it)
                        }
                    }
                }

                override fun onFailure(call: Call<KondisiJalanResponse>, t: Throwable) {
                    listKondisiJalan.value = ApiResponse.error(ERROR_CONNECTION, null)
                }
            })

        return listKondisiJalan
    }

    fun getAllTrayek(): LiveData<ApiResponse<List<Trayek>>> {
        val listTrayek = MutableLiveData<ApiResponse<List<Trayek>>>()

        apiConfig.client().getAllTrayek().enqueue(object : Callback<TrayekResponse> {
            override fun onResponse(
                call: Call<TrayekResponse>,
                response: Response<TrayekResponse>
            ) {
                if (response.code() == 200) {
                    response.body()?.trayek?.let {
                        if (it.isNotEmpty()) {
                            listTrayek.value = ApiResponse.success(it)
                        } else if (it.isEmpty()) {
                            listTrayek.value = ApiResponse.empty(EMPTY_DATA, it)
                        }
                    }

                } else {
                    response.body()?.trayek?.let {
                        listTrayek.value = ApiResponse.error(ERROR_CONNECTION, it)
                    }
                }
            }

            override fun onFailure(call: Call<TrayekResponse>, t: Throwable) {
                listTrayek.value = ApiResponse.error(ERROR_CONNECTION, null)
            }
        })

        return listTrayek
    }
}