(ns life.core)

(defn neighbors [[x y]]
  (let [coordinates [-1 0 1]]
    (for [xinc coordinates
          yinc coordinates
          :when (not= xinc yinc 0)]
      [(+ x xinc)
       (+ y yinc)])))

(declare tick)