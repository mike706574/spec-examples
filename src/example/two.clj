(ns example.two
  (:require [clojure.spec.alpha :as s]))

;; a `predicate` is a function that
;;   - takes a single argument
;;   - returns a truthy value

;; any `predicate` is a `spec`, so...

  nil?
  string?
  pos-int?
  #(> % 5)
  #{"super" "regular"}

;; ...are all `specs`

  (s/explain-str nil? nil)
  ;; => Success!

  (s/explain-str string? "mike")
  ;; => Success!

  (s/explain-str pos-int? -2)
  ;; => val: -2 fails predicate: :clojure.spec.alpha/unknown

  (s/explain-str #(<= 4 (count %) 16) "1234")
  ;; => Success!

  (s/explain-str #{"super" "regular"} "hardcore")
  ;; => val: "hardcore" fails predicate: :clojure.spec.alpha/unknown
