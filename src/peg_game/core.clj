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

peg-game.core>
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
              (assoc-in new-board [p1 :connections p2] nbr))
            board
            [[pos dest] [dest pos]])
    board))

(defn connect-right
  [board max-pos pos]
  (let [nbr (inc pos)
        dest (inc nbr)]
    (if-not (or (triangular? nbr) (triangular? pos))
      (connect board max-pos pos dest nbr)
      board)))


(defn connect-down-left
  [board max-pos pos]
  (let [row (row-num pos)
        nbr (+ row pos)
        dest (+ 1 row nbr)]
    (connect board max-pos pos dest nbr)))


(defn connect-down-right
  [board max-pos pos]
  (let [row (row-num pos)
        nbr (+ 1 row pos)
        dest (+ 2 row nbr)]
    (connect board max-pos pos dest nbr)))


(defn add-pos
  [board max-pos pos]
  (let [pegged-board (assoc-in board [pos :pegged] true)]
    (reduce (fn [new-board comp-function]
              (comp-function new-board max-pos pos))
            pegged-board
            [connect-right connect-down-left connect-down-right])))



(defn make-board
  ([row]
   (make-board (row-tri row) 1 {}))
  ([last pos board]
   #_(println last pos board)
   (if (> pos last)
     board
     (recur last (inc pos) (add-pos board last pos)))))

(defn pegged?
  [board pos]
  (get-in board [pos :pegged]))

(defn remove-peg
  [board pos]
  (assoc-in board [pos :pegged] false))

(defn place-peg
  [board pos]
  (assoc-in board [pos :pegged] true))

(defn move-peg
  [board p1 p2]
  (remove-peg (place-peg board p2) p1))


(defn move-valid?
  [board pos dest]
  (get-in board [pos :connections dest]))

(defn move-possible?
  [board pos dest]
  (if-let [nbr (move-valid? board pos dest)]
    (and (pegged? board pos) (pegged? board nbr) (not (pegged? board dest)))))

(defn make-move
  [board pos dest]
  (if (move-possible? board pos dest)
    (remove-peg (move-peg board pos dest) (get-in board [pos :connections dest]) ) ))


(defn conn [board pos]
  (keys (get-in board [pos :connections])))

(defn game-over?
  ([board max-pos]
   (game-over? board 1 (conn board 1 ) max-pos false))
  ([board pos dest max-pos flag]
   (if (or (> pos max-pos) flag)
     (not flag)
     (if (empty? dest)
       (game-over? board (inc pos) (conn board (inc pos)) max-pos flag)
       (game-over? board pos (rest dest) max-pos (move-possible? board pos (first dest)))))))


(def alpha-start 97)
(def alpha-end 123)
(def letters (map (comp str char) (range alpha-start alpha-end)))

(defn render-pos
  [board pos]
  (str pos
       (if (get-in board [pos :pegged])
         "(0)"
          "(_)")))

(defn row-pos
  [row]
  (range (inc (or (row-tri (dec row)) 0))
         (inc (row-tri row))))

(defn render-pad
  [row-num rows]
  (let [padd-length   (/ (* (- rows row-num) 3) 2)]
    (apply str (take padd-length (repeat " ")))))

(defn render-row
  [board row-num rows]
  (str (render-pad row-num rows )
       (clojure.string/join " " (map (partial render-pos board)
                                     (row-pos row-num)))))

(defn printboard
  [board rows]
  (doseq [row-num (range 1 (inc rows))]
    (println (render-row board row-num))))
