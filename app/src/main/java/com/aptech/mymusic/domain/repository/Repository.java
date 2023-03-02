package com.aptech.mymusic.domain.repository;

public interface Repository {

    AdsRepository getAdsRepository();

    AlbumRepository getAlbumRepository();

    CategoryRepository getCategoryRepository();

    PlaylistRepository getPlaylistRepository();

    SongRepository getSongRepository();

    TopicRepository getTopicRepository();

}
