(ns peg-game.core
  (require [clojure.set :as set])
  (:gen-class))

(declare successful-move prompt-move game-over query-rows)

(defn tri*
  ([] (tri* 0 1))
  ([sum n]
   (let [new_sum (+ sum n)]
     (cons new_sum (lazy-seq (tri* new_sum (inc n)))))))

(def tri (tri*))

(defn triangular? [n]
  (= n (last (take-while #(>= n %) (tri*)))))

(defn row-tri
  "returns triangular no. at the end of the row"
  [n]
  (last (take n tri)))

(defn row-num
  [pos]
  (inc (count (take-while #(> pos %) tri))))
