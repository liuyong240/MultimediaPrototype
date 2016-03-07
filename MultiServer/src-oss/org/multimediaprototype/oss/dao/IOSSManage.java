package org.multimediaprototype.oss.dao;

import java.util.List;

import org.multimediaprototype.oss.dao.model.OSSFile;

public interface IOSSManage {

    /**
     * 添加oss数据
     */
    long addOss(OSSFile ossManage);
 
    /**
     * 根据id获取某一个oss详细信息
     */
    OSSFile getOssdetail(long id);

    /**
     * 根据id获取某一个oss详细信息
     */
    OSSFile getOssdetailbykey(String key,Long userid);
    
    /**
     * 获取用户下所有oss列表
     */
    List<OSSFile> getOssList(Long id,Integer offset,Integer rowCount);
    
    /**
     * 删除oss数据
     */
    long delete(long id);

    OSSFile getByUrl(String url);
    
}