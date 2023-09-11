/*
 * Copyright (C) 2023 Hal Perkins.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Winter Quarter 2023 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */


//IMPORTANT INSTRUCTIONS
//click run on SparkServer
// in terminal:
// cd hw-campuspaths
// npm install --no-audit
// npm start


package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.CampusMap;
import pathfinder.ModelAPI;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class SparkServer {


    // has my campus map and the SparkServer routes that use this
    // graph. Has the major functions that allows my campus paths works properly
    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        ModelAPI map = new CampusMap(); // builds building list and graph


        // gives all the building names and respective key names (which are used to
        // find buildings) as a JSON string
        Spark.get("/buildings", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                Gson gson = new Gson();
                return gson.toJson(map.buildingNames());
            }
        });



        // takes 2 buildings as starting and destinations and makes a path between them
        // then returns it as a JSON string.
        Spark.get("/draw-path", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String start = request.queryParams("Start");
                String dest = request.queryParams("Dest");
                if(start == null || dest == null) {
                    Spark.halt(400, "must have start and destination");
                }
                // path that was found
                Path<Point> path = new Path<>(new Point(0,0));
                try {
                    path = map.findShortestPath(start, dest);
                } catch(IllegalArgumentException e) {
                    Spark.halt(400, "start and dest must be buildings");
                }

                Gson gson = new Gson();
                return gson.toJson(path);
            }
        });
    }

}
