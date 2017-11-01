(ns example.three
  (:require [clojure.spec.alpha :as s]))

;; but remember, we want to talk about parts of our data `specifically`, like...

;; a `user` has
;;  - a `user` has `name` of a `user` is a 4-16 character string
;;  - a `type` of `user` is either super or regular
;;  - the `age` of a `user` is 0 to 125 years
;;  - the `gender` of a`user` is male or female
;;  - the `pin` of a `user` is a 4-digit numeric string
;;  - the optional `code` of a `user` is
;;    - type `a`, a 5 character numeric string
;;    - type `b`, a 7 character alphanumeric string

;; ...so we can give `predicate` specs `specific` names with `def`:

(s/def :domain.user/type #{"super" "regular"})

(s/explain-str :domain.user/type "hardcore")
;; => val: "hardcore" fails spec: :domain.user/type predicate: #{"super" "regular"}

(s/def :domain.user/pin #(re-matches #"[0-9]{4}" %))
(s/explain-str :domain.user/pin "12AB")
;; => val: "12AB" fails spec: :domain.user/pin predicate: (re-matches #"[0-9]{4}" %)
