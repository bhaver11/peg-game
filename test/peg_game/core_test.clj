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

(expect/expect {1 {:connections {4 2}} 4 {:connections {1 2}}} (connect {} 15 1 4 2))

(expect/expect {4 {:connections {6 5}} 6 {:connections {4 5}}} (connect-right {} 15 4))

(expect/expect {1 {:connections {4 2}} 4 {:connections {1 2}}} (connect-down-left {} 15 1))

(expect/expect {1 {:connections {6 3}} 6 {:connections {1 3}}} (connect-down-right {} 15 1))



(expect/expect {1 {:pegged true :connections {4 2,6 3}}
                 4 {:connections {1 2}}
                 6 {:connections {1 3}}} (add-pos {} 15 1) )

(expect/expect true (pegged? {1 {:pegged true}} 1 ))

(def a (make-board 5))


(expect/expect nil (move-valid? a 4 2))

(expect/expect true (game-over? a 15))


(expect/expect '(4 5 6) (row-pos 3))


(expect/expect "      " (render-pad 1 5))
