(ns life.core
  (:use [clojure.contrib.seq-utils :only [separate]])
  (:import [javax.swing JFrame JPanel JButton JLabel]
           [java.awt GridLayout Color Graphics Dimension Canvas Frame]
           [java.awt.image BufferedImage]
           [java.awt.event ActionListener]))

(def *dimension* 80)

(defn- create-cells [size]
  (let [xs (range size)
        ys xs]
    (for [x xs y ys]
      {:coordinates [x y]
       :status (if (= (int (rand 2)) 1) :alive :dead)})))

(defn- neighbor-coordinates [[x y]]
  (for [xinc [-1 0 1]
        yinc [-1 0 1]
        :when (not= xinc yinc 0)]
    [(+ x xinc) (+ y yinc)]))

(defn- create-world [size]
  (let [world (create-cells size)]
    (into {}
          (map (fn [{coordinates :coordinates :as cell}]
                 [coordinates
                  (assoc cell :neighbors (neighbor-coordinates coordinates))])
               world))))

(defn- neighbor-count [world {neighbors :neighbors}]
  (->> (for [neighbor neighbors]
         (:status (get world neighbor)))
       (filter (complement nil?))
       (separate #(= :alive %))
       (map count)))

(defn- live? [self [alive dead]]
  (cond (and (= self :alive) (< alive 2)) :dead
        (and (= self :alive) (<= 2 alive 3)) :alive
        (and (= self :alive) (< 3 alive)) :dead
        (and (= self :dead) (= alive 3)) :alive
        :else :dead))

(defn tick [world]
  (->> world
       (map
        (fn [[k v]]
          [k
           (assoc v :status (live? (:status v) (neighbor-count world v)))]))
       (into {})))

(def generations (atom (create-world *dimension*)))

(def *scale* 5)

(def grid (proxy [Canvas] []
            (paint [g] (let [world (swap! generations tick)]
                        (doseq [x (range *dimension*)
                                 y (range *dimension*)]
                           (let [alive? (= :alive (:status (get world [x y])))]
                             (when (and alive? (= x y))
                               (.fillRect g (+ *scale* x) (+ *scale* y) *scale* *scale*))))))))

(def frame (doto (JFrame.)
             (.add grid)
             (.setSize 640 480)
             (.setVisible true)))