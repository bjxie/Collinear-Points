import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.Comparator;

// faster version of BruteCollinearPoints
public class FastCollinearPoints {
    private int count = 0;
    private LineSegment[] segmentArr;

    public FastCollinearPoints(Point[] points) {
        checkException(points);
        int holdSize = (points.length * (points.length + 1)) / 2; // the maximum number of segments = (n * n + 1) / 2
        LineSegment[] hold = new LineSegment[holdSize]; // temp array to hold the segments

        for (int i = 0; i < points.length - 3; i++) {
            int track = 1;
            Point[] pointCopy = Arrays.copyOfRange(points, i + 1, points.length);
            Comparator<Point> comp = points[i].slopeOrder();
            Arrays.sort(pointCopy, comp); // sort a copy array of the points by Y-coordinate, breaking ties
                                          // by X-coordinate


            Point beginning = points[i];
            Point end = pointCopy[0];

            if (beginning.compareTo(end) > 0) { // switch the end and beginning if they are in wrong positions
                beginning = end;
                end = points[i];
            }

            double baseSlope = beginning.slopeTo(end); // initial slope

            for (int k = 1; k < pointCopy.length; k++) {
                double newSlope = points[i].slopeTo(pointCopy[k]);

                if (Double.compare(newSlope, baseSlope) == 0) { // compare the initial slope to new slopes
                    track++;
                    if (pointCopy[k].compareTo(beginning) < 0) { // update the segment endpoints
                        beginning = pointCopy[k];
                    } else if (pointCopy[k].compareTo(end) > 0) {
                        end = pointCopy[k];
                    }

                    if (track == 3) { // if 3 other points are collinear, they in a line
                        hold[count] = new LineSegment(beginning, end);
                        count++;
                    }
                } else {
                    if (pointCopy[k].compareTo(points[i]) < 0) {
                        beginning = pointCopy[k]; // update the next point to compare to
                        end = points[i];
                    } else {
                        beginning = points[i];
                        end = pointCopy[k];
                    }
                    baseSlope = newSlope;
                    track = 1;
                }
            }
        }
        segmentArr = new LineSegment[count];
        for (int i = 0; i < count; i++) {
            segmentArr[i] = hold[i];
        }
    }

    // finds all line segments containing 4 or more points
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

    public int numberOfSegments() {
        return count;
    }      // the number of line segments

    public LineSegment[] segments() {
        return segmentArr;
    } // the line segments


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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
