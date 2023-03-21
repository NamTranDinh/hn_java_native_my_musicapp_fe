package com.aptech.mymusic.data.repository;

import com.aptech.mymusic.data.sever.DataService;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.domain.repository.SongRepository;

import java.util.List;

import retrofit2.Call;

public class SongRepositoryImpl implements SongRepository {

    @Override
    public Call<List<SongModel>> getNewlyReleasedMusic() {
        return DataService.getInstance().getNewlyReleasedMusic();
    }

    @Override
    public Call<List<SongModel>> getAllSongFrom(String type, Long id) {
        return DataService.getInstance().getAllSongFrom(type, id);
    }

    @Override
    public Call<List<SongModel>> getSuggestSong(Long id, String listCurrentSongId, Integer limit) {
        return DataService.getInstance().getSuggestSong(id, listCurrentSongId, limit);
    }

    @Override
    public Call<List<SongModel>> searchSongByName(String nameSong) {
        return DataService.getInstance().searchSongByName(nameSong);
    }
}
