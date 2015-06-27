package com.buruburu.nabi;

import java.util.ArrayList;

public class Models {
  public class ApiResponse{
    public Route route;
  }

  public class Vibration{
    public String situation;
    public long[] vibrations;
  };

  public class Route{
    public int distance;
    public ArrayList<Link> link;
  }

  public class Link{
    public int distance;
    public ArrayList<LonLat> line;
    public boolean passage;
    public boolean roof;
    public String structureType;
    public int structureTypeCode;
    public String type;
    public int typeCode;
    public boolean viaPoint;
  }

  public class Guidance{
    public String crossInfo;
    public ArrayList<String> crossMark;
    public ArrayList<String> crossName;
    public String nextDist;
    public String turningInfo;
  }

  public class LonLat{
    public double lat;
    public double lon;
  }
}
