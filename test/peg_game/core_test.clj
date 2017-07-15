(ns peg-game.core-test
  (:require [clojure.test :refer :all]
            [peg-game.core :refer :all]
            [expectations :as expect]))

#_(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

(expect/expect true (triangular? 10))

(expect/expect 10 (row-tri 4))

(expect/expect 2 (row-num 3))

(expect/expect 4 (row-num 10))
