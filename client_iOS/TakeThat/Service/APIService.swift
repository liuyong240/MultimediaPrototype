//
// Created by yangdongxu on 15/12/10.
// Copyright (c) 2015 xbingoz. All rights reserved.
//

import Foundation
import SwiftHTTP
import UIKit

typealias callbackFunc = (NSError?, Response?) -> Void

class APIService {
    static let backendHost = "http://127.0.0.1:8080"
    static let uploadAPI = "/admin/api/video/updateMediaAndPicStatus"
    static let listAPI = "/admin/api/video/list?status=1"
    static let videoAPI = "/admin/api/video/get/"

    public class func upload(movieURL:NSURL, picURL:NSURL, desc:String, callback:((Response) -> Void) ) {
        let theAPI = backendHost + uploadAPI
        do {
            let opt = try HTTP.POST(theAPI, parameters: [
                "desc": desc,
                "mediaFile": Upload(fileUrl: movieURL),
                "picFile": Upload(fileUrl: picURL)
            ])
            opt.start(callback)
        } catch let error {
            print("got an error creating the request: \(error)")
        }
    }

    public class func get(url: String, callback: callbackFunc) {
        do {
            let opt = try HTTP.GET(url)
            opt.start { response in
                if let err = response.error {
                    print("error: \(err.localizedDescription)")
                    callback(err, nil)
                    return //also notify app of failure as needed
                }
                callback(nil, response)
                //print("opt finished: \(response)")
                //print("data is: \(response.data)") access the response of the data with response.data
            }
        } catch let error as NSError {
            print("got an error creating the request: \(error)")
            callback(error, nil)
        }
    }

    public class func getList(callback: callbackFunc) {
        let url = backendHost + listAPI
        get(url, callback: callback)
    }

    public class func getVideo(id:Int, callback: callbackFunc) {
        let url = "\(backendHost + videoAPI)\(id)"
        print(url)
        get(url, callback: callback)
    }

}
