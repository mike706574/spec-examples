(ns example.one
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))

;; `spec` lets us continue to use `general` data structures

{:name "mike"
 :type "super"
 :gender "male"
 :state "WI"
 :age 26
 :pin "1234"
 :code "A1BC5XY"}

;; while allowing us to `specify` and `validate` them... `specifically`

;; a `user` has
;;  - a `user` `name` is a 4-16 character string
;;  - a `type` of `user` is either super or regular
;;  - the `age` of a `user` is 0 to 125 years
;;  - the `gender` of a`user` is male or female
;;  - the `pin` of a `user` is a 4-digit numeric string
;;  - the optional `code` of a `user` is
;;    - type `a`, a 5 character numeric string
;;    - type `b`, a 7 character alphanumeric string

(gen/generate (s/gen :domain/user))

(def mike {:name "mike"
           :type "super"
           :gender "male"
           :state "WI"
           :age 26
           :pin "1234"
           :code "A1BC5XY"})

(s/explain-str :domain/user mike)
;; => true

(def bob {:name "bob"
          :type "hardcore"
          :state "oiho"
          :age -5
          :gender :male
          :code "21"})

(s/valid? :domain/user bob)
;; => false

(s/explain-str :domain/user bob)
;; => string explanation

(s/explain-data :domain/user bob)
;; => data explanation
