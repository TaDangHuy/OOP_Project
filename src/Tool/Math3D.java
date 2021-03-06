package Tool;
import java.util.ArrayList;
import java.util.List;

public class Math3D {

    public Math3D() {
    }

    public Vector3D VectorDirectProduct(Vector3D a1, Vector3D a2){
        double t1 = a1.getY() * a2.getZ() - a2.getY() * a1.getZ();
        double t2 = a1.getZ() * a2.getX() - a1.getX() * a2.getZ();
        double t3 = a1.getX() * a2.getY() - a2.getX() * a1.getY();
        return new Vector3D(t1,t2,t3);
    }

    public boolean Is3PointInLine(Point a, Point b, Point c){
        Vector3D ab = new Vector3D(a,b);
        Vector3D ac = new Vector3D(a,c);
        Vector3D bc = new Vector3D(b,c);

        if(Math.abs(ab.length() + ac.length() - bc.length()) < 0.0001)
            return true;
        if(Math.abs(ab.length() + bc.length() - ac.length()) < 0.0001)
            return true;
        if(Math.abs(ac.length() + bc.length() - ab.length()) < 0.0001)
            return true;

        return false;
    }

    public boolean IsRightTriangle(Point a, Point b, Point c){
        Vector3D ab = new Vector3D(a,b);
        Vector3D ac = new Vector3D(a,c);
        Vector3D bc = new Vector3D(b,c);

        if(Is3PointInLine(a,b,c))
            return false;
        if(Math.abs(ab.length2() + ac.length2() - bc.length2()) < 0.0001)
            return true;
        if(Math.abs(ab.length2() + bc.length2() - ac.length2()) < 0.0001)
            return true;
        if(Math.abs(ac.length2() + bc.length2() - ab.length2()) < 0.0001)
            return true;

        return false;
    }

    public double triangleArea(Point a, Point b, Point c){
        Vector3D ab = new Vector3D(a,b);
        Vector3D ac = new Vector3D(a,c);
        Vector3D bc = new Vector3D(b,c);
        double  p = ab.length()+ bc.length()+ ac.length();
        return Math.sqrt(p*(p- ab.length())*(p- bc.length())*(p- ac.length()));
    }

    public Point midPoint(Point a, Point b){
        Point c = new Point();
        c.setX((a.getX() + b.getX())/2);
        c.setY((a.getY() + b.getY())/2);
        c.setZ((a.getZ() + b.getZ())/2);
        return c;
    }

    public boolean IsRectangle(Point a, Point b, Point c, Point d){
        if(!IsRightTriangle(a,b,c))
            return false;
        Vector3D ab = new Vector3D(a,b);
        Vector3D ac = new Vector3D(a,c);
        Vector3D bc = new Vector3D(b,c);

        if(ab.length() > ac.length() && ab.length() > bc.length()){
            Point midab = midPoint(a,b);
            Point d1 = new Point();
            d1.setX(midab.getX()*2 - c.getX());
            d1.setY(midab.getY()*2 - c.getY());
            d1.setZ(midab.getZ()*2 - c.getZ());
            if(d.equals(d1))
                return true;
        }

        if(ac.length() > ab.length() && ac.length() > bc.length()){
            Point midac = midPoint(a,c);
            Point d1 = new Point();
            d1.setX(midac.getX()*2 - b.getX());
            d1.setY(midac.getY()*2 - b.getY());
            d1.setZ(midac.getZ()*2 - b.getZ());
            if(d.equals(d1))
                return true;
        }

        if(bc.length() > ab.length() && bc.length() > ac.length()){
            Point midbc = midPoint(b,c);
            Point d1 = new Point();
            d1.setX(midbc.getX()*2 - a.getX());
            d1.setY(midbc.getY()*2 - a.getY());
            d1.setZ(midbc.getZ()*2 - a.getZ());
            if(d.equals(d1))
                return true;
        }
        return false;
    }

    public boolean IsCuboid(List<Point> list){
        List<Point> listPoint = new ArrayList<Point>(8);
        listPoint.addAll(list);

        int i,pointIndex1=0;
        int pointIndex = 0;

        double minRange = (new Vector3D(list.get(0),list.get(1))).length();
        double minArea = triangleArea(list.get(0),list.get(1),list.get(2));

        for(i=2;i<list.size();i++){
            Vector3D v = new Vector3D(list.get(0),list.get(i));
            if (v.length() <= minRange){
                minRange = v.length();
                pointIndex1 = i;
            }
        }

        for(i=1; i < list.size() && i!=pointIndex1;i++){
            if(triangleArea(list.get(0),list.get(pointIndex1),list.get(i)) <= minArea){
                minArea = triangleArea(list.get(0),list.get(pointIndex1),list.get(i));
                pointIndex = i;
            }
        }

        PlaneEquation p = new PlaneEquation(list.get(0),list.get(pointIndex1),list.get(pointIndex));
        Point p1 = list.get(0);
        Point p2 = list.get(pointIndex1);
        Point p3 = list.get(pointIndex);
        listPoint.remove(p1);
        listPoint.remove(p2);
        listPoint.remove(p3);

        for(i=0;i<listPoint.size();i++){
            if(p.IsIncludePoint(listPoint.get(i))){
               if(!IsRectangle(p1,p2,p3,listPoint.get(i)))
                   return false;
               Point p4 = listPoint.get(i);
               listPoint.remove(p4);
                double c1=0,c2=0,c3=0,c4=0;
                c1 = edge(p,p1,listPoint.get(0), listPoint.get(1), listPoint.get(2),listPoint.get(3));
                c2 = edge(p,p2,listPoint.get(0),listPoint.get(1), listPoint.get(2), listPoint.get(3));
                c3 = edge(p,p3,listPoint.get(0), listPoint.get(1),listPoint.get(2),listPoint.get(3));
                c4 = edge(p,p4,listPoint.get(0),listPoint.get(1),listPoint.get(2), listPoint.get(3));
                if(c1 != 0 && c2 != 0 && c3 != 0 && c4!= 0 && Math.abs(c1 - c2) < 0.0001 && Math.abs(c2 - c3) < 0.0001 && Math.abs(c3 - c4) < 0.0001)
                    return true;
                }
            }

        return false;
    }

    public double edge(PlaneEquation p, Point o, Point d1, Point d2, Point d3, Point d4){
        Vector3D od1 = new Vector3D(o,d1);
        Vector3D od2 = new Vector3D(o,d2);
        Vector3D od3 = new Vector3D(o,d3);
        Vector3D od4 = new Vector3D(o,d4);
        double edge = 0;

        if(p.IsPerpendicular(od1)){
            return od1.length();
        }
        else if(p.IsPerpendicular(od2)){
            return od2.length();
        }
        else if(p.IsPerpendicular(od3)){
            return od3.length();
        }
        else if(p.IsPerpendicular(od4)){
            return od4.length();
        }
        return 0;
    }

    public double xMax (List <Point> list){
        double max = list.get(0).getX();
        for(int i = 0 ;i < list.size();i++){
            if(list.get(i).getX() >= max)
                max = list.get(i).getX();
        }
        return max;
    }

    public double yMax (List <Point> list){
        double max = list.get(0).getY();
        for(int i = 0 ;i < list.size();i++){
            if(list.get(i).getY() >= max)
                max = list.get(i).getY();
        }
        return max;
    }

    public double zMax (List <Point> list){
        double max = list.get(0).getZ();
        for(int i = 0 ;i < list.size();i++){
            if(list.get(i).getZ() >= max)
                max = list.get(i).getZ();
        }
        return max;
    }

    public double xMin (List <Point> list){
        double min = list.get(0).getX();
        for(int i = 0 ;i < list.size();i++){
            if(list.get(i).getX() <= min)
                min = list.get(i).getX();
        }
        return min;
    }

    public double yMin (List <Point> list){
        double min = list.get(0).getY();
        for(int i = 0 ;i < list.size();i++){
            if(list.get(i).getY() <= min)
                min = list.get(i).getY();
        }
        return min;
    }

    public double zMin (List <Point> list){
        double min = list.get(0).getZ();
        for(int i = 0 ;i < list.size();i++){
            if(list.get(i).getZ() <= min)
                min = list.get(i).getZ();
        }
        return min;
    }

    public boolean IsPointInCuboid(Point p,List<Point> cuboid){
        if(p.getX()>=xMin(cuboid) && p.getX()<=xMax(cuboid) && p.getY()>=yMin(cuboid) && p.getY()<=yMax(cuboid) && p.getZ()>=zMin(cuboid) && p.getZ()<=zMax(cuboid))
            return true;
        return false;
    }

    public boolean IsPointInWall(Point p, List<Point> cuboid){
        if(p.getX()==xMin(cuboid) || p.getX()==xMax(cuboid) && p.getY()>=yMin(cuboid) && p.getY()<=yMax(cuboid) && p.getZ()>=zMin(cuboid) && p.getZ()<=zMax(cuboid))
            return true;
        if(p.getX()>=xMin(cuboid) && p.getX()<=xMax(cuboid) && p.getY()==yMin(cuboid) || p.getY()<=yMax(cuboid) && p.getZ()>=zMin(cuboid) && p.getZ()<=zMax(cuboid))
            return true;
        if(p.getX()>=xMin(cuboid) && p.getX()<=xMax(cuboid) && p.getY()>=yMin(cuboid) && p.getY()<=yMax(cuboid) && p.getZ()==zMax(cuboid))
            return true;
        return false;
    }

    public boolean IsSmallCuboidInBigCuboid(List<Point> smallCuboid, List<Point> bigCuboid){
        int count = 0;

        for(int i = 0; i<smallCuboid.size(); i++){
            if(!IsPointInCuboid(smallCuboid.get(i),bigCuboid))
                return false;
            if(smallCuboid.get(0).getZ() == smallCuboid.get(i).getZ())
                count++;
        }

        if(count != 4)
            return false;
        return true;
    }

    public PlaneEquation PlaneXNotEqualZero(List<Point> cuboid){
        for( int i = 0; i<cuboid.size(); i++){
            if(cuboid.get(i).getX()!=0)
                return new PlaneEquation(1,0,0,-cuboid.get(i).getX());

        }
        return null;
    }

    public PlaneEquation PlaneYNotEqualZero(List<Point> cuboid){
        for( int i = 0; i<cuboid.size(); i++){
            if(cuboid.get(i).getY()!=0)
                return new PlaneEquation(0,1,0,-cuboid.get(i).getY());

        }
        return null;
    }

    public PlaneEquation PlaneZNotEqualZero(List<Point> cuboid){
        for( int i = 0; i<cuboid.size(); i++){
            if(cuboid.get(i).getZ()!=0)
                return new PlaneEquation(0,0,1,-cuboid.get(i).getZ());

        }
        return null;
    }

    public PlaneEquation PlaneXEqualZero(){
        return new PlaneEquation(1,0,0,0);
    }

    public PlaneEquation PlaneYEqualZero(){
        return new PlaneEquation(0,1,0,0);
    }

    public double angleBetweenLineAndPlane(PlaneEquation l,PlaneEquation p){
        Vector3D vp = new Vector3D(p.getA(),p.getB(),p.getC());
        Vector3D vl = new Vector3D(l.getA(),l.getB(),l.getC());
        return Math.toDegrees(Math.acos(Math.abs((p.getA()*l.getA()+p.getB()*l.getB()+p.getC()*l.getC())/(vp.lengthxyz()*vl.lengthxyz()))));
    }

    public boolean IsPointInPyramidInCuboid(Point p, Pyramid pyramid, List<Point> cuboid){
        if(PlaneXNotEqualZero(cuboid).IsIncludePoint(pyramid.getP()) || PlaneXEqualZero().IsIncludePoint(pyramid.getP())){
            PlaneEquation pHorizontal = new PlaneEquation(pyramid.getP(),0,1,0);
            PlaneEquation pVertical = new PlaneEquation(pyramid.getP(), 0,0,1);

            LineEquation pPyramidTop = new LineEquation(p,pyramid.getP());
            LineEquation pRightAngleZEqualZero = new LineEquation(p,0,0,1);
            LineEquation pRightAngleYEqualZero = new LineEquation(p,0,1,0);

            Vector3D directProduct1 = VectorDirectProduct(pPyramidTop.VectorDirect(), pRightAngleZEqualZero.VectorDirect());
            Vector3D directProduct2 = VectorDirectProduct(pPyramidTop.VectorDirect(), pRightAngleYEqualZero.VectorDirect());

            PlaneEquation planeIncludeP1 = new PlaneEquation(p,directProduct1.getX(),directProduct1.getY(),directProduct1.getZ());
            PlaneEquation planeIncludeP2 = new PlaneEquation(p,directProduct2.getX(),directProduct2.getY(),directProduct2.getZ());

            double ph = angleBetweenLineAndPlane(planeIncludeP1,pHorizontal);
            double pv = angleBetweenLineAndPlane(planeIncludeP2,pVertical);

            if(ph - 0.000001 <= pyramid.getHorizontalFieldOfView()/2 && pv - 0.0000001<= pyramid.getVerticalFieldOfView()/2)
                return true;
        }

        if(PlaneYNotEqualZero(cuboid).IsIncludePoint(pyramid.getP()) || PlaneYEqualZero().IsIncludePoint(pyramid.getP())){
            PlaneEquation pHorizontal = new PlaneEquation(pyramid.getP(),1,0,0);
            PlaneEquation pVertical = new PlaneEquation(pyramid.getP(), 0,0,1);

            LineEquation pPyramidTop = new LineEquation(p,pyramid.getP());
            LineEquation pRightAngleZEqualZero = new LineEquation(p,0,0,1);
            LineEquation pRightAngleXEqualZero = new LineEquation(p,1,0,0);

            Vector3D directProduct1 = VectorDirectProduct(pPyramidTop.VectorDirect(), pRightAngleZEqualZero.VectorDirect());
            Vector3D directProduct2 = VectorDirectProduct(pPyramidTop.VectorDirect(), pRightAngleXEqualZero.VectorDirect());

            PlaneEquation planeIncludeP1 = new PlaneEquation(p,directProduct1.getX(),directProduct1.getY(),directProduct1.getZ());
            PlaneEquation planeIncludeP2 = new PlaneEquation(p,directProduct2.getX(),directProduct2.getY(),directProduct2.getZ());

            double ph = angleBetweenLineAndPlane(planeIncludeP1,pHorizontal);
            double pv = angleBetweenLineAndPlane(planeIncludeP2,pVertical);

            if(ph-0.0000001 <= pyramid.getHorizontalFieldOfView()/2 && pv-0.000001<= pyramid.getVerticalFieldOfView()/2)
                return true;
        }
        if(PlaneZNotEqualZero(cuboid).IsIncludePoint(pyramid.getP())){
            PlaneEquation pHorizontal = new PlaneEquation(pyramid.getP(),1,0,0);
            PlaneEquation pVertical = new PlaneEquation(pyramid.getP(), 0,1,0);

            LineEquation pPyramidTop = new LineEquation(p,pyramid.getP());
            LineEquation pRightAngleXEqualZero = new LineEquation(p,1,0,0);
            LineEquation pRightAngleYEqualZero = new LineEquation(p,0,1,0);

            Vector3D directProduct1 = VectorDirectProduct(pPyramidTop.VectorDirect(), pRightAngleYEqualZero.VectorDirect());
            Vector3D directProduct2 = VectorDirectProduct(pPyramidTop.VectorDirect(), pRightAngleXEqualZero.VectorDirect());

            PlaneEquation planeIncludeP1 = new PlaneEquation(p,directProduct1.getX(),directProduct1.getY(),directProduct1.getZ());
            PlaneEquation planeIncludeP2 = new PlaneEquation(p,directProduct2.getX(),directProduct2.getY(),directProduct2.getZ());

            double ph = angleBetweenLineAndPlane(planeIncludeP1,pHorizontal);
            double pv = angleBetweenLineAndPlane(planeIncludeP2,pVertical);

            if(ph -0.0000001 <= pyramid.getHorizontalFieldOfView()/2 && pv - 0.0000001 <= pyramid.getVerticalFieldOfView()/2)
                return true;
        }
        return false;
    }


    public boolean IsStadardAxisCuboid(List<Point> list){
        int count =0;
        for(int i = 0; i< list.size();i++){
            if(list.get(i).getX()==0 && list.get(i).getY()==0 && list.get(i).getZ() == 0)
                count++;
            if(list.get(i).getX()!=0 && list.get(i).getY()==0 && list.get(i).getZ() == 0)
                count++;
            if(list.get(i).getX()==0 && list.get(i).getY()!=0 && list.get(i).getZ() == 0)
                count++;
            if(list.get(i).getX()==0 && list.get(i).getY()==0 && list.get(i).getZ() != 0)
                count++;
        }
        if(count ==4)
            return true;

        return false;
    }
}
