(ns life.core
  (:use [clojure.contrib.seq-utils :only [separate]]))

(defn- create-cells [size]
  (let [xs (range size)
        ys xs]
    (for [x xs y ys]
      {:coordinates [x y]
       :status :alive})))

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
  (cond (<= alive 2) :dead
        (and (= self :alive) (<= 2 alive 3)) :alive
        (and (= self :alive) (< 3 alive)) :dead
        (and (= self :dead) (= alive 3)) :alive))

(defn tick [world]
  (->> world
       (map
        (fn [[k v]]
          [k
           (assoc v :status (live? (:status v) (neighbor-count world v)))]))
       (into {})))

(defn generations []
  (iterate tick (create-world 7)))

(defn draw-world [world]
  (let [size (Math/sqrt (count world))
        coordinates (range size)]
    (doseq [x coordinates y coordinates]
      (let [status (:status (get world [x y]))]
        (if (= y (dec size))
          (println status " ")
          (print status " "))))
    (println)))