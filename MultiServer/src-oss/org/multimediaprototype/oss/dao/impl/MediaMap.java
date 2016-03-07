package org.multimediaprototype.oss.dao.impl;

import org.multimediaprototype.oss.dao.ImediaMap;
import org.multimediaprototype.oss.dao.mapper.MediaMappingMapper;
import org.multimediaprototype.oss.dao.model.MediaMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("MediaMap")
public class MediaMap implements ImediaMap {
    
    @Autowired
    private MediaMappingMapper mediaMappingMapper;

    @Override
    public long addMediaMap(MediaMapping mediaMapping) {
        // TODO Auto-generated method stub
        return mediaMappingMapper.insert(mediaMapping);
    }

    @Override
    public long updataMediaMap(MediaMapping mediaMapping) {
        // TODO Auto-generated method stub
        return mediaMappingMapper.updateByPrimaryKeySelective(mediaMapping);
    }

    @Override
    public MediaMapping getMediaMap(Long id) {
        // TODO Auto-generated method stub
        return mediaMappingMapper.selectByPrimaryKey(id);
    }

    @Override
    public MediaMapping getMediaMapByMediaId(Long mediaId) {
        // TODO Auto-generated method stub
        return mediaMappingMapper.selectByMediaId(mediaId);
    }

    @Override
    public long deleteMudiaMap(long id) {
        // TODO Auto-generated method stub
        return mediaMappingMapper.deleteByPrimaryKey(id);
    }

}
