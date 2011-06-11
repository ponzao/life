(ns life.core)

(defn neighbors [[x y]]
  (let [coordinates [-1 0 1]]
    (for [xinc coordinates
          yinc coordinates
          :when (not= xinc yinc 0)]
      [(+ x xinc)
       (+ y yinc)])))

(defn create-grid []
  (vec (for [x (range 9)]
         nil)))

(defn create-cell [grid [x y :as cell]]
  (let [row-length (int (Math/sqrt (count grid)))
        position (+ x (* row-length y))]
    (assoc grid position cell)))

(defn survivors [grid]
  (for [cell grid]
    (i)))

(defn tick [grid])

(defn generations [initial-grid]
  (iterate tick initial-grid))