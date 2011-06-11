(ns life.core-test
  (:use [life.core :as life]
        midje.sweet))

(fact "neighbors contains a cell's friends"
      (life/neighbors [1 1]) => (just [ [0 2] [1 2] [2 2]
                                        [0 1]       [2 1]
                                        [0 0] [1 0] [2 0] ]
                                      :in-any-order))
(fact "grid is 3x3"
      (life/create-grid) => [nil nil nil
                             nil nil nil
                             nil nil nil])

(fact "a living cell can be created within a grid"
      (life/create-cell (life/create-grid) [1 1]) => [nil nil   nil
                                                      nil [1 1] nil
                                                      nil nil   nil])
