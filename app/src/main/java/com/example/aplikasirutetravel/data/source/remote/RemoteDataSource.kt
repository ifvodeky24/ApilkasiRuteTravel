package com.example.aplikasirutetravel.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aplikasirutetravel.data.source.remote.response.KondisiJalan
import com.example.aplikasirutetravel.data.source.remote.response.KondisiJalanResponse
import com.example.aplikasirutetravel.data.source.remote.response.Perusahaan
import com.example.aplikasirutetravel.data.source.remote.response.PerusahaanResponse
import com.example.aplikasirutetravel.data.source.remote.service.ApiConfig
import com.example.aplikasirutetravel.utils.EMPTY_DATA
import com.example.aplikasirutetravel.utils.ERROR_CONNECTION
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        val listPerusahaan = MutableLiveData<ApiResponse<List<KondisiJalan>>>()

        apiConfig.client().getAllKondisiJalan().enqueue(object : Callback<KondisiJalanResponse> {
            override fun onResponse(
                call: Call<KondisiJalanResponse>,
                response: Response<KondisiJalanResponse>
            ) {
                if (response.code() == 200) {
                    response.body()?.kondisi_jalan?.let {
                        if (it.isNotEmpty()) {
                            listPerusahaan.value = ApiResponse.success(it)
                        } else if (it.isEmpty()) {
                            listPerusahaan.value = ApiResponse.empty(EMPTY_DATA, it)
                        }
                    }

                } else {
                    response.body()?.kondisi_jalan?.let {
                        listPerusahaan.value = ApiResponse.error(ERROR_CONNECTION, it)
                    }
                }
            }

            override fun onFailure(call: Call<KondisiJalanResponse>, t: Throwable) {
                listPerusahaan.value = ApiResponse.error(ERROR_CONNECTION, null)
            }
        })

        return listPerusahaan
    }

    fun getKondisiJalanSearch(query: String): LiveData<ApiResponse<List<KondisiJalan>>> {
        val listPerusahaan = MutableLiveData<ApiResponse<List<KondisiJalan>>>()

        apiConfig.client().getKondisiJalanSearch(query).enqueue(object : Callback<KondisiJalanResponse> {
            override fun onResponse(
                call: Call<KondisiJalanResponse>,
                response: Response<KondisiJalanResponse>
            ) {
                if (response.code() == 200) {
                    response.body()?.kondisi_jalan?.let {
                        if (it.isNotEmpty()) {
                            listPerusahaan.value = ApiResponse.success(it)
                        } else if (it.isEmpty()) {
                            listPerusahaan.value = ApiResponse.empty(EMPTY_DATA, it)
                        }
                    }

                } else {
                    response.body()?.kondisi_jalan?.let {
                        listPerusahaan.value = ApiResponse.error(ERROR_CONNECTION, it)
                    }
                }
            }

            override fun onFailure(call: Call<KondisiJalanResponse>, t: Throwable) {
                listPerusahaan.value = ApiResponse.error(ERROR_CONNECTION, null)
            }
        })

        return listPerusahaan
    }
}