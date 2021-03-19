package com.example.aplikasirutetravel.data

import androidx.lifecycle.LiveData
import com.example.aplikasirutetravel.data.source.local.LocalDataSource
import com.example.aplikasirutetravel.data.source.local.entity.KondisiJalanEntity
import com.example.aplikasirutetravel.data.source.local.entity.PerusahaanEntity
import com.example.aplikasirutetravel.data.source.remote.ApiResponse
import com.example.aplikasirutetravel.data.source.remote.RemoteDataSource
import com.example.aplikasirutetravel.data.source.remote.response.KondisiJalan
import com.example.aplikasirutetravel.data.source.remote.response.Perusahaan
import com.example.aplikasirutetravel.utils.AppExecutors
import com.example.aplikasirutetravel.vo.Resource

class TravelRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : TravelDataSource {

    companion object {
        @Volatile
        private var instance: TravelRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
            appExecutors: AppExecutors
        ): TravelRepository =
            instance ?: synchronized(this) {
                instance ?: TravelRepository(remoteData, localData, appExecutors)
            }
    }

    override fun getAllPerusahaan(): LiveData<Resource<List<PerusahaanEntity>>> {
        return object :
            NetworkBoundResource<List<PerusahaanEntity>, List<Perusahaan>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<PerusahaanEntity>> =
                localDataSource.getAllPerusahaan()

            override fun shouldFetch(data: List<PerusahaanEntity>?): Boolean =
                data == null || data.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<Perusahaan>>> =
                remoteDataSource.getAllPerusahaan()

            public override fun saveCallResult(data: List<Perusahaan>) {
                val perusahaanList = ArrayList<PerusahaanEntity>()
                for (response in data) {
                    val perusahaan = PerusahaanEntity(
                        response.id_perusahaan,
                        response.alamat_perusahaan,
                        response.asal,
                        response.created_at,
                        response.facebook,
                        response.foto,
                        response.grid_rute,
                        response.id_jadwal,
                        response.id_trayek,
                        response.instagram,
                        response.nama_perusahaan,
                        response.nama_trayek,
                        response.nomor_handphone,
                        response.pimpinan,
                        response.status,
                        response.tujuan,
                        response.updated_at,
                        response.website,
                    )
                    perusahaanList.add(perusahaan)
                }

                localDataSource.insertPerusahaan(perusahaanList)
            }
        }.asLiveData()
    }

    override fun getPerusahaanById(id_perusahaan: String): LiveData<Resource<PerusahaanEntity>> {
        TODO("Not yet implemented")
    }

    override fun getAllKondisiJalan(): LiveData<Resource<List<KondisiJalanEntity>>> {
        return object :
            NetworkBoundResource<List<KondisiJalanEntity>, List<KondisiJalan>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<KondisiJalanEntity>> =
                localDataSource.getAllKondisiJalan()

            override fun shouldFetch(data: List<KondisiJalanEntity>?): Boolean =
                true

            override fun createCall(): LiveData<ApiResponse<List<KondisiJalan>>> =
                remoteDataSource.getAllKondisiJalan()

            override fun saveCallResult(data: List<KondisiJalan>) {
                val kondisiJalanList = ArrayList<KondisiJalanEntity>()
                for (response in data) {
                    val kondisiJalan = KondisiJalanEntity(
                        response.id_kondisi_jalan,
                        response.created_at,
                        response.deskripsi,
                        response.foto,
                        response.latitude,
                        response.longitude,
                        response.nama_lokasi,
                        response.tanggal,
                        response.updated_at
                    )
                    kondisiJalanList.add(kondisiJalan)
                }

                localDataSource.insertKondisiJalan(kondisiJalanList)
            }
        }.asLiveData()
    }

    override fun getAllKondisiJalanSearch(query: String): LiveData<Resource<List<KondisiJalanEntity>>> {
        return object :
            NetworkBoundResource<List<KondisiJalanEntity>, List<KondisiJalan>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<KondisiJalanEntity>> =
                localDataSource.getAllKondisiJalanSearch(query)

            override fun shouldFetch(data: List<KondisiJalanEntity>?): Boolean =
                data == null || data.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<KondisiJalan>>> =
                remoteDataSource.getKondisiJalanSearch(query)

            override fun saveCallResult(data: List<KondisiJalan>) {
                val kondisiJalanList = ArrayList<KondisiJalanEntity>()
                for (response in data) {
                    val kondisiJalan = KondisiJalanEntity(
                        response.id_kondisi_jalan,
                        response.created_at,
                        response.deskripsi,
                        response.foto,
                        response.latitude,
                        response.longitude,
                        response.nama_lokasi,
                        response.tanggal,
                        response.updated_at
                    )
                    kondisiJalanList.add(kondisiJalan)
                }

                localDataSource.insertKondisiJalan(kondisiJalanList)
            }
        }.asLiveData()
    }

    override fun getKondisiJalanById(id_kondisi_jalan: String): LiveData<Resource<KondisiJalanEntity>> {
        TODO("Not yet implemented")
    }

}