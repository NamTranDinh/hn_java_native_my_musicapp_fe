package com.aptech.mymusic.data.repository;

import com.aptech.mymusic.domain.repository.AdsRepository;
import com.aptech.mymusic.domain.repository.AlbumRepository;
import com.aptech.mymusic.domain.repository.CategoryRepository;
import com.aptech.mymusic.domain.repository.PlaylistRepository;
import com.aptech.mymusic.domain.repository.Repository;
import com.aptech.mymusic.domain.repository.SongRepository;
import com.aptech.mymusic.domain.repository.TopicRepository;

public class RepositoryImpl implements Repository {

    private final AdsRepository mAdsRepository;
    private final AlbumRepository mAlbumRepository;
    private final CategoryRepository mCategoryRepository;
    private final PlaylistRepository mPlaylistRepository;
    private final SongRepository mSongRepository;
    private final TopicRepository mTopicRepository;

    public RepositoryImpl(AdsRepository mAdsRepository, AlbumRepository mAlbumRepository, CategoryRepository mCategoryRepository, PlaylistRepository mPlaylistRepository, SongRepository mSongRepository, TopicRepository mTopicRepository) {
        this.mAdsRepository = mAdsRepository;
        this.mAlbumRepository = mAlbumRepository;
        this.mCategoryRepository = mCategoryRepository;
        this.mPlaylistRepository = mPlaylistRepository;
        this.mSongRepository = mSongRepository;
        this.mTopicRepository = mTopicRepository;
    }

    @Override
    public AdsRepository getAdsRepository() {
        return mAdsRepository;
    }

    @Override
    public AlbumRepository getAlbumRepository() {
        return mAlbumRepository;
    }

    @Override
    public CategoryRepository getCategoryRepository() {
        return mCategoryRepository;
    }

    @Override
    public PlaylistRepository getPlaylistRepository() {
        return mPlaylistRepository;
    }

    @Override
    public SongRepository getSongRepository() {
        return mSongRepository;
    }

    @Override
    public TopicRepository getTopicRepository() {
        return mTopicRepository;
    }
}
