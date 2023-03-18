package com.aptech.mymusic.di;

import com.aptech.mymusic.data.repository.AdsRepositoryImpl;
import com.aptech.mymusic.data.repository.AlbumRepositoryImpl;
import com.aptech.mymusic.data.repository.CategoryRepositoryImpl;
import com.aptech.mymusic.data.repository.PlaylistRepositoryImpl;
import com.aptech.mymusic.data.repository.RepositoryImpl;
import com.aptech.mymusic.data.repository.SongRepositoryImpl;
import com.aptech.mymusic.data.repository.TopicRepositoryImpl;
import com.aptech.mymusic.domain.repository.Repository;

public class DataInjection {

    private static final class MRepositoryHolder {
        static final Repository repository = new RepositoryImpl(
                new AdsRepositoryImpl(),
                new AlbumRepositoryImpl(),
                new CategoryRepositoryImpl(),
                new PlaylistRepositoryImpl(),
                new SongRepositoryImpl(),
                new TopicRepositoryImpl()
        );
    }

    public static Repository provideRepository() {
        return MRepositoryHolder.repository;
    }

}
