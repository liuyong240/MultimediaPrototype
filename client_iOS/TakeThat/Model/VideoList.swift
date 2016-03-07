//
// Created by yangdongxu on 15/12/10.
// Copyright (c) 2015 xbingoz. All rights reserved.
//

import Foundation
import JSONJoy

struct Video : JSONJoy{
    var id : Int?
    var mediaId: Int?
    var picId: Int?
    var description: String?
    var isDelete: Bool?
    var gmtCreated: Int?
    var status: Int?
    var picURL: String?
    init() {}
    init(_ decoder : JSONDecoder) {
        id = decoder["id"].integer
        mediaId = decoder["mediaId"].integer
        picId = decoder["picId"].integer
        description = decoder["description"].string
        isDelete = decoder["isDelete"].bool
        gmtCreated = decoder["gmtCreated"].integer
        status = decoder["status"].integer
        picURL = decoder["pic_url"].string
    }
}

struct VideoList : JSONJoy {
    var list: [Video]?
    init() {
    }
    init(_ decoder : JSONDecoder) {
        if let jsonList = decoder["list"].array {
            var listTem = [Video]()
            for videoDecoder in jsonList {
                let v = Video(videoDecoder)
                listTem.append(v)
            }
            list = listTem
        }
    }
}
