package org.multimediaprototype.oss.dao.impl;

import java.util.List;

import org.multimediaprototype.oss.dao.IOSSManage;
import org.multimediaprototype.oss.dao.mapper.OSSFileMapper;
import org.multimediaprototype.oss.dao.model.OSSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("OSSManage")
public class OSSManage implements IOSSManage {

    @Autowired
    public OSSFileMapper ossFileMapper;
    
    @Override
    public long addOss(OSSFile ossManage) {
        // TODO Auto-generated method stub
        return ossFileMapper.insert(ossManage);
    }

    @Override
    public OSSFile getOssdetail(long id) {
        // TODO Auto-generated method stub
        return ossFileMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<OSSFile> getOssList(Long id,Integer offset,Integer rowCount) {
        // TODO Auto-generated method stub
        return ossFileMapper.selectByKey(id,offset*rowCount);
    }

    @Override
    public long delete(long id) {
        // TODO Auto-generated method stub
        return ossFileMapper.deleteByPrimaryKey(id);
    }

    @Override
    public OSSFile getOssdetailbykey(String key, Long userid) {
        // TODO Auto-generated method stub
        return ossFileMapper.getOssdetailbykey(key, userid);
    }

    @Override
    public OSSFile getByUrl(String url) {
        return ossFileMapper.getByUrl(url);
    }


}
