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

(defn connect
  [board max-pos pos dest nbr]
  (if (<= dest max-pos)
    (reduce (fn [new-board [p1 p2]]
              (assoc-in new-board [p1 :connections p2] nbr)) board [[pos dest] [dest pos]])
    board))

(defn connect-right
  [board max-pos pos]
  (let [nbr (inc pos)
        dest (inc nbr)]
    (if-not (or (triangular? nbr) (triangular? pos))
      (connect board max-pos pos dest nbr))))

(connect-right {} 15 1)

(defn connect-down-left
  [board max-pos pos]
  (let [row (row-num pos)
        nbr (+ row pos)
        dest (+ 1 row nbr)]
    (connect board max-pos pos nbr dest)))

(connect-down-left {} 15 2)

(defn connect-down-right
  [board max-pos pos]
  (let [row (row-num pos)
        nbr (+ 1 row pos)
        dest (+ 2 row nbr)]
    (connect board max-pos pos nbr dest)))

(connect-down-right {} 15 3)

(defn add-pos
  [board max-pos pos]
  (let [pegged-board (assoc-in board [pos :pegged] true)]
    (reduce (fn [new-board comp-function]
              (comp-function new-board max-pos pos))
            pegged-board [connect-right connect-down-left connect-down-right])))

(add-pos {} 15 1)
