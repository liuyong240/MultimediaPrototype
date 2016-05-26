package org.multimediaprototype.oss.dao;

import org.multimediaprototype.oss.dao.model.MediaMapping;

public interface ImediaMap {
    
    long addMediaMap(MediaMapping mediaMapping);
    long updataMediaMap(MediaMapping mediaMapping);
    MediaMapping getMediaMap(Long id);
    MediaMapping getMediaMapByMediaId(Long mediaId);
    long deleteMudiaMap(long id);
    int deleteMediaByTime();
}
