package com.example.aplikasirutetravel.data

import androidx.lifecycle.LiveData
import com.example.aplikasirutetravel.data.source.local.LocalDataSource
import com.example.aplikasirutetravel.data.source.local.entity.KondisiJalanEntity
import com.example.aplikasirutetravel.data.source.local.entity.PerusahaanEntity
import com.example.aplikasirutetravel.data.source.local.entity.TrayekEntity
import com.example.aplikasirutetravel.data.source.remote.ApiResponse
import com.example.aplikasirutetravel.data.source.remote.RemoteDataSource
import com.example.aplikasirutetravel.data.source.remote.response.KondisiJalan
import com.example.aplikasirutetravel.data.source.remote.response.Perusahaan
import com.example.aplikasirutetravel.data.source.remote.response.Trayek
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

    override fun getAllTrayek(): LiveData<Resource<List<TrayekEntity>>> {
        return object :
            NetworkBoundResource<List<TrayekEntity>, List<Trayek>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<TrayekEntity>> =
                localDataSource.getAllTrayek()

            override fun shouldFetch(data: List<TrayekEntity>?): Boolean =
                true

            override fun createCall(): LiveData<ApiResponse<List<Trayek>>> =
                remoteDataSource.getAllTrayek()

            public override fun saveCallResult(data: List<Trayek>) {
                val trayekList = ArrayList<TrayekEntity>()
                for (response in data) {
                    val trayek = TrayekEntity(
                        response.id_trayek,
                        response.nama_trayek,
                        response.asal,
                        response.tujuan,
                        response.id_jadwal,
                        response.latitude_asal,
                        response.longitude_asal,
                        response.latitude_tujuan,
                        response.longitude_tujuan,
                        response.status,
                        response.created_at,
                        response.updated_at,
                        response.jam,
                        response.hari,
                        response.nama_perusahaan
                    )
                    trayekList.add(trayek)
                }
                localDataSource.insertTrayek(trayekList)
            }
        }.asLiveData()
    }

    override fun getPerusahaanById(id_perusahaan: String): LiveData<Resource<PerusahaanEntity>> {
        return object :
            NetworkBoundResource<PerusahaanEntity, List<Perusahaan>>(appExecutors) {
            override fun loadFromDB(): LiveData<PerusahaanEntity> =
                localDataSource.getPerusahaanById(id_perusahaan)

            override fun shouldFetch(data: PerusahaanEntity?): Boolean =
                data == null

            override fun createCall(): LiveData<ApiResponse<List<Perusahaan>>> =
                remoteDataSource.getPerusahaanById(id_perusahaan)

            override fun saveCallResult(data: List<Perusahaan>) {
                val perusahaan = PerusahaanEntity(
                    data[0].id_perusahaan,
                    data[0].alamat_perusahaan,
                    data[0].asal,
                    data[0].created_at,
                    data[0].facebook,
                    data[0].foto,
                    data[0].id_jadwal,
                    data[0].id_trayek,
                    data[0].instagram,
                    data[0].nama_perusahaan,
                    data[0].nama_trayek,
                    data[0].nomor_handphone,
                    data[0].pimpinan,
                    data[0].status,
                    data[0].tujuan,
                    data[0].updated_at,
                    data[0].website,
                )
                localDataSource.insertPerusahaanById(perusahaan)
            }
        }.asLiveData()
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
        return object :
            NetworkBoundResource<KondisiJalanEntity, List<KondisiJalan>>(appExecutors) {
            override fun loadFromDB(): LiveData<KondisiJalanEntity> =
                localDataSource.getKondisiJalanById(id_kondisi_jalan)

            override fun shouldFetch(data: KondisiJalanEntity?): Boolean =
                data == null

            override fun createCall(): LiveData<ApiResponse<List<KondisiJalan>>> =
                remoteDataSource.getKondisiJalanById(id_kondisi_jalan)

            override fun saveCallResult(data: List<KondisiJalan>) {
                val kondisiJalan = KondisiJalanEntity(
                    data[0].id_kondisi_jalan,
                    data[0].created_at,
                    data[0].deskripsi,
                    data[0].foto,
                    data[0].latitude,
                    data[0].longitude,
                    data[0].nama_lokasi,
                    data[0].tanggal,
                    data[0].updated_at
                )
                localDataSource.insertKondisiJalanById(kondisiJalan)
            }
        }.asLiveData()
    }

}