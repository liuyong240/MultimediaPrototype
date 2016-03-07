//
// Created by yangdongxu on 15/12/10.
// Copyright (c) 2015 xbingoz. All rights reserved.
//

import Foundation
import UIKit
import SwiftHTTP
import JSONJoy
import AVFoundation
import AVKit
import MobileCoreServices

class PlayerViewController : AVPlayerViewController {

    var videoId: Int?

    override func viewDidLoad() {
        super.viewDidLoad()
        print(111, videoId)
        loadData()

    }
    
    override func viewDidDisappear(animated: Bool) {
        super.viewDidDisappear(animated)
        self.player!.pause()
    }


    func loadData() {
        print(22, videoId)
        APIService.getVideo(videoId!, callback: {
            (err: NSError?, resp: Response?) in

            if resp != nil {

                let r = ResponseObject(JSONDecoder(resp!.data))
                if r.code == 0 && r.data != nil {

                    let data = r.data as? JSONDecoder
                    let videoDetail = VideoDetail(data!)
                    let mediaURL = videoDetail.mediaURL!
                    let steamingURL = NSURL(string: "\(APIService.backendHost)\(mediaURL)")!

                    self.player = AVPlayer(URL:steamingURL)
                    self.player!.play()

                }
            }

        })
    }

}