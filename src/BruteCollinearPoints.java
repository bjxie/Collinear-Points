import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
// Given a set of n distinct points in the plane, find every (maximal) line segment that
// connects a subset of 4 or more of the points (quadratic time).

public class BruteCollinearPoints {
    private int count;
    private LineSegment[] segmentArr;

    public BruteCollinearPoints(Point[] points) {
        checkException(points);
        int segmentArraySize = (points.length * (points.length + 1)) / 2; // the maximum number of segments = (n * n + 1) / 2
        LineSegment[] hold = new LineSegment[segmentArraySize]; // temp array to hold the segments

        for (int i = 0; i < points.length - 2; i++) {
            for (int j = i + 1; j < points.length; j++) {
                Point beginning = points[i];
                Point end = points[j];
                int track = 1;

                if (beginning.compareTo(end) > 0) { // keeps track of the segment endpoints
                    beginning = end;
                    end = points[i];
                }
                double initialSlope = points[i].slopeTo(points[j]);
                for (int k = j + 1; k < points.length; k++) {
                    double nextSlope = points[i].slopeTo(points[k]); // compares the slope between [i] & [j] to the slopes
                                                                     // between [i] and rest of points
                    if (Double.compare(initialSlope, nextSlope) == 0) {
                        track++;
                        if (points[k].compareTo(beginning) < 0) {
                            beginning = points[k]; // update the endpoints of the line segment
                        } else if (points[k].compareTo(end) > 0) {
                            end = points[k];
                        }
                    }
                    if (track == 3) { // if four points are collinear, they are in a line
                        hold[count] = new LineSegment(beginning, end);
                        count++;
                        break;
                    }
                }
            }
        }

        segmentArr = new LineSegment[count];
        for (int i = 0; i < count; i++) {
            segmentArr[i] = hold[i]; // transfer segments from temp array to returned array
        }

    }    // finds all line segments containing 4 points

    // the number of line segments
    public int numberOfSegments() {
        return count;
}

    public LineSegment[] segments() {
        return segmentArr;

    }

    private void checkException(Point[] points) {
        if (points == null) throw new java.lang.NullPointerException();
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new java.lang.NullPointerException();
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null)
                    throw new java.lang.NullPointerException();
                if (points[i].compareTo(points[j]) == 0)
                    throw new java.lang.IllegalArgumentException();
            }
        }
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
