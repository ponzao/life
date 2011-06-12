(ns life.core
  (:use [clojure.contrib.seq-utils :only [separate]]))

(defn join-neighbors [[x y] world]
  (for [xinc [-1 0 1]
        yinc [-1 0 1]
        :when (not= xinc yinc 1)]
    [(+ x xinc) (+ y yinc)]))

(defn neighbors-by-coordinates [world]
  (for [{coordinates :coordinates status :status} world]
    [coordinates {:status status}]))

(defn cells-with-neighbors [world]
  (into {} (map (fn [[coordinates data]]
                  [coordinates
                   (assoc data :neighbors (join-neighbors coordinates (neighbors-by-coordinates world)))])
                (neighbors-by-coordinates world))))

(def *world* (cells-with-neighbors (create-world)))

(defn neighbor-count [{coordinates :coordinates
                       neighbors   :neighbors   :as cell}]
  (->> (for [neighbor neighbors]
         (:status (get *world* neighbor)))
       (filter (complement nil?))
       (separate #(= :alive %))
       (map (juxt first count))
       (into {})))

(defn tick [world]
  (-> (for [cell world]
        cell)
      set))

(defn create-world []
  #{{:coordinates [0 2] :status :dead} {:coordinates [1 2] :status :dead}  {:coordinates [2 2] :status :dead}
    {:coordinates [0 1] :status :dead} {:coordinates [1 1] :status :alive} {:coordinates [2 1] :status :dead}
    {:coordinates [0 0] :status :dead} {:coordinates [1 0] :status :dead}  {:coordinates [2 0] :status :dead}})

(defn generations []
  (iterate tick (create-world)))