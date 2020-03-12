package de.flocki;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import oscP5.*;
import processing.core.PApplet;

class Flock {

    ArrayList boids; // An arraylist for all the boids
    ConcurrentHashMap<Integer, Cluster> clusters = new ConcurrentHashMap<>();
    int ttclusters = 0;

    FlockingProperties props = new FlockingProperties();

    int graincount = 200;

    Flock() {
        boids = new ArrayList(); // Initialize the arraylist
        ++ttclusters;
        clusters.put(0, new Cluster(0));
    }

    void toggleNbhdRep() {
        for (int i = 0; i < boids.size(); i++) {
            Boid b = (Boid) boids.get(i);
            b.neighborhoodrepresentation = !b.neighborhoodrepresentation;
        }
    }

    void toggleWebRepp() {
        for (int i = 0; i < boids.size(); i++) {
            Boid b = (Boid) boids.get(i);
            b.connectivity = !b.connectivity;
        }
    }

    void run() {

        OscBundle bundle = new OscBundle();
        UndirectedSparseGraph<Boid, BoidVertex> boid_graph = new UndirectedSparseGraph<>();

        for (int i = 0; i < boids.size(); i++) {

            Boid b = (Boid) boids.get(i);

            b.run(boids);  // Passing the entire list of boids to each boid individually

            boid_graph.addVertex(b);

            for(Boid neighbor : b.neighbors)
                boid_graph.addEdge(new BoidVertex(), new Pair<>(b, neighbor));

            OscMessage msg = new OscMessage("/" + i);

            float tx = b.loc.x - 250;
            float ty = (b.loc.y - 250) * -1;
            float tz = (b.loc.z - 250) * -1;

            float rad = (float) Math.sqrt(tx * tx + ty * ty + tz * tz);

            msg.add(tx);
            msg.add(ty);
            msg.add(tz);
            msg.add(rad / 353.55);
            msg.add(b.vel.magnitude());

            bundle.add(msg);
        }

        WeakComponentClusterer<Boid, BoidVertex> clusterer = new WeakComponentClusterer<Boid, BoidVertex>();

        Set<Set<Boid>> nclusters =  clusterer.apply(boid_graph);

        int cluster_id = 0;

        ArrayList<Set<Boid>> cllist = new ArrayList<>(nclusters);

        cllist.sort(Comparator.comparingInt(Set::size));


        for(int y = cllist.size() - 1 ; y >=0; --y) {

            Set<Boid> boids = cllist.get(y);

            int lastcl = mostCommonCluster(boids);

            Cluster cl = clusters.get(lastcl);

            if(cl.used) {
                cl = new Cluster(ttclusters++);
                cl.used = true;
                clusters.put(cl.id, cl);
            } else {
                cl.used = true;
            }

            for(Boid sb : boids) {

                sb.construction.r = cl.r;
                sb.construction.g = cl.g;
                sb.construction.b = cl.b;

                sb.last_cluster = cl.id;
            }

        }

        Iterator<Map.Entry<Integer, Cluster>> cl_it = clusters.entrySet().iterator();

        while(cl_it.hasNext()){

            Cluster cl = cl_it.next().getValue();

            if(!cl.used)
                clusters.remove(cl);

            cl.used = false;
        }

        // oscP5.send(bundle, remote);
    }

    void setup(PApplet renderer, Matter matter, Vector3D ghetto) {

        for (int i = 0; i < graincount; i++) {

            Boid boid = new Boid(new Vector3D(
                    (float) Math.random() * renderer.width / 2, (float) Math.random() * renderer.height / 2), i);

            boid.renderer = renderer;
            boid.props = props;
            boid.matter = matter;
            boid.ghetto = ghetto;
            boid.flock = this;

            boids.add(boid);
        }
    }

    int mostCommonCluster(Set<Boid> boids) {

        Map<Integer, Integer> map = new HashMap<>();

        for (Boid t : boids) {

            int id = t.last_cluster;

            Integer val = map.get(id);
            map.put(id, val == null ? 1 : val + 1);
        }

        Map.Entry<Integer, Integer> max = null;

        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max.getKey();
    }
}