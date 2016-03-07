package org.multimediaprototype.ocs.service.impl;

import java.util.List;

import org.multimediaprototype.ocs.base.OCSBaseImpl;
import org.multimediaprototype.ocs.service.IOSSOCSService;
import org.multimediaprototype.oss.dao.impl.OSSManage;
import org.multimediaprototype.oss.dao.model.OSSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.multimediaprototype.ocs.base.KeysTools;
@Service("ossOCSService")
public class OSSOCSService implements IOSSOCSService {

    @Autowired
    private OCSBaseImpl OCSBase;
    
    @Autowired
    private OSSManage ossManage;
    
    @SuppressWarnings("unchecked")
    @Override
    public List<OSSFile> getUserOssList(Long userid, Integer offset,
            Integer rowCount) {
        // TODO Auto-generated method stub
        String userkey = KeysTools.UserOssListkey.userOssListKey(userid, offset, rowCount);
        Object userOsslist = OCSBase.get(userkey);
//        Object userOsslist = null; 
        if(userOsslist==null){
            List<OSSFile> userOsslist1 = ossManage.getOssList(userid, offset, rowCount);
            if(userOsslist1==null)
                return null;
            else{
                OCSBase.set(userkey, 1000, userOsslist1);
                return userOsslist1;
            }
        }
        return (List<OSSFile>)userOsslist;
    }

}
