//
// Created by yangdongxu on 15/12/10.
// Copyright (c) 2015 xbingoz. All rights reserved.
//

import Foundation
import JSONJoy

struct VideoDetail : JSONJoy {
    var mediaURL:String?
    var picURL:String?
    var baseInfo:JSONDecoder?

    init(){}
    init(_ decoder:JSONDecoder) {
        mediaURL = decoder["mediaURL"].string
        picURL = decoder["picURL"].string
        baseInfo = decoder["baseInfo"]
    }
}

