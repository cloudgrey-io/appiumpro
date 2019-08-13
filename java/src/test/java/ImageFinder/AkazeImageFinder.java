package ImageFinder;

import org.opencv.core.*;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

class CVImg {

    public String filename;
    public Mat img;
    public MatOfKeyPoint keypoints = new MatOfKeyPoint();
    public Mat descriptors = new Mat();
}

public class AkazeImageFinder {

    static boolean opencv_setup = false;

    static void setupOpenCVEnv() {

        String originalPath = System.getProperty("java.library.path");
        String currentPath = Paths.get("").toAbsolutePath().toString();
      //  System.setProperty("java.library.path", originalPath + System.getProperty("path.separator") + currentPath + "/opencv-3.0.0/");
        System.setProperty("java.library.path", "/usr/local/Cellar/opencv/4.1.0_2/share/java/opencv4/");

        Field fieldSysPath = null;
        try {
            fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        fieldSysPath.setAccessible(true);
        try {
            fieldSysPath.set(null, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.loadLibrary("opencv_java410");
        opencv_setup = true;
    }

    static CVImg LoadImg(String filename) {
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.AKAZE);
        DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.AKAZE);

        CVImg result = new CVImg();
        result.filename = filename;
        result.img = Imgcodecs.imread(filename, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        detector.detect(result.img, result.keypoints);
        extractor.compute(result.img, result.keypoints, result.descriptors);
        
        return result;
    }

    static public int[] QueryImage(String queryImagePath, String sceneImagePath) {

        if (!opencv_setup) {
            setupOpenCVEnv();
        }

        CVImg query = LoadImg(queryImagePath);
        CVImg scene = LoadImg(sceneImagePath);

        //Compute matches and filter matches
        MatOfDMatch matches = CVUtility.computeMatches(query.descriptors, scene.descriptors);
        MatOfDMatch filtered = CVUtility.filterMatchesByHomography(query.keypoints, scene.keypoints, matches);

        //show debug message
        int original_key1 = (int) query.descriptors.size().height;
        int original_key2 = (int) scene.descriptors.size().height;
        int original_matches = (int) matches.size().height;
        int filtered_matches = (int) filtered.size().height;

        System.out.println("query_key:" + original_key1);
        System.out.println("scene_key:" + original_key2);
        System.out.println("original_matches:" + original_matches);
        System.out.println("filtered_matches:" + filtered_matches);

        // If count of matches is OK, apply homography check
        if (filtered_matches < 4) {
            return null;
        }

        //Convert matches to keypoints' List
        DMatch inliers[] = filtered.toArray();
        KeyPoint[] queryKPs = query.keypoints.toArray();
        KeyPoint[] sceneKPs = scene.keypoints.toArray();

        ArrayList<Point> queryInlierList = new ArrayList<Point>();
        ArrayList<Point> sceneInlierList = new ArrayList<Point>();

        for (int i = 0; i < inliers.length; i++) {
            DMatch m = inliers[i];

            Point pO = queryKPs[m.queryIdx].pt;
            Point pS = sceneKPs[m.trainIdx].pt;
            queryInlierList.add(pO);
            sceneInlierList.add(pS);
        }
        //compute matched  rect
        Mat scene_corners = CVUtility.computeSceneCorners(query.img, queryInlierList, sceneInlierList);

        //draw matches to img
        Mat colorQueryImg = Imgcodecs.imread(queryImagePath, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        Mat colorSceneImg = Imgcodecs.imread(sceneImagePath, Imgcodecs.CV_LOAD_IMAGE_COLOR);

        Mat resultImg = CVUtility.drawMatchesResult(colorQueryImg, colorSceneImg, queryInlierList, sceneInlierList, scene_corners);
        String resultFilename = queryImagePath + "_query_result.png";
        Imgcodecs.imwrite(resultFilename, resultImg);

        String screenshotDirectory = System.getProperty("appium.screenshots.dir", System.getProperty("java.io.tmpdir", ""));
        Imgcodecs.imwrite(new File(screenshotDirectory, new File(resultFilename).getName()).getAbsolutePath(), resultImg);

        //compute rotate      
        Point top_left = new Point(scene_corners.get(0, 0));
        Point top_right = new Point(scene_corners.get(1, 0));
        Point bottom_left = new Point(scene_corners.get(3, 0));
        Point bottom_right = new Point(scene_corners.get(2, 0));
        
        String calculateRotation = calculateImageRotation(top_left, top_right, bottom_left, bottom_right);

        double centerX = (top_left.x + top_right.x + bottom_left.x + bottom_right.x) * 0.25;
        double centerY = (top_left.y + top_right.y + bottom_left.y + bottom_right.y) * 0.25;
        Point center = new Point(centerX,centerY);
        System.out.println("find query image at " + (int)center.x + "," + (int)center.y + "with rotation = " + calculateRotation);

      
        if (calculateRotation.equals("90 degrees")) {
            center.x = centerY;
            center.y = scene.img.size().width - centerX;
        } else if (calculateRotation.equals("180 degrees")) {
            center.x = scene.img.size().width - centerX;
            center.y = scene.img.size().height - centerY;
        } else if (calculateRotation.equals("270 degrees")) {
            center.x = scene.img.size().height - centerY;
            center.y = centerX;
        }         

        int[] result = {(int) center.x, (int) center.y};
        System.out.println("rotate fixed result: x=" + result[0] + ", y = " + result[1]);
        return result;
    }

    static String calculateImageRotation(Point top_left, Point top_right, Point bottom_left, Point bottom_right) {
            //we need to calculate rotation first, if this is not set
        //process the coordinates found - transform negative values to 0, get correct sizes
        //Point[] object_points = new Point[] {top_left,top_right,bottom_left,bottom_right};
        Point[] scene_points = new Point[]{top_left, top_right, bottom_left, bottom_right};
        Arrays.sort(scene_points, new PointSortY());
        //keep only biggest y out of the 2 small values:
        scene_points[0].y = scene_points[1].y;
        //keep only the smallest value out of the 2 high values:
        scene_points[3].y = scene_points[2].y;

        Arrays.sort(scene_points, new PointSortX());
        //keep only highest x out of the 2 small values:
        scene_points[0].x = scene_points[1].x;
        //keep only the smallest value out of the 2 high values:
        scene_points[3].x = scene_points[2].x;

        Point[] left_points = new Point[]{scene_points[0], scene_points[1]};
        Arrays.sort(left_points, new PointSortY());
        Point[] right_points = new Point[]{scene_points[2], scene_points[3]};
        Arrays.sort(right_points, new PointSortY());

        Point scene_top_left = new Point(left_points[0].x, left_points[0].y);

        String result = "no set";
        if (scene_top_left.equals(top_left)) {
            result = "0 degrees";
        } else if (scene_top_left.equals(bottom_left)) {
            result = "90 degrees";
        } else if (scene_top_left.equals(bottom_right)) {
            result = "180 degrees";
        } else if (scene_top_left.equals(top_right)) {
            result = "270 degrees";
        }

        return result;
    }
}

class PointSortX implements Comparator<Point> {

    public int compare(Point a, Point b) {
        return (a.x < b.x) ? -1 : (a.x > b.x) ? 1 : 0;
    }
}

class PointSortY implements Comparator<Point> {

    public int compare(Point a, Point b) {
        return (a.y < b.y) ? -1 : (a.y > b.y) ? 1 : 0;
    }
}
