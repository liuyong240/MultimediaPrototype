//
// Created by yangdongxu on 15/12/10.
// Copyright (c) 2015 xbingoz. All rights reserved.
//

import Foundation
import JSONJoy

struct ResponseObject {
    var code: Int?
    var error: String?
    var data: AnyObject?
    init() {}
    init(_ decoder : JSONDecoder) {
        code = decoder["code"].integer
        error = decoder["error"].string
        data = decoder["data"]
    }
}

