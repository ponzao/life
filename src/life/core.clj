(ns life.core
  (:use [clojure.contrib.seq-utils :only [separate]])
  (:import [javax.swing JFrame JPanel JButton JLabel]
           [java.awt GridLayout Color Graphics Dimension]
           [java.awt.image BufferedImage]
           [java.awt.event ActionListener]))

(def dim 80)

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

(comment (def generations (atom (create-world dim))))

;; The following adapted from ants.clj

; Pixels per world cell
(def scale 5)

(defn fill-cell [g x y c]
  (doto g
    (.setColor c)
    (.fillRect (* x scale) (* y scale) scale scale)))

(defn render-cell [g status x y]
  (if (= :alive status)
    (fill-cell g x y (Color. 0 0 0 255))
    (fill-cell g x y (Color. 100 100 100 255))))

(defn render [g]
  (let [img (BufferedImage. (* scale dim) (* scale dim)
                            BufferedImage/TYPE_INT_ARGB)
        bg (.getGraphics img)]
    (doto bg
      (.setColor Color/white)
      (.fillRect 0 0 (.getWidth img) (.getHeight img)))
    ()))