//
// Created by yangdongxu on 15/12/10.
// Copyright (c) 2015 xbingoz. All rights reserved.
//

import Foundation
import UIKit
import MobileCoreServices
import AVKit
import AVFoundation

class UploadViewController : UIViewController {

    //let movieFilePath:String?
    var movieURL:NSURL? = nil

    @IBOutlet weak var preview: UIImageView!

    @IBOutlet weak var desc: UITextView!

    @IBAction func save(sender: AnyObject) {
//        dismissViewControllerAnimated(true, completion: nil)
        let imageData = UIImagePNGRepresentation(preview.image!)
        let paths = NSSearchPathForDirectoriesInDomains(
        NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask, true)
        let documentsDirectory: AnyObject = paths[0]
        let dataPath = documentsDirectory.stringByAppendingPathComponent("/test.png")
        imageData?.writeToFile(dataPath, atomically: false)

        let imageURL = NSURL(fileURLWithPath:dataPath)

        APIService.upload(movieURL!, picURL:imageURL, desc: desc.text, callback: {response in
            print(response.description)

            self.dismissViewControllerAnimated(true, completion: nil)

        })
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        desc.layer.borderColor = UIColor.lightGrayColor().CGColor
        desc.layer.borderWidth = 0.4
    }

    func setMovieFilePath(file:String) {
        movieURL = getMovieURL(file)
        preview.image = getImage(movieURL!)
    }

    func getMovieURL(file:String) -> NSURL {
        let paths = NSSearchPathForDirectoriesInDomains(
        NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.UserDomainMask, true)
        let documentsDirectory: AnyObject = paths[0]
        let dataPath = documentsDirectory.stringByAppendingPathComponent(file)
        return NSURL(fileURLWithPath:dataPath)
    }

    func getImage(fileURL:NSURL) -> UIImage? {
        var image:UIImage?
        let videoAsset = (AVAsset(URL: fileURL))
        let duration = videoAsset.duration
        let generator = AVAssetImageGenerator(asset: videoAsset)
        generator.appliesPreferredTrackTransform = true
        let halfDuration = CMTime(seconds: duration.seconds / 2.0, preferredTimescale: 600)
        do {
            let frameRef = try generator.copyCGImageAtTime(halfDuration, actualTime: nil)
            image = UIImage(CGImage: frameRef)
        } catch let error as NSError {
            print(error.localizedDescription)
        }
        return image
    }
}
