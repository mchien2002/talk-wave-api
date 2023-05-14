
# coding: utf-8
# The script is written by Ashutosh Kumar. Contact here: askumar.iitk@gmail.com


#Importing all necessary libraries here

import cv2
import sys
import os

# Writing images to disk function

def write_frames(videoName, outfile):
    video_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "video_message", videoName))
    thumbnail_dir = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'thumbnail_img')
    vidcap = cv2.VideoCapture(video_path)
    success, frame = vidcap.read()
    if success:
        cv2.imwrite(os.path.join(thumbnail_dir,'{}.jpg'.format(outfile)), frame)
        print('Writing image to disk')
        
if __name__ == "__main__":
    try:
        infile = sys.argv[1]
        outfile = sys.argv[2]   
    except:
        print("Wrong arguments. Please see the usage of the script.")
        sys.exit()
    write_frames(infile, outfile)

