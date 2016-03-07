package org.multimediaprototype.ocs.service;

import java.util.List;

import org.multimediaprototype.oss.dao.model.OSSFile;

public interface IOSSOCSService {

    List<OSSFile> getUserOssList(Long userid,Integer offset,Integer rowCount);

}
