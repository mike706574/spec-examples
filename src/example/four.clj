(ns example.four
  (:require [clojure.spec.alpha :as s]))

;; we can also `compose` our specs in various ways

;; `and`

(defn name-length-ok? [name] (<= 4 (count name) 16))
(s/def :domain.user/name (s/and string? name-length-ok?))

(s/explain-str :domain.user/name "bob")
;; => val: "bob" fails spec: :domain.user/name predicate: name-length-ok?

;; `or`

(defn code-a? [code] (re-matches #"[0-9]{5}" code))
(defn code-b? [code] (re-matches #"[A-Z0-9]{7}" code))

(s/def :domain.user/code (s/or :a code-a?
                               :b code-b?))

(s/explain-str :domain.user/code "12")
;; => val: "12" fails spec: :domain.user/code at: [:a] predicate: code-a?
;;    val: "12" fails spec: :domain.user/code at: [:b] predicate: code-b?

;; `keys`

(s/def :domain/user (s/keys :req-un [:domain.user/name
                                     :domain.user/type
                                     :domain.user/age
                                     :domain.user/pin]
                            :opt-un [:domain.user/code]))
