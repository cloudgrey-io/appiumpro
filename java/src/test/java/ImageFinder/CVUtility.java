package ImageFinder;


import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class CVUtility {

    static public MatOfDMatch computeMatches(Mat queryDescriptors, Mat sceneDescriptors) {
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

        ArrayList<MatOfDMatch> matches = new ArrayList<MatOfDMatch>();
        matcher.knnMatch(queryDescriptors, sceneDescriptors, matches, 2);
        //System.out.println("original_matches:" + matches.size());
        ArrayList<DMatch> listFromMatches = new ArrayList<DMatch>();

        float dist1 = 0.0f, dist2 = 0.0f;
        for (int i = 0; i < matches.size(); i++) {
            DMatch dMatches[] = matches.get(i).toArray();
            DMatch dmatch = dMatches[0];
            dist1 = dMatches[0].distance;
            dist2 = dMatches[1].distance;

            if (dist1 < 0.8f * dist2) {
                listFromMatches.add(dmatch);
            }
        }

        MatOfDMatch trueMatches = new MatOfDMatch();
        trueMatches.fromList(listFromMatches);
        return trueMatches;
    }

    static public MatOfDMatch filterMatchesByHomography(MatOfKeyPoint keypoints1, MatOfKeyPoint keypoints2, MatOfDMatch matches) {
        List<Point> lp1 = new ArrayList<Point>(500);
        List<Point> lp2 = new ArrayList<Point>(500);

        KeyPoint[] k1 = keypoints1.toArray();
        KeyPoint[] k2 = keypoints2.toArray();

        List<DMatch> matches_original = matches.toList();

        if (matches_original.size() < 4) {
            MatOfDMatch mat = new MatOfDMatch();
            return mat;
        }

        // Add matches keypoints to new list to apply homography
        for (DMatch match : matches_original) {
            Point kk1 = k1[match.queryIdx].pt;
            Point kk2 = k2[match.trainIdx].pt;
            lp1.add(kk1);
            lp2.add(kk2);
        }

        MatOfPoint2f srcPoints = new MatOfPoint2f(lp1.toArray(new Point[0]));
        MatOfPoint2f dstPoints = new MatOfPoint2f(lp2.toArray(new Point[0]));

        Mat mask = new Mat();
        Calib3d.findHomography(srcPoints, dstPoints, Calib3d.RANSAC, 2.5, mask, 2000, 0.995);

        List<DMatch> matches_homo = new ArrayList<DMatch>();
        int size = (int) mask.size().height;
        for (int i = 0; i < size; i++) {
            if (mask.get(i, 0)[0] == 1) {
                DMatch d = matches_original.get(i);
                matches_homo.add(d);
            }
        }

        MatOfDMatch mat = new MatOfDMatch();
        mat.fromList(matches_homo);
        return mat;
    }
    
    static public Mat computeSceneCorners (Mat queryImg,ArrayList<Point> queryInlierList,  ArrayList<Point> sceneInlierList) {
        
        MatOfPoint2f objForDraw = new MatOfPoint2f();
        objForDraw.fromList(queryInlierList);
        MatOfPoint2f sceneForDraw = new MatOfPoint2f();
        sceneForDraw.fromList(sceneInlierList);

        Mat H = Calib3d.findHomography(objForDraw, sceneForDraw);
        
        Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);
        Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);

        obj_corners.put(0, 0, new double[]{0, 0});
        obj_corners.put(1, 0, new double[]{queryImg.cols(), 0});
        obj_corners.put(2, 0, new double[]{queryImg.cols(), queryImg.rows()});
        obj_corners.put(3, 0, new double[]{0, queryImg.rows()});
        Core.perspectiveTransform(obj_corners, scene_corners, H);

        return scene_corners;
    }
    
     static public Mat drawMatchesResult(Mat queryImg,Mat sceneImg,ArrayList<Point> queryInlierList,ArrayList<Point> sceneInlierList,Mat scene_corners)
    {
        int queryW = (int)queryImg.size().width;
        int queryH = (int)queryImg.size().height;
        
        int sceneW = (int)sceneImg.size().width;
        int sceneH = (int)sceneImg.size().height;
        
        int needW = sceneW > queryW ? sceneW : queryW;
        int needH = sceneH +  queryH;
        Mat newImg = Mat.zeros(needH,needW , queryImg.type());
        
        sceneImg.copyTo(newImg.rowRange(0,sceneH).colRange(0, sceneW));
        queryImg.copyTo(newImg.rowRange(sceneH,needH).colRange(0, queryW));

        
        int count = queryInlierList.size();
        for (int i = 0; i < count; i++) {
            
            Point from = sceneInlierList.get(i);
            
            Point to = new Point(queryInlierList.get(i).x,queryInlierList.get(i).y);
            to.y += sceneH;
            
            Imgproc.line(newImg,from , to, new Scalar(0, 255, 0), 4);
        }

        Imgproc.line(newImg, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)), new Scalar(0, 255, 0), 4);
        Imgproc.line(newImg, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)), new Scalar(0, 255, 0), 4);
        Imgproc.line(newImg, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)), new Scalar(0, 255, 0), 4);
        Imgproc.line(newImg, new Point(scene_corners.get(3, 0)), new Point(scene_corners.get(0, 0)), new Scalar(0, 255, 0), 4);
        
        return newImg;
    }
}
