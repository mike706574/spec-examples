(ns example.start
  (:require [clojure.spec.alpha :as s]))

;; a `predicate` is a function that
;;   - takes a single argument
;;   - returns a truthy value

;; any `predicate` is a `spec`, so...

  nil?
  string?
  even?
  (fn [x] (> x 65))
  #(= % 55)
  :foo
  #{"a" "b" "c"}

;; ...are all `specs`
