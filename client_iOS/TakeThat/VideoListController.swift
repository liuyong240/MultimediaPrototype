//
//  VideoListController.swift
//  TakeThat
//
//  Created by yangdongxu on 15/12/9.
//  Copyright © 2015年 xbingoz. All rights reserved.
//

import Foundation
import UIKit
import SwiftHTTP
import JSONJoy

class VideoListController : UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var tableView: UITableView!

    var videos:[Video]?
    var refreshControl:UIRefreshControl?

    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.delegate = self
        tableView.dataSource = self

        refreshControl = UIRefreshControl()
        refreshControl!.addTarget(self, action: Selector("loadData"), forControlEvents: UIControlEvents.ValueChanged)
        tableView.addSubview(refreshControl!);
        refreshControl!.beginRefreshing()

        loadData()
    }

    func loadData() {
        APIService.getList({(err:NSError?, resp:Response?) -> Void in
            if resp != nil {
                let r = ResponseObject(JSONDecoder(resp!.data))
                if r.code == 0 && r.data != nil{
                    let data = r.data as? JSONDecoder
                    let videoList = VideoList(data!)
                    self.videos = videoList.list
//                    print(videoList)
                    self.tableView.reloadData()
                    self.refreshControl?.endRefreshing()
                }
            }
        })
    }


    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        if segue.identifier == "go2player" {
            let playerViewController = segue.destinationViewController as! PlayerViewController
            if let selectedCell = sender as? UITableViewCell {
                let indexPath = self.tableView.indexPathForCell(selectedCell)!
                let selectVideo = self.videos![indexPath.row]
                playerViewController.videoId = selectVideo.id
            }
        }
    }


    func numberOfSectionsInTableView(tableView:UITableView) -> Int {
        return 1
    }

    func tableView(tableView:UITableView, numberOfRowsInSection section: Int) -> Int {
        if videos == nil {
            return 0
        }
        return videos!.count
    }

    func tableView(tableView:UITableView, cellForRowAtIndexPath indexPath:NSIndexPath) -> UITableViewCell {

        let cell = tableView.dequeueReusableCellWithIdentifier("baseCell", forIndexPath: indexPath) as! MainTableCell

        cell.previewImage.image = nil

        let text = videos![indexPath.row].description

        let imageURLString = APIService.backendHost + videos![indexPath.row].picURL!
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_BACKGROUND, 0)) {
            let imageURL = NSURL(string: imageURLString)
            if imageURL == nil {
                return
            }
            
            let imageData = NSData(contentsOfURL: imageURL!)
            
            if imageData == nil {
                return
            }
            
            let image = UIImage(data: imageData!)
            dispatch_async(dispatch_get_main_queue()) {
                cell.previewImage.image = image;
            }
        }

        cell.descLabel.text = text

        return cell
    }

}